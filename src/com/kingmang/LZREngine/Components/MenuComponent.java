package com.kingmang.LZREngine.Components;

import com.kingmang.LZREngine.Exeptions.EntityException;
import com.kingmang.LZREngine.Game.GameFrame;
import com.kingmang.LZREngine.Enity.Input;
import com.kingmang.LZREngine.Engine.RenderComponent;
import com.kingmang.LZREngine.Engine.Renderer;
import com.kingmang.LZREngine.Scene.SceneController;
import com.kingmang.LZREngine.Exeptions.SceneException;
import com.kingmang.LZREngine.Geometry.Vector2f;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class MenuComponent extends RenderComponent{
    private Color border, text, borderhighlighted, texthiglighted, borderselected, textselected;
    private Font font;
    private float padding = 10f;
    private float spacing = 10f;
    private Cursor onMenuCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor;
  

    public static class MenuItem implements Comparable<MenuItem>{
        String text;
        String sceneKey;
        Method callback;
        Object obj;
        Object[] args;
        float ordering;

        public MenuItem(String text, String sceneKey, float ordering){
            this.text = text;
            this.sceneKey = sceneKey;
            this.ordering = ordering;
        }
        public MenuItem(String text, float ordering, Method callback, Object obj, Object... args){
            this.text = text;
            this.callback = callback;
            this.obj = obj;
            this.args = args;
            this.ordering = ordering;
        }
        public void invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            callback.invoke(obj, args);
        }
        public boolean isScene() {
            return this.callback==null;
        }
        public int compareTo(MenuItem o) {
            return (int)Math.signum(ordering-o.ordering);
        }

    }

    private ArrayList<MenuItem> menuItems;
    private MenuItem highlightedItem;
    private MenuItem selectedItem;
    private int highlightedItemIndex = -1;
    private int selectedItemIndex = -1;
    private float greatestWidthItem;
    private boolean enabled = true;

    public MenuComponent(String id, Color border, Color text, Color borderhighlighted, Color texthiglighted, Font font){
        this(id, border, text, borderhighlighted, texthiglighted, border, text, font);

    }
    public MenuComponent(String id, Color border, Color text, Color borderhighlighted, Color texthiglighted, Color borderselected, Color textselected, Font font){
        super(id);
        this.border = border;
        this.text = text;
        this.borderhighlighted = borderhighlighted;
        this.texthiglighted = texthiglighted;
        this.borderselected = borderselected;
        this.textselected = textselected;
        this.font = font;
        menuItems = new ArrayList<MenuItem>();
        this.defaultCursor = GameFrame.get().getCursor();
    }

    public void addMenuItem(String text, String sceneKey, float ordering){
        this.menuItems.add(new MenuItem(text, sceneKey, ordering));
        float width = (float)Renderer.get().getFontMetrics(font).stringWidth(text);
        if(width>greatestWidthItem) greatestWidthItem = width;
        Collections.sort(this.menuItems);
    }
    public void addMenuItem(String text, float ordering, Method callback, Object obj, Object... args){
        this.menuItems.add(new MenuItem(text, ordering, callback, obj, args));
        float width = (float)Renderer.get().getFontMetrics(font).stringWidth(text);
        if(width>greatestWidthItem) greatestWidthItem = width;
        Collections.sort(this.menuItems);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    private void selectNext(){
        int next = highlightedItemIndex+1;
        if(next>=menuItems.size()){
            highlightedItemIndex = 0;
        }else{
            highlightedItemIndex = next;
        }
        highlightedItem = menuItems.get(highlightedItemIndex);
    }
    private void selectPrevious(){
        int prev = highlightedItemIndex-1;
        if(prev<0){
            highlightedItemIndex = menuItems.size()-1;
        }else{
            highlightedItemIndex = prev;
        }
        highlightedItem = menuItems.get(highlightedItemIndex);
    }
    @Override
    public void update(Input input, float updatesPerSecond) throws EntityException {
        if(!isEnabled()) return;
        if(input.isKeyPressed()){
            KeyEvent evt = input.getKeyPressed();
            if(evt.getKeyCode()==KeyEvent.VK_DOWN){
                selectNext();
                selectedItemIndex = -1;
                selectedItem = null;
            }else if(evt.getKeyCode()==KeyEvent.VK_UP){
                selectPrevious();
                selectedItemIndex = -1;
                selectedItem = null;
            }else if(highlightedItem!=null&&evt.getKeyCode()==KeyEvent.VK_SPACE){
                selectedItem = highlightedItem;
                selectedItemIndex = highlightedItemIndex;
                actionMenuItem(highlightedItem);
            }
        }
        if(input.isMouseMoved()){
            MouseEvent me = input.getMouseMoved();
            int mi = mouseOverItem(new Vector2f(me.getPoint()));
            highlightedItemIndex = mi;
            if(highlightedItemIndex>-1){
                GameFrame.get().setCursor(onMenuCursor);
                highlightedItem = menuItems.get(highlightedItemIndex);
            }else{
                GameFrame.get().setCursor(defaultCursor);
                highlightedItem = null;
            }
            if(selectedItemIndex!=highlightedItemIndex){
                selectedItemIndex = -1;
                selectedItem = null;
            }
        }
        if(input.isMousePressed()){
            MouseEvent me = input.getMousePressed();
            int mi = mouseOverItem(new Vector2f(me.getPoint()));
            selectedItemIndex = mi;
            if(selectedItemIndex>-1){
                selectedItem = menuItems.get(selectedItemIndex);
            }else{
                selectedItem = null;
            }
        }
        if(highlightedItem!=null&&input.isMouseClicked()){
            MouseEvent me = input.getMouseClicked();
            actionMenuItem(highlightedItem);
            selectedItemIndex = -1;
            selectedItem = null;
        }
    }
    private void actionMenuItem(MenuItem item) throws EntityException{
        GameFrame.get().setCursor(defaultCursor);
        if(item.isScene()){
            try {
                SceneController.get().setCurrentScene(item.sceneKey);
            } catch (SceneException ex) {
                throw new EntityException(ex);
            }
        }else{
            try {
                item.invoke();
            } catch (IllegalAccessException ex) {
                throw new EntityException(ex);
            } catch (IllegalArgumentException ex) {
                throw new EntityException(ex);
            } catch (InvocationTargetException ex) {
                throw new EntityException(ex);
            }
        }
    }

    private int mouseOverItem(Vector2f mousePos){
        Vector2f pos = getOwner().getPosition();
        FontMetrics fm = Renderer.get().getFontMetrics(font);
        float blockheight = fm.getHeight() + (2*padding);
        Vector2f topLeft = 
                new Vector2f(pos.x - (this.greatestWidthItem/2)-padding,
                pos.y);
        Vector2f bottomRight = 
                new Vector2f(pos.x + (this.greatestWidthItem/2)+padding,
                pos.y+(menuItems.size()*(blockheight+spacing)));
        //In bounds check
        if(mousePos.x<topLeft.x || mousePos.x > bottomRight.x
                || mousePos.y < topLeft.y || mousePos.y > bottomRight.y){
            return -1;
        }
        float yPosWithinMenu = mousePos.y - pos.y;
        int index = (int)Math.floor(yPosWithinMenu/(blockheight+spacing));
        if(index == menuItems.size()){
            return menuItems.size()-1;
        }
        return index;
    }



    @Override
    public void render(Graphics2D g) {
        Vector2f pos = getOwner().getPosition();

        FontMetrics fm = Renderer.get().getFontMetrics(font);
        Font origf = g.getFont();
        Color origc = g.getColor();
        Stroke origs = g.getStroke();
        g.setFont(font);
        g.setStroke(new BasicStroke(3f));
        float currentY = pos.y;
        float x = pos.x - (this.greatestWidthItem/2)-padding;
        float borderwidth = this.greatestWidthItem + (2*padding);
        float borderheight = fm.getHeight() + (2*padding);
        float textX;

        Rectangle2D borderR;
        synchronized(this){
            for(MenuItem item : menuItems){
                if (item.equals(selectedItem)){
                    g.setColor(borderselected);
                }else if (item.equals(highlightedItem)) {
                    g.setColor(borderhighlighted);
                }else{
                    g.setColor(border);
                }
                borderR = new Rectangle2D.Float(x, currentY, borderwidth, borderheight);
                g.draw(borderR);
                if (item.equals(selectedItem)){
                    g.setColor(textselected);
                }else if(item.equals(highlightedItem)) {
                    g.setColor(texthiglighted);
                }else{
                    g.setColor(text);
                }
                textX = pos.x - (fm.stringWidth(item.text)/2);
                g.drawString(item.text, textX, currentY+fm.getHeight()+padding);
                currentY += borderheight+spacing;
            }
        }
        g.setColor(origc);
        g.setFont(origf);
        g.setStroke(origs);

    }

}

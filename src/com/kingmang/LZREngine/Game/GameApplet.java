package com.kingmang.LZREngine.Game;

import com.kingmang.LZREngine.Engine.*;
import com.kingmang.LZREngine.Scene.SceneController;
import com.kingmang.LZREngine.Exeptions.SceneException;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameApplet extends Applet implements GameContainer, AWTEventListener,
        MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener{

    private static GameApplet instance;
    public GameApplet(){
        super();
    }

    @Override
    public void init(){
        initialise();
        GameApplet.instance = this;
    }
    @Override
    public void start(){
        try {
            String gameClassStr = getParameter("game");
            Class<? extends Game> gameClass = (Class<? extends Game>)Class.forName(gameClassStr);
            Constructor<? extends Game> constructor =
                    gameClass.getConstructor(new Class[]{String.class, Integer.TYPE, Integer.TYPE});
            
            String title = getParameter("title");
            if(title==null) title = Game.DefaultTitle;
            String widthStr = getParameter("width");
            String heightStr = getParameter("height");
            int width = Game.DefaultDimensions.width;
            int height = Game.DefaultDimensions.height;
            if(widthStr!=null){
                width = Integer.parseInt(widthStr);
            }
            if(heightStr!=null){
                height = Integer.parseInt(heightStr);
            }
            Game game = constructor.newInstance(new Object[]{title, width, height});
            game.setRunningAsApplet(true);
            int fps = Game.DefaultFPS;
            if(getParameter("fps")!=null){
                fps = Integer.parseInt(getParameter("fps"));
            }
            game.setRequiredFPS(fps);

            boolean showFps = Game.DefaultShowFPS;
            if(getParameter("showfps")!=null){
                showFps = Boolean.parseBoolean(getParameter("showfps"));
            }
            game.setShowFPS(showFps);

            game.start();
        } catch (ClassNotFoundException ex) {
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Class Not Found Exception at Game Start: "+ex.getMessage());
        } catch (ClassCastException ex) {
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Class Cast Exception at Game Start: "+ex.getMessage());
        } catch (NoSuchMethodException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("No Such Method Exception at Game Start: "+ex.getMessage());
        } catch (NumberFormatException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Number Format Exception Exception at Game Start: "+ex.getMessage());
        } catch (InstantiationException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Instantiation Exception at Game Start: "+ex.getMessage());
        } catch (InvocationTargetException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Invocation Target Exception at Game Start: "+ex.getMessage());
        } catch (IllegalAccessException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Illegal AccessException Exception at Game Start: "+ex.getMessage());
        } catch (SceneException ex){
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Scene Exception at Game Start: "+ex.getMessage());
        }
    }
    @Override
    public String getAppletInfo() {
        return "Basic Game Engine Applet, Version 0\n"
            + "Copyright 3void";
    }
    @Override
    public String[][] getParameterInfo(){
        return new String[][]{
            {"game", "String", "Canonical name of game class"},
            {"title", "String", "Game title"},
            {"width", "int", "Game width"},
            {"height", "int", "Game height"},
            {"fps", "int", "fps to aim for"},
            {"showfps", "boolean", "Whether or not to show the fps"}
        };
    }
    @Override
    public void stop(){
        Engine.get().stop();
    }
    @Override
    public void destroy(){

    }



    public static GameApplet get(){
        if(instance==null) throw new RuntimeException("Instance not yet created");
        return GameApplet.instance;
    }

    public int getAvailableWidth(){
        return getWidth()-(this.getInsets().left+this.getInsets().right);
    }
    public int getAvailableHeight(){
        return getHeight()-(this.getInsets().top+this.getInsets().bottom);
    }

    private void initialise(){
        Renderer.instantiate(this.getGraphicsConfiguration());
        this.addComponentListener(this);
        this.add(Renderer.get());
        Renderer.get().setBounds(this.getInsets().left, this.getInsets().top, getAvailableWidth(), getAvailableHeight());
        Toolkit.getDefaultToolkit().addAWTEventListener(this,
                AWTEvent.KEY_EVENT_MASK);
        Renderer.get().addMouseMotionListener(this);
        Renderer.get().addMouseListener(this);
        Renderer.get().addMouseWheelListener(this);
    }
    @Override
    public final void repaint(){
        Graphics g;
        try{
            g = Renderer.get().getGraphics();
            paint(g);
            g.dispose();
        }catch(Exception e){
            Engine.get().exceptionThrown(e);
            Engine.get().fatalError("Graphics context error: "+ e);
        }
    }


    @Override
    public final void paint(Graphics g){
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Renderer.get().render((Graphics2D)g);
    }

    public void keyTyped(KeyEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().keyTyped(e);
        }
    }

    public void keyPressed(KeyEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().keyReleased(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseMoved(e);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseReleased(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseEntered(e);
        }
    }

    public void mouseExited(MouseEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseExited(e);
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if(SceneController.get().hasCurrentScene()){
            SceneController.get().getCurrentScene().mouseWheelMoved(e);
        }
    }

    public void componentResized(ComponentEvent e) {
        Renderer.get().resized();
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void eventDispatched(AWTEvent event) {
        if(event instanceof KeyEvent){
            KeyEvent ke = (KeyEvent) event;
            if(ke.getID()==KeyEvent.KEY_PRESSED){
                keyPressed(ke);
            }else if(ke.getID()==KeyEvent.KEY_RELEASED){
                keyReleased(ke);
            }else if(ke.getID()==KeyEvent.KEY_TYPED){
                keyTyped(ke);
            }
        }
    }

 
}

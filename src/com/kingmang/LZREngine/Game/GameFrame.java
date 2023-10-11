package com.kingmang.LZREngine.Game;

import com.kingmang.LZREngine.Engine.Engine;
import com.kingmang.LZREngine.Engine.Renderer;
import com.kingmang.LZREngine.Scene.SceneController;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class GameFrame extends Frame implements GameContainer, AWTEventListener,
        MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener{

    private static GameFrame instance;
    public GameFrame(){
        super();
        initialise();
    }
    private GameFrame(String title){
        super(title);
        initialise();
    }
    private GameFrame(GraphicsConfiguration gc){
        super(gc);
        initialise();
    }
    private GameFrame(String title,GraphicsConfiguration gc){
        super(title,gc);
        initialise();
    }
    public static void instantiate(){
        if(instance!=null) throw new RuntimeException("Instance already exists");
        GameFrame.instance = new GameFrame();
    }
    public static void instantiate(String title){
        if(instance!=null) throw new RuntimeException("Instance already exists");
        GameFrame.instance = new GameFrame(title);
    }
    public static void instantiate(GraphicsConfiguration gc){
        if(instance!=null) throw new RuntimeException("Instance already exists");
        GameFrame.instance = new GameFrame(gc);
    }
    public static void instantiate(String title,GraphicsConfiguration gc){
        if(instance!=null) throw new RuntimeException("Instance already exists");
        GameFrame.instance = new GameFrame(title,gc);
    }
    public static GameFrame get(){
        if(instance==null) throw new RuntimeException("Instance not yet created");
        return GameFrame.instance;
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

    boolean setantialias = false;
    @Override
    public final void paint(Graphics g){
        if(!setantialias){
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                setantialias = true;
        }
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

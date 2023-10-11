package com.kingmang.LZREngine.Scene;

import com.kingmang.LZREngine.Engine.Renderer;
import com.kingmang.LZREngine.Exeptions.SceneException;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public abstract class Scene {

    private final String key;
    public Scene(String key){
        this.key = key;
    }
    
    public abstract void initialise() throws SceneException;
    public abstract void onGameLoad() throws SceneException;
    public abstract void render(Graphics2D g);
    public abstract void update(float updatesPerSecond) throws SceneException;

    
    public void onPreEnterScene(Scene from) throws SceneException {}
    public void onEnterScene(Scene from) throws SceneException {}
    public void onLeaveScene(Scene to) throws SceneException {}
    
    public final int getWidth(){
        return Renderer.get().getWidth();
    }
    public final int getHeight(){
        return Renderer.get().getHeight();
    }
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseWheelMoved(MouseWheelEvent e) {}


    public String getKey() {
        return key;
    }

}

package com.kingmang.LZREngine.Game;

import com.kingmang.LZREngine.Engine.*;
import com.kingmang.LZREngine.Scene.Scene;
import com.kingmang.LZREngine.Scene.SceneController;
import com.kingmang.LZREngine.Exeptions.SceneException;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class Game {

    static final int DefaultFPS = 60;
    static final String DefaultTitle = "No Title";
    static final boolean DefaultShowFPS = false;
    static final Dimension DefaultDimensions = new Dimension(300, 300);
    private String gameTitle=DefaultTitle;
    private Dimension dimensions = DefaultDimensions;
    private boolean showFPS = false;
    private int requiredFPS = DefaultFPS;
    
    private Engine gameEngine;
    private GameContainer container;
    
    private boolean runningAsApplet = false;
    
    public abstract void setup();
    
    public Game(String title, int width, int height){
        this.gameTitle = title;
        this.dimensions = new Dimension(width, height);
    }
    public abstract void addScenes()throws SceneException;

    protected final void newScene(Scene scene) throws SceneException {
        SceneController.get().addScene(scene);
    }


    public final void start()throws SceneException{
        setup();
        if(!runningAsApplet){
            GameFrame.instantiate(gameTitle);
            GameFrame.get().setPreferredSize(dimensions);
            GameFrame.get().setMinimumSize(dimensions);
            GameFrame.get().setMaximumSize(dimensions);
            GameFrame.get().setSize(dimensions);
            GameFrame.get().setResizable(false);
            GameFrame.get().addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) { }
                public void windowClosing(WindowEvent e) {System.exit(0);}
                public void windowClosed(WindowEvent e) {}
                public void windowIconified(WindowEvent e) {}
                public void windowDeiconified(WindowEvent e) {}
                public void windowActivated(WindowEvent e) {}
                public void windowDeactivated(WindowEvent e) {}
            });
            GameFrame.get().setVisible(true);
            container = GameFrame.get();
        }else{
            GameApplet.get().setPreferredSize(dimensions);
            GameApplet.get().setMinimumSize(dimensions);
            GameApplet.get().setMaximumSize(dimensions);
            GameApplet.get().setSize(dimensions);
            GameApplet.get().setVisible(true);
            container = GameApplet.get();
        }
        gameEngine = Engine.instantiate(this);
        gameEngine.setRequiredFPS(requiredFPS);
        if(isShowFPS()) gameEngine.showFPS();
        
        
        addScenes();
        gameEngine.start();
    }
    

    public final String getTitle() {
        return gameTitle;
    }

    protected final void setTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }


    public final Dimension getDimensions() {
        return dimensions;
    }

    protected final void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    public final boolean isShowFPS() {
        return showFPS;
    }


    protected final void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }


    public final int getRequiredFPS() {
        return requiredFPS;
    }


    protected final void setRequiredFPS(int requiredFPS) {
        this.requiredFPS = requiredFPS;
    }



    public final boolean isRuningAsApplet() {
        return runningAsApplet;
    }


    final void setRunningAsApplet(boolean runAsApplet) {
        this.runningAsApplet = runAsApplet;
    }


    public GameContainer getContainer() {
        return container;
    }

    
}

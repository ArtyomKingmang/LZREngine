package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Scene.Scene;
import com.kingmang.LZREngine.Scene.SceneController;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class Renderer extends Component{
    private static Renderer instance;

    public static void instantiate(){
        if(instance!=null) throw new RuntimeException("Instance already exists");
        Renderer.instance = new Renderer();
    }

    public static void instantiate(GraphicsConfiguration gc){
        if(instance!=null) throw new RuntimeException("Instance already exists");

        Renderer.instance = new Renderer(gc);
    }
    public static Renderer get(){
        if(instance==null) throw new RuntimeException("Instance not yet created");
        return Renderer.instance;
    }
    private Renderer(){
        this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        this.graphicsConfiguration = this.graphicsDevice.getDefaultConfiguration();
        
    }
    private Renderer(GraphicsConfiguration gc){
        this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        this.graphicsConfiguration = gc;
    }
    private int width;
    private int height;

    public static class RendererStatics{
        public static class Colour{
            public static Color BACKGROUND = Color.WHITE;
            public static Color FPS_DISPLAY = Color.RED.darker();
        }
       
    }
    public final FontMetrics getFontMetrics(){
        return getGraphics().getFontMetrics();
    }
    @Override
    public final FontMetrics getFontMetrics(Font f){
        return getGraphics().getFontMetrics(f);
    }
    //Graphics environment variables
    private GraphicsEnvironment graphicsEnvironment;
    private GraphicsDevice graphicsDevice;
    private GraphicsConfiguration graphicsConfiguration;

    private Graphics2D doubleBufferGraphics;
    private Image doubleBufferImage;
    private boolean resized;



    private Scene currentScene;


    

    @Override
    public BufferedImage createImage(int width, int height){
        return this.graphicsConfiguration.createCompatibleImage(width, height);
    }
    public BufferedImage createImage(int width, int height, int transparency){
        return this.graphicsConfiguration.createCompatibleImage(width, height, transparency);
    }
    @Override
    public VolatileImage createVolatileImage(int width, int height){
        return this.graphicsConfiguration.createCompatibleVolatileImage(width, height);
    }
    public VolatileImage createVolatileImage(int width, int height, int transparency){
        return this.graphicsConfiguration.createCompatibleVolatileImage(width, height, transparency);
    }


    public final void render(Graphics2D g2){
        buffer();
        try{
            if((g2!=null)&&doubleBufferImage!=null){
                g2.drawImage(doubleBufferImage, 0, 0, null);
            }
            g2.dispose();
        }catch(Exception e){
            Engine.get().exceptionThrown(e);
            Engine.get().fatalError("Graphics context error: "+ e);
        }
    }
    public final void buffer(){
        synchronized(this){
            if(this.resized||doubleBufferImage==null||doubleBufferGraphics==null){
                doubleBufferImage = createImage(getWidth(), getHeight());
                if(doubleBufferImage == null){
                    Engine.get().fatalError("Cannot create image for buffering");
                }else{
                    doubleBufferGraphics = (Graphics2D)doubleBufferImage.getGraphics();
                    doubleBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                }
                if(this.resized) this.resized = false;
            }
        }
        doubleBufferGraphics.setColor(RendererStatics.Colour.BACKGROUND);
        doubleBufferGraphics.fillRect(0, 0, doubleBufferImage.getWidth(null),
                doubleBufferImage.getHeight(null));

        if (SceneController.get().hasCurrentScene()) {
            currentScene = SceneController.get().getCurrentScene();
            currentScene.render(doubleBufferGraphics);
        }

        if(Engine.get().isDisplayFPS()){
            double averageFps = Engine.get().getAverageFPS();
            double requiredFps = Engine.get().getRequredFPS();
            double averageUps = Engine.get().getAverageUPS();
            doubleBufferGraphics.setColor(RendererStatics.Colour.FPS_DISPLAY);
            String val =  (averageFps > 0?roundDouble(averageFps): "---")+" fps";
            val += " / "+roundDouble(requiredFps)+" fps";
            val += " ["+(averageUps > 0?roundDouble(averageUps): "---")+" ups]";

            doubleBufferGraphics.drawString(val ,5,15);
            doubleBufferGraphics.setColor(RendererStatics.Colour.BACKGROUND);
        }

    }

    private String roundDouble(double d){
        return ""+Math.round(d*100)/100;
    }

    public void resized(){
        this.resized = true;
    }




}

package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Game.Game;
import com.kingmang.LZREngine.Scene.SceneController;
import com.kingmang.LZREngine.Exeptions.SceneException;


public class Engine implements Runnable{

    private Game game;
    
    private static Engine instance;
    public static <T extends Engine> Engine instantiate(Game game){
        if(Engine.instance!=null) throw new RuntimeException("Instance already exists");
        Engine.instance = new Engine();
        Engine.instance.game = game;
//        engine.initialise();
        return Engine.instance;
    }
    public static Engine get(){
        if(Engine.instance==null) throw new RuntimeException("Instance not yet created");
        return Engine.instance;
    }

    
    private Thread thread;

    private boolean running;

    private boolean showFPS = false;
    private float requiredFPS = 25;
    private float actualFPS = -1;
    private float averageFPS = -1;
    private float averageUPS = -1;
    private long periodFPS = 40000000;
    private int frameSkips;
    private float[] storeFPS = new float[EngineStatics.FPS.STORENUM];
    private float[] storeUPS = new float[EngineStatics.FPS.STORENUM];

    public final void start(){
        thread = new Thread(this);
        thread.start();
    }
    public final void sleep(long time){
        try{Thread.sleep(time);}catch(InterruptedException ex){}
    }

    public final void run() {
        this.running = true;
        long start, end, timediff, sleepTime;
        long oversleepTime = 0l;
        int noDelays = 0;
        long excess = 0l;
        start = System.nanoTime();
        game.getContainer().requestFocus();
        while(running){

            update();

            game.getContainer().repaint();


            end = System.nanoTime();
            timediff = end - start;
            sleepTime = this.periodFPS - timediff - oversleepTime;
            if(sleepTime>0){
                sleep(sleepTime/EngineStatics.FPS.NS_TO_MS);
                oversleepTime = System.nanoTime() - end - sleepTime;
            }else{
                excess -= 0l;
                oversleepTime = 0l;
                if(++noDelays >= EngineStatics.FPS.FRAMES_WITHOUT_SLEEP_OR_YEILD){
                    Thread.yield();
                    noDelays = 0;
                }
            }
            this.frameSkips = 0;
            while((excess>periodFPS) && (this.frameSkips < EngineStatics.FPS.MAX_FRAME_SKIPS)){
                excess -= periodFPS;
                update();
                this.frameSkips++;
            }
            timediff = System.nanoTime() - start;
            this.actualFPS = EngineStatics.FPS.MS_TO_S/(float)(timediff/EngineStatics.FPS.NS_TO_MS);
            addFPS(this.actualFPS);
            addUPS(getUPS());
            start = System.nanoTime();
        }
    }
    public final void stop(){
        this.running = false;
        System.exit(0);
    }
    public final void update() {
        if (this.running == true) {
            if (SceneController.get().hasCurrentScene()) {
                try {
                    SceneController.get().getCurrentScene().update(getAverageUPS());
                } catch (SceneException ex) {
                    exceptionThrown(ex);
                }
            }
        }
    }
    public void exceptionThrown(Exception ex){
        Engine.out("Exception: "+ex.getMessage());
        ex.printStackTrace();
    }

    public void fatalError(String msg){
        Engine.out("Fatal: "+msg);
        System.exit(1);
    }
    public static void out(String msg){
        System.out.println(msg);
    }

    public boolean isDisplayFPS(){
        return this.showFPS;
    }
    private void addFPS(float FPS){

        float[] newStore = new float[EngineStatics.FPS.STORENUM];
        float avg = 0;
        for(int i = 0; i < EngineStatics.FPS.STORENUM; i++){
            if(i+1==EngineStatics.FPS.STORENUM){
                newStore[i] = FPS;
                avg += FPS;
            }else{
                newStore[i] = storeFPS[i+1];
                avg += storeFPS[i+1];
            }
        }
        avg = avg/EngineStatics.FPS.STORENUM;
        this.averageFPS = avg;
        storeFPS = newStore;
    }
    private void addUPS(float UPS){
        float[] newStore = new float[EngineStatics.FPS.STORENUM];
        float avg = 0;
        for(int i = 0; i < EngineStatics.FPS.STORENUM; i++){
            if(i+1==EngineStatics.FPS.STORENUM){
                newStore[i] = UPS;
                avg += UPS;
            }else{
                newStore[i] = storeUPS[i+1];
                avg += storeUPS[i+1];
            }
        }
        avg = avg/EngineStatics.FPS.STORENUM;
        this.averageUPS = avg;
        storeUPS = newStore;
    }

    public void showFPS(){
        this.showFPS = true;
    }
    public void hideFPS(){
        this.showFPS = false;
    }
    public void setRequiredFPS(float requiredFPS){
        this.requiredFPS = requiredFPS;
        this.periodFPS = (long)(EngineStatics.FPS.NS_TO_S/requiredFPS);
    }
    public float getFPS(){
        return this.actualFPS;
    }
    public float getUPS(){
        return this.actualFPS*(this.frameSkips+1);
    }
    public float getRequredFPS(){
        return this.requiredFPS;
    }
    public float getAverageFPS(){
        return this.averageFPS;
    }
    public float getAverageUPS(){
        return this.averageUPS;
    }

    public static class EngineStatics{
        public static class FPS{
            public final static int MS_TO_S = 1000;
            public final static int NS_TO_S = 1000000000;
            public final static int NS_TO_MS= 1000000;
            public static int FRAMES_WITHOUT_SLEEP_OR_YEILD = 16;
            public static int MAX_FRAME_SKIPS = 5;
            public final static int STORENUM = 10;
        }
    }

}

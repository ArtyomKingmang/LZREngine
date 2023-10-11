package com.kingmang.LZREngine.Scene;

import com.kingmang.LZREngine.Exeptions.SceneException;
import com.kingmang.LZREngine.Game.GameFrame;

import java.util.HashMap;

public class SceneController {
    private static SceneController instance;

    private String currentSceneKey;

    private HashMap<String, Scene> scenes;

    private SceneController(){
        scenes = new HashMap<String, Scene>();
    }

    public static SceneController get(){
        if(instance == null) instance = new SceneController();
        return instance;
    }

    public void addScene(Scene scene) throws SceneException {
        if(hasSceneKey(scene.getKey())) throw new SceneException("Scene with key '"+scene.getKey()+"' already exists");
        try {
            scene.initialise();
        } catch (SceneException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new SceneException(ex);
        } catch (Exception ex) {
            throw new SceneException(ex);
        }
        synchronized(this){
            scenes.put(scene.getKey(), scene);
            if(!hasCurrentScene())currentSceneKey = scene.getKey();
        }
    }
    public void removeScene(Scene scene) throws SceneException{
        String key = scene.getKey();
        if(key!=null){
            removeScene(key);
        }
    }
    public void removeScene(String key) throws SceneException{
        if(!hasSceneKey(key)) throw new SceneException("Scene with key '"+key+"' does not exist");
        if(key.equals(currentSceneKey)) throw new SceneException("Cannot remove scene, it is the current scene");
        synchronized(this){
            scenes.remove(key);
        }
    }

    public synchronized boolean hasSceneKey(String key){
        return (this.scenes.containsKey(key));
    }


  


    public Scene[] getScenesArray(){
        synchronized(this){
            return this.scenes.values().toArray(new Scene[this.scenes.size()]);
        }
    }

    public Scene getScene(String key){
        if(this.scenes.containsKey(key)){
            return this.scenes.get(key);
        }else{
            return null;
        }
    }

    public synchronized void setCurrentScene(String key) throws SceneException{
        if(!hasSceneKey(key)) throw new SceneException("Scene with key '"+key+"' does not exist");
        Scene prev = null;
        if(hasCurrentScene()){
            prev = getCurrentScene();
        }
        Scene next = getScene(key);
        next.onPreEnterScene(prev);
        this.currentSceneKey = key;
        next.onEnterScene(prev);
        GameFrame.get().repaint();
        if(prev!=null)prev.onLeaveScene(next);
    }


    public boolean hasCurrentScene(){
        return(hasSceneKey(currentSceneKey));
    }
     public boolean isSceneCurrent(String key){
        return key.equals(currentSceneKey);
    }
    public Scene getCurrentScene(){
        return getScene(currentSceneKey);
    }

}

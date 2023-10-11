package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Enity.*;
import com.kingmang.LZREngine.Exeptions.EntityException;
import com.kingmang.LZREngine.Exeptions.SceneException;

import java.awt.Graphics2D;

public abstract class State {

    private EntityMap entities = new EntityMap();

    private StateEntityScene owner;
    private String key;
    public State(String key){
        this.key = key;
    }

    public abstract void initialise() throws SceneException;
    public abstract void onGameLoad() throws SceneException;
    public abstract void onStatePreLoad() throws SceneException;
    public abstract void onStateLoad() throws SceneException;
    public abstract void onStatePreLeave() throws SceneException;
    public abstract void onStateLeave() throws SceneException;

    public synchronized void addEntity(Entity entity) throws EntityException {
        entity.setParentState(this);
        entities.add(entity);
    }
    public synchronized void removeEntity(Entity entity) throws EntityException{
        entity.setParentState(null);
        entities.remove(entity);
    }
    public synchronized void removeEntity(String entitykey) throws EntityException{
        getEntity(entitykey).setParentState(null);
        entities.remove(entitykey);
    }
    public synchronized void clearEntities(){
        for(Entity e : entities.getAll()) e.setParentState(null);
        entities.clear();
    }
    public Entity getEntity(String entityId){
        return entities.get(entityId);
    }


    public void render(Graphics2D g){
       synchronized(this){
           for(Entity entity : entities.getForRender()){
               entity.render(g);
           }
       }
    }
    public void update(Input input, float updatesPerSecond) throws SceneException{
        synchronized(this){
           for(Entity entity :entities.getForUpdate()){
                try {
                    entity.update(input, updatesPerSecond);
                } catch (EntityException ex) {
                    throw new SceneException(ex);
                }
           }
       }
    }

    


    public StateEntityScene getOwner() {
        return owner;
    }

    public void setOwner(StateEntityScene owner) {
        this.owner = owner;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

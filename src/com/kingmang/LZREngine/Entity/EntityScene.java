package com.kingmang.LZREngine.Entity;

import com.kingmang.LZREngine.Engine.SceneException;
import com.kingmang.LZREngine.Scene.Scene;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public abstract class EntityScene extends Scene {

    public EntityScene(String key){
        super(key);
    }
    
    EntityMap entities = new EntityMap();

    Input input = new Input();

    public void addEntity(Entity entity) throws EntityException {
        entities.add(entity);
        entity.setParentScene(this);
    }

    public void clearEntities(){
        for(Entity e : entities.getAll()) e.setParentScene(null);
        entities.clear();

    }
    public Entity getEntity(String entityId){
        return entities.get(entityId);
    }

    public void removeEntity(String entityId){
        getEntity(entityId).setParentScene(null);
        entities.remove(entityId);
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
        entity.setParentScene(null);
    }

    @Override
    public void render(Graphics2D g){
       synchronized(this){
           for(Entity entity : entities.getForRender()){
               entity.render(g);
           }
       }
    }
    @Override
    public void update(float updatesPerSecond) throws SceneException {
        synchronized(this){
           sceneUpdate(input, updatesPerSecond);
           for(Entity entity :entities.getForUpdate()){
                try {
                    entity.update(input, updatesPerSecond);
                } catch (EntityException ex) {
                    throw new SceneException(ex);
                }
           }
           input.clear();
       }
    }

    public void sceneUpdate(Input input, float updatesPerSecond) throws SceneException{

    }

    @Override
    public final void keyTyped(KeyEvent e) {
        input.keyTyped(e);
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        input.keyPressed(e);
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        input.keyReleased(e);
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        input.mouseDragged(e);
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        input.mouseMoved(e);
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        input.mouseClicked(e);
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        input.mousePressed(e);
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        input.mouseReleased(e);
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        input.mouseEntered(e);
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        input.mouseExited(e);
    }

    @Override
    public final void mouseWheelMoved(MouseWheelEvent e) {
        input.mouseWheelMoved(e);
    }

}

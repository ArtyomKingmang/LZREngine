package com.kingmang.LZREngine.Enity;

import com.kingmang.LZREngine.Exeptions.EntityException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class EntityMap {
    private static class RenderComparator implements Comparator<Entity>{
        public int compare(Entity o1, Entity o2) {
            return o1.compareRenderOrder(o2);
        }
    }
    private static class UpdateComparator implements Comparator<Entity>{
        public int compare(Entity o1, Entity o2) {
            return o1.compareUpdateOrder(o2);
        }
    }
    private final static RenderComparator renderComparator = new RenderComparator();
    private final static UpdateComparator updateComparator = new UpdateComparator();
    private final HashMap<String, Entity> entities = new HashMap<String, Entity>();
    private ArrayList<Entity> renderOrderedEntities = new ArrayList<Entity>();
    private ArrayList<Entity> updateOrderedEntities = new ArrayList<Entity>();



    public synchronized void add(Entity entity) throws EntityException {
        if(entities.containsKey(entity.getId()))
            throw new EntityException("Entity '"+entity.getId()+"' already exists.");

        entities.put(entity.getId(), entity);
        renderOrderedEntities = new ArrayList<Entity>();
        renderOrderedEntities.addAll(entities.values());
        Collections.sort(renderOrderedEntities,renderComparator);
        updateOrderedEntities = new ArrayList<Entity>();
        updateOrderedEntities.addAll(entities.values());
        Collections.sort(updateOrderedEntities,updateComparator);
    }

    public synchronized void clear(){
        entities.clear();
        renderOrderedEntities = new ArrayList<Entity>();
        updateOrderedEntities = new ArrayList<Entity>();
    }
    public Entity get(String entityId){
        return entities.get(entityId);
    }
    public synchronized void remove(String entityId){
        Entity e = entities.remove(entityId);
        updateOrderedEntities.remove(e);
        renderOrderedEntities.remove(e);
        Collections.sort(renderOrderedEntities,renderComparator);
        Collections.sort(updateOrderedEntities,updateComparator);
    }
    public synchronized void remove(Entity entity){
        remove(entity.getId());

    }
    public synchronized Entity[] getForRender(){
        return renderOrderedEntities.toArray(new Entity[renderOrderedEntities.size()]);
    }
    public synchronized Entity[] getAll(){
        return entities.values().toArray(new Entity[entities.values().size()]);
    }
    public synchronized Entity[] getForUpdate(){
        return updateOrderedEntities.toArray(new Entity[updateOrderedEntities.size()]);
    }
}

package com.kingmang.LZREngine.Enity;

import com.kingmang.LZREngine.Engine.*;
import com.kingmang.LZREngine.Exeptions.EntityException;
import com.kingmang.LZREngine.Geometry.Vector2f;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Entity{

    private String id;

    private Vector2f position;
    private float height;
    private float width;
    private float scale;

    private boolean awake;
    private boolean visible;

    private State parentState;
    private EntityScene parentScene;
    private boolean updating;


    private ArrayList<String> flaggedForRemoval;
    private ArrayList<Component> flaggedForAddition;

    public Entity(String id){
        flaggedForRemoval = new ArrayList<String>();
        flaggedForAddition = new ArrayList<Component>();
        this.id = id;
        this.awake = false;
        this.visible = false;
        this.position = new Vector2f(0f, 0f);
        this.height = 0;
        this.width = 0;
        this.scale = 1;
    }
    private final HashMap<String, Component> components = new HashMap<String, Component>();
    private RenderComponent renderComponent;
    private float renderOrdering = 0;
    private float updateOrdering = 0;

    public synchronized void addComponent(Component component) throws EntityException {
        if(updating){
            flaggedForAddition.add(component);
        }else{
            if(components.containsKey(component.getId()))
                throw new EntityException("Component '"+component.getId()+"' already exists.");
            if(component.isRender()){
                renderComponent = (RenderComponent) component;
            }
            components.put(component.getId(), component);
            component.setOwner(this);
        }
    }
    public synchronized boolean hasComponent(String id){
        return components.containsKey(id);
    }
    public synchronized void clearComponents(){
        components.clear();
    }

    public synchronized void removeComponent(String componentId){
        if(updating){
            flaggedForRemoval.add(componentId);
        }else{
            Component prev = components.remove(componentId);
            if(prev!=null){
                prev.setOwner(null);
                if(prev.isRender()) renderComponent = null;
            }
        }
        
    }
    private synchronized void peformComponentRemoval(){
        for(String componentId : flaggedForRemoval){
            Component prev = components.remove(componentId);
            if(prev!=null){
                prev.setOwner(null);
                if(prev.isRender()) renderComponent = null;
            }
        }
        flaggedForRemoval.clear();
    }
    private synchronized void performComponentAddition() throws EntityException{
        for(Component component : flaggedForAddition){
            if(components.containsKey(component.getId()))
                throw new EntityException("Component '"+component.getId()+"' already exists.");
            if(component.isRender()){
                renderComponent = (RenderComponent) component;
            }
            components.put(component.getId(), component);
            component.setOwner(this);
        }
        flaggedForAddition.clear();

    }
    public synchronized void replaceComponent(Component component) throws EntityException{
        removeComponent(component.getId());
        addComponent(component);
    }
    public Component getComponent(String componentId){
        return components.get(componentId);
    }
    public final String getId(){
        return id;
    }


    public int compareRenderOrder(Entity o) {
        return (int) Math.signum(getRenderOrdering()-o.getRenderOrdering());
    }
    public int compareUpdateOrder(Entity o) {
        return (int) Math.signum(getUpdateOrdering()-o.getUpdateOrdering());
    }
    public void render(Graphics2D g){
        if(!isVisible()) return;
        if(renderComponent!=null) renderComponent.render(g);
    }
    public void update(Input input, float updatesPerSecond) throws EntityException{
        if(!isAwake()) return;
        updating = true;
        synchronized(components){
            Collection<Component> comps = components.values();
            for(Component c : comps){
                c.update(input, updatesPerSecond);
            }
        }
        updating = false;
        peformComponentRemoval();
        performComponentAddition();
    }


    public boolean isAwake() {
        return awake;
    }


    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getRenderOrdering() {
        return renderOrdering;
    }

    public void setRenderOrdering(float renderOrdering) {
        this.renderOrdering = renderOrdering;
    }

    public float getUpdateOrdering() {
        return updateOrdering;
    }

    public void setUpdateOrdering(float updateOrdering) {
        this.updateOrdering = updateOrdering;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2f(x, y));
    }
    public void setPosition(Vector2f position) {
        this.position = position;
    }
    public void move(Vector2f moveBy) {
        this.position.add(moveBy);
    }

    public float getHeight() {
        return height;
    }
    public float getDrawHeight() {
        return height*scale;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public float getWidth() {
        return width;
    }
    public float getDrawWidth() {
        return width*scale;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }



    public void center() {
        float w = getDrawWidth();
        float h = getDrawHeight();
        setPosition(new Vector2f(
            Renderer.get().getWidth()/2f-w/2f,
            Renderer.get().getHeight()/2f-h/2f
            ));
    }


    public State getParentState() {
        return parentState;
    }


    public void setParentState(State parentState) {
        this.parentState = parentState;
    }


    public EntityScene getParentScene() {
        return parentScene;
    }

    public void setParentScene(EntityScene parentScene) {
        this.parentScene = parentScene;
    }

}

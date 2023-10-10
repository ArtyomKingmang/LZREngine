
package com.kingmang.LZREngine.GameObject;

import com.kingmang.LZREngine.Cheker.check;
import com.kingmang.LZREngine.Geometry.IsotheticBoundingBox2D;
import com.kingmang.LZREngine.Geometry.Polygon2D;
import com.kingmang.LZREngine.Geometry.Vector2f;


public abstract class GameObject {

    private Vector2f position = new Vector2f(0, 0);
    private Vector2f velocity = new Vector2f(0, 0);
    private Vector2f drag = new Vector2f(0, 0);
    private Polygon2D polygon = new Polygon2D();


    public final Vector2f getPosition() {
        return position;
    }

    public final double getX() {
        return this.position.getX();
    }
    public final double getY() {
        return this.position.getY();
    }


    public final void setPosition(Vector2f position) {
        this.position = position;
        this.polygon.setTopLeft(position);
    }
    public final void setPosition(float x, float y) {
        this.position.set(x, y);
        this.polygon.setTopLeft(position);
    }
    public final void moveBy(Vector2f vector) {
        this.position.add(vector);
        this.polygon.setTopLeft(position);
    }
    public final void moveBy(float x, float y) {
        this.position.add(x, y);
        this.polygon.setTopLeft(position);
    }

    public final Polygon2D getPolygon() {
        return polygon;
    }

    public final Vector2f[] getVertices() {
        return polygon.toGlobalArray();
    }

    public final void setPolygon(Polygon2D shape) {
        this.polygon = shape;
    }
    public final void addVertex(Vector2f vertex) {
        this.polygon.addVertex(vertex);
    }
    public final void addVertex(float x, float y) {
        this.polygon.addVertex(new Vector2f(x, y));
    }
    public final void closePolygon() {
        this.polygon.close();
    }

    public final IsotheticBoundingBox2D getBoundingBox(){
        return this.polygon.getIsotheticBoundingBox();
    }

    public boolean isWithin(Polygon2D polygon){
        return isWithin(this.getPosition(), polygon);
    }
    public boolean isWithin(Vector2f location, Polygon2D polygon){
        Polygon2D myPolygon = this.polygon.getPolygonFrom(location);

        if(!polygon.getIsotheticBoundingBox().
                containsExcl(myPolygon.getIsotheticBoundingBox())){
            return false;
        }


        Vector2f[] vertices = myPolygon.toGlobalArray();
        boolean vertexOutside = false;
        for(int i=0; i<vertices.length && !vertexOutside; i++){
            vertexOutside = !check.pointInPolygon(polygon, vertices[i]);
        }
        return (!vertexOutside);
    }
    public boolean collidesWith(Vector2f myNextLocation, GameObject other){
        Polygon2D nextPolygon = this.polygon.getPolygonFrom(myNextLocation);
        //check rectangles first to save time
        if(!nextPolygon.getIsotheticBoundingBox().collidesIncl(other.getBoundingBox())) return false;
        Vector2f[] vertices = nextPolygon.toGlobalArray();
        boolean collisionFound = false;
        for(int i=0; i<vertices.length && !collisionFound; i++){
            collisionFound = check.pointInPolygon(other.getPolygon(), vertices[i]);
        }
        if(collisionFound) return true;
        vertices = other.getPolygon().toGlobalArray();
        for(int i=0; i<vertices.length && !collisionFound; i++){
            collisionFound = check.pointInPolygon(this.getPolygon(), vertices[i]);
        }
        return collisionFound;
    }


    public final Vector2f getVelocity() {
        return velocity;
    }


    public final void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
    public final void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }
    public final void addVelocity(Vector2f velocity) {
        this.velocity.add(velocity);
    }
    public final void addVelocity(float x, float y) {
        this.velocity.add(x, y);
    }

    public final Vector2f getDrag() {
        return drag;
    }

    public final void setDrag(Vector2f drag) {
        this.drag = drag;
    }
    public final void setDrag(float x, float y) {
        this.drag.set(x, y);
    }

}

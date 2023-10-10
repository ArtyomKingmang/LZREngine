
package com.kingmang.LZREngine.Geometry;

public class IsotheticBoundingBox2D {
    private Vector2f topLeft;
    private Vector2f topRight;
    private Vector2f bottomRight;
    private Vector2f bottomLeft;
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public IsotheticBoundingBox2D(Vector2f topLeft, Vector2f bottomRight){
        this(topLeft.getX(), bottomRight.getY(), bottomRight.getX(), topLeft.getY());
    }
    public IsotheticBoundingBox2D(float minX, float minY, float maxX, float maxY){
        this.topLeft = new Vector2f(minX, maxY);
        this.topRight = new Vector2f(maxX, maxY);
        this.bottomLeft = new Vector2f(minX, minY);
        this.bottomRight = new Vector2f(maxX, minY);
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    public boolean containsPointIncl(Vector2f point){
        return (this.minX <= point.getX() && point.getX() <= this.maxX )&&
                (this.minY <= point.getY() && point.getY() <= this.maxY );
    }
    public boolean containsPointExcl(Vector2f point){
        return (this.minX < point.getX() && point.getX() < this.maxX )&&
                (this.minY < point.getY() && point.getY() < this.maxY );
    }
    public boolean collidesIncl(IsotheticBoundingBox2D other){
        if(this.containsPointIncl(other.getTopLeft())) return true;
        if(this.containsPointIncl(other.getTopRight())) return true;
        if(this.containsPointIncl(other.getBottomRight())) return true;
        if(this.containsPointIncl(other.getBottomLeft())) return true;
        return false;
    }
    public boolean collidesExcl(IsotheticBoundingBox2D other){
        if(this.containsPointExcl(other.getTopLeft())) return true;
        if(this.containsPointExcl(other.getTopRight())) return true;
        if(this.containsPointExcl(other.getBottomRight())) return true;
        if(this.containsPointExcl(other.getBottomLeft())) return true;
        return false;
    }
    public boolean containsIncl(IsotheticBoundingBox2D other){
        return (this.containsPointIncl(other.getTopLeft())&&
                this.containsPointIncl(other.getTopRight())&&
                this.containsPointIncl(other.getBottomRight()) &&
                this.containsPointIncl(other.getBottomLeft()));
    }
    public boolean containsExcl(IsotheticBoundingBox2D other){
        return (this.containsPointExcl(other.getTopLeft())&&
                this.containsPointExcl(other.getTopRight())&&
                this.containsPointExcl(other.getBottomRight()) &&
                this.containsPointExcl(other.getBottomLeft()));
    }

    public Vector2f getTopLeft() {
        return topLeft;
    }

    public Vector2f getTopRight() {
        return topRight;
    }

    public Vector2f getBottomRight() {
        return bottomRight;
    }

    public Vector2f getBottomLeft() {
        return bottomLeft;
    }
}

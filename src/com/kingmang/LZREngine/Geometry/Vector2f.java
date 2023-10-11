package com.kingmang.LZREngine.Geometry;

public class Vector2f implements Cloneable{
    public float x;
    public float y;
    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }
     public Vector2f(java.awt.Point p){
        this.x = (float)p.getX();
        this.y = (float)p.getY();
    }
    public void add(Vector2f vector){
        this.x += vector.getX();
        this.y +=  vector.getY();
    }
    public void add(float x, float y){
        this.x += x;
        this.y += y;
    }
    public void addX(float x){
        this.x += x;
    }
    public void addY(float y){
        this.y += y;
    }
    public void subtract(Vector2f vector){
        this.x -= vector.getX();
        this.y -=  vector.getY();
    }
    public void subtract(float x, float y){
        this.x -= x;
        this.y -= y;
    }
    public void subtractX(float x){
        this.x -= x;
    }
    public void subtractY(float y){
        this.y -= y;
    }

    public void multiply(Vector2f vector){
        this.x *= vector.getX();
        this.y *=  vector.getY();
    }
    public void multiply(float x, float y){
        this.x *= x;
        this.y *= y;
    }
    public void multiplyX(float x){
        this.x *= x;
    }
    public void multiplyY(float y){
        this.y *= y;
    }
    public Vector2f proportion(Vector2f proportion){
        float xp = (proportion.getX()>1?1:proportion.getX());
        xp = (xp<0?0:xp);
        float yp = (proportion.getY()>1?1:proportion.getY());
        yp = (yp<0?0:yp);
        return new Vector2f(
            (this.x - this.x*xp),
            (this.y - this.y*yp)
        );
    }

    public void flipX(){
        this.x = -this.x;
    }
    public void flipY(){
        this.y = -this.y;
    }

    public float dotProduct(Vector2f other){
        return (this.getX()*other.getX())+(this.getY()*other.getY());
    }

    public Vector2f getEdge(Vector2f other){
        return new Vector2f(this.getX()-other.getX(), this.getY()-other.getY());
    }
    public Vector2f getPerpendicular(){
        return new Vector2f(-this.getY(), this.getX());
    }
 

    public float angle(Vector2f other){
        return (float)Math.acos(
                this.dotProduct(other)/
                (this.magnitude()*other.magnitude())
                );
    }

    public Vector2f unitVector(){
        float magnitude = this.magnitude();
        return new Vector2f(x/magnitude, y/magnitude);
    }

    public float distanceSq(Vector2f other){
        return (float)(Math.pow(other.getX()-this.getX(),2)+Math.pow(other.getY()-this.getY(),2));
    }
    public float distance(Vector2f other){
        return (float)Math.sqrt(this.distanceSq(other));
    }
    public float magnitudeSq(){
        return (float)(Math.pow(this.getX(),2)+Math.pow(this.getY(),2));
    }
    public float magnitude(){
        return (float)Math.sqrt(this.magnitudeSq());
    }

    public float getX() {
        return x;
    }
    public int getXi() {
        return (int)Math.round(x);
    }

    public void setX(float x) {
        this.x = x;
    }


    public float getY() {
        return y;
    }
    public int getYi() {
        return (int)Math.round(y);
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "("+this.getX()+", "+this.getY()+")";
    }
    public String toString(int decimalPlaces){
        float x = Math.round(this.getX()*(10*decimalPlaces))/(10*decimalPlaces);
        float y = Math.round(this.getY()*(10*decimalPlaces))/(10*decimalPlaces);
        return "("+x+", "+y+")";
    }


    @Override
    public boolean equals(Object other){
        if(other==this)return true;
        if(!(other instanceof Vector2f))return false;
        Vector2f vo = (Vector2f) other;
        return (this.getX()==vo.getX()&&this.getY()==vo.getY());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (int) (Float.floatToIntBits(this.x) ^ (Float.floatToIntBits(this.x) >>> 31));
        hash = 19 * hash + (int) (Float.floatToIntBits(this.y) ^ (Float.floatToIntBits(this.y) >>> 31));
        return hash;
    }

    @Override
    public Vector2f clone(){
        return new Vector2f(x, y);
    }

}

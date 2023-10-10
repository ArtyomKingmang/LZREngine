
package com.kingmang.LZREngine.Geometry;

public class LineSegment2f {
    private Vector2f start;
    private Vector2f end;
    public LineSegment2f(Vector2f start, Vector2f end){
        this.start = start;
        this.end = end;
    }

    public boolean isHorizontal(){
        return (this.start.getY()==this.end.getY());
    }
    public boolean isVertical(){
        return (this.start.getX()==this.end.getX());
    }

    public double getSlope(){
        if(isVertical()) return Double.POSITIVE_INFINITY;
        if(isHorizontal()) return 0;
        return (this.end.getY()-this.start.getY())/
                (this.end.getX()-this.start.getX());
    }
    public double getYIntercept(){
        if(isVertical()) throw new IllegalArgumentException("Line is vertical, does not cross the y axis");
        if(isHorizontal()) return this.getStart().getY();
        return this.start.getY()-(this.getSlope()*this.start.getX());
    }

    public double getXIntercept(){
        if(isHorizontal()) throw new IllegalArgumentException("Line is horizontal, does not cross the x axis");
        if(isVertical()) return this.getStart().getX();
        return (-getYIntercept()/this.getSlope());
    }


    public Vector2f intersect(LineSegment2f other){
        float x1 = start.x;
        float y1 = start.y;
        float x2 = end.x;
        float y2 = end.y;
        float x3 = other.start.x;
        float y3 = other.start.y;
        float x4 = other.end.x;
        float y4 = other.end.y;
        float x = ((x1*y2-y1*x2)*(x3-x4) - (x1-x2)*(x3*y4 - y3*x4))/
                ((x1-x2)*(y3-y4) - (y1 - y2)*(x3 - x4));
        float y = ((x1*y2-y1*x2)*(y3-y4) - (y1 - y2)*(x3*y4 - y3*x4))/
                ((x1 - x2)*(y3-y4) - (y1-y2)*(x3-x4));
        Vector2f intersect = new Vector2f(x, y);
        if(distanceFromPoint(intersect)<=0.00001){
            //close enough
            return intersect;
        }
        return null;
    }
    public float distanceFromPoint(Vector2f point){
        //Get projection of point onto line
        //(tangent of line that collides with point)
        //http://paulbourke.net/geometry/pointline/
        float x1 = start.x;
        float y1 = start.y;
        float x2 = end.x;
        float y2 = end.y;
        float x3 = point.x;
        float y3 = point.y;
        float u = ((x3-x1)*(x2-x1) + (y3-y1)*(y2-y1))/lengthSq();
        if (u < 0.0) return point.distance(start);       // Beyond the start  of the segment
        else if (u > 1.0) return point.distance(end);  // Beyond the end of the segment
        Vector2f projection =  new Vector2f(
                x1 + u*(x2-x1),
                y1 + u*(y2-y1));
        return point.distance(projection);
    }

    public float lengthSq(){
        return start.distanceSq(end);
    }

    public boolean isPointRightOfLine(Vector2f point){
       return (pointInRelationToLine(point)<0);
    }
    public boolean isPointLeftOfLine(Vector2f point){
       return (pointInRelationToLine(point)>0);
    }
    public boolean isPointOnLine(Vector2f point){
       return (pointInRelationToLine(point)==0);
    }


    public int pointInRelationToLine(Vector2f point){

        double x1 = this.start.getX();
        double y1 = this.start.getY();
        double x2 = this.end.getX();
        double y2 = this.end.getY();
        double x3 = point.getX();
        double y3 = point.getY();

        double val = ((x2 - x1)*(y3 - y1)) - ((y2 - y1)*(x3 - x1));


        return (int)Math.signum(val);
    }




    public Vector2f getStart() {
        return start;
    }

    public void setStart(Vector2f start) {
        this.start = start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public void setEnd(Vector2f end) {
        this.end = end;
    }

    public void set(Vector2f start, Vector2f end) {
        this.start = start;
        this.end = end;
    }

}

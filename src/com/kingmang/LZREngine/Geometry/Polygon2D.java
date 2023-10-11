package com.kingmang.LZREngine.Geometry;

import java.awt.geom.GeneralPath;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;


public class Polygon2D {
    private boolean closed = false;
    private TreeMap<Integer, Vector2f> vertices;
    private TreeSet<Vector2f> verticesOrderedByXY;
    private TreeSet<Vector2f> verticesOrderedByYX;
    private Vector2f topLeft;
    private int vertexCount;
    public Polygon2D(){
        topLeft = new Vector2f(0, 0);
        vertices = new TreeMap<Integer, Vector2f>(new Comparator<Integer>(){
            public int compare(Integer o1, Integer o2) {
                return o1.intValue()-o2.intValue();
            }
        });
        verticesOrderedByXY = new TreeSet(new Comparator<Vector2f>(){
            public int compare(Vector2f o1, Vector2f o2) {
                double diff = o1.getX()-o2.getX();
                if(diff==0d){
                    diff = o1.getY()-o2.getY();
                    if(diff<0) return -1;
                    if(diff>0) return 1;
                    return 0;
                }
                if(diff<0) return -1;
                return 1;
            }
        });
        verticesOrderedByYX = new TreeSet(new Comparator<Vector2f>(){
            public int compare(Vector2f o1, Vector2f o2) {
                double diff = o1.getY()-o2.getY();
                if(diff==0d){
                    diff = o1.getX()-o2.getX();
                    if(diff<0) return -1;
                    if(diff>0) return 1;
                    return 0;
                }
                if(diff<0) return -1;
                return 1;
            }
        });
        vertexCount = -1;
    }
    public int vertices(){
        return vertexCount+1;
    }
    public synchronized void addVertex(float x, float y){
        this.addVertex(new Vector2f(x, y));
    }
    public synchronized void addVertex(Vector2f vertex){
        if(closed) return;
        if(vertices.firstEntry()!=null&&vertex.equals(vertices.firstEntry().getValue())){
            this.closed = true;
        }else{
            vertexCount++;
            vertices.put(vertexCount, vertex);
            verticesOrderedByXY.add(vertex);
            verticesOrderedByYX.add(vertex);
        }
    }
    public void close(){
        this.closed = true;
    }

    public IsotheticBoundingBox2D getIsotheticBoundingBox(){
        return new IsotheticBoundingBox2D(
        verticesOrderedByXY.first().getX()+topLeft.getX(), verticesOrderedByYX.first().getY()+topLeft.getY(),
        verticesOrderedByXY.last().getX()+topLeft.getX(), verticesOrderedByYX.last().getY()+topLeft.getY());
    }

    public Iterator<Vector2f> iterator(){
        return vertices.values().iterator();
    }

    public synchronized Vector2f[] toLocalArray(){
        return vertices.values().toArray(new Vector2f[vertices.size()]);
    }

    public synchronized Vector2f[] toGlobalArray(){
        Vector2f[] globals = new Vector2f[vertices.size()];
        Vector2f clone;
        int i = 0;
        for(Vector2f v : vertices.values()){
            clone = v.clone();
            clone.add(topLeft);
            globals[i] = clone;
            i++;
        }
        return globals;
    }

    public GeneralPath getPath(){
        GeneralPath path = new GeneralPath();
        Vector2f[] arr = toGlobalArray();
        Vector2f globalPosition = arr[0];
        path.moveTo(globalPosition.getXi(), globalPosition.getYi());
        for(int i=1; i<arr.length; i++){
            globalPosition = arr[i];
            path.lineTo(globalPosition.getXi(), globalPosition.getYi());
        }
        path.closePath();
        return path;
    }


    @Override
    public Polygon2D clone() {
        Polygon2D clone = new Polygon2D();
        Vector2f[] verticesArr = toLocalArray();
        for(Vector2f v : verticesArr){
            clone.addVertex(v);
        }
        clone.close();
        return clone;
    }

    public Polygon2D getPolygonFrom(Vector2f topLeft) {
        Polygon2D clone = this.clone();
        clone.setTopLeft(topLeft);
        return clone;
    }

    public Vector2f getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Vector2f topLeft) {
        this.topLeft = topLeft;
    }
}

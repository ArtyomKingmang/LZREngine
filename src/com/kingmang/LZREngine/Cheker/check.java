package com.kingmang.LZREngine.Cheker;

import com.kingmang.LZREngine.Geometry.LineSegment2f;
import com.kingmang.LZREngine.Geometry.Polygon2D;
import com.kingmang.LZREngine.Geometry.Vector2f;

public class check {

    public static boolean pointInPolygon(Polygon2D polygon, Vector2f point){
        Vector2f[] vertices = polygon.toGlobalArray();
        if(vertices.length==0) return false;
        int total = 0;
        Vector2f currentVertex = vertices[0];
        int vertexQuadrant = whichQuadrant(point, currentVertex);

        Vector2f nextVertex;
        int nextVertexQuadrant;
        int quadrantChange;
        for(int i = 1; i <= vertices.length; i++){
            if(i == vertices.length){
                nextVertex = vertices[0];
            }else{
                nextVertex = vertices[i];
            }
            nextVertexQuadrant = whichQuadrant(point, nextVertex);
            quadrantChange = nextVertexQuadrant-vertexQuadrant;
            LineSegment2f edge = new LineSegment2f(currentVertex, nextVertex);
            switch (quadrantChange) {
                case 2:
                    if (edge.isPointRightOfLine(point)){
                        quadrantChange = - 2;
                    }
                break;
                case -2:
                    if (edge.isPointLeftOfLine(point)){
                        quadrantChange = 2;
                    }
                break;
                case 3:
                    quadrantChange = -1;
                break;
                case -3:
                    quadrantChange =	 1;
                break;
            }
            total += quadrantChange;
            vertexQuadrant = nextVertexQuadrant;
            currentVertex = nextVertex;
        }
        return (Math.abs(total)==4);
    }

    public static int whichQuadrant(Vector2f point, Vector2f vertex){
        return (vertex.getX()<point.getX()?
                    (vertex.getY()<point.getY()? 1 : 4):
                    (vertex.getY()<point.getY()? 2 : 3)
                );
    }
}


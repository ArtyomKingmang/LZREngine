package com.kingmang.LZREngine.Geometry;

public class Rectangle2f implements Cloneable{
    private static final int V_TOPLEFT = 0;
    private static final int V_TOPRIGHT = 1;
    private static final int V_BOTTOMRIGHT = 2;
    private static final int V_BOTTOMLEFT = 3;


    private static final int E_TOPLEFT_TOPRIGHT = 0;
    private static final int E_TOPRIGHT_BOTTOMRIGHT = 1;
    private static final int E_BOTTOMRIGHT_BOTTOMLEFT = 2;
    private static final int E_BOTTOMLEFT_TOPLEFT = 3;

    private final Vector2f[] vertices;
    private final Vector2f[] edges_v;
    private final LineSegment2f[] edges;



    public Rectangle2f(Vector2f topleft, float w, float h){
        this(topleft, new Vector2f(topleft.x+w, topleft.y+h));

    }

    public Rectangle2f(float x, float y, float w, float h){
        this(new Vector2f(x, y), new Vector2f(x+w, y+h));

    }

    public Rectangle2f(Vector2f topleft, Vector2f bottomright){
        this(topleft, new Vector2f(bottomright.x, topleft.y), bottomright, new Vector2f(topleft.x, bottomright.y));

    }
    public Rectangle2f(Vector2f topleft, Vector2f topright, Vector2f bottomright, Vector2f bottomleft){
        vertices = new Vector2f[4];
        edges_v = new Vector2f[4];
        edges = new LineSegment2f[4];
        this.vertices[V_TOPLEFT] = topleft;
        this.vertices[V_TOPRIGHT] = topright;
        this.vertices[V_BOTTOMRIGHT] = bottomright;
        this.vertices[V_BOTTOMLEFT] = bottomleft;
        this.edges_v[E_TOPLEFT_TOPRIGHT] = this.vertices[V_TOPLEFT].getEdge(this.vertices[V_TOPRIGHT]);
        this.edges_v[E_TOPRIGHT_BOTTOMRIGHT] = this.vertices[V_TOPRIGHT].getEdge(this.vertices[V_BOTTOMRIGHT]);
        this.edges_v[E_BOTTOMRIGHT_BOTTOMLEFT] = this.vertices[V_BOTTOMRIGHT].getEdge(this.vertices[V_BOTTOMLEFT]);
        this.edges_v[E_BOTTOMLEFT_TOPLEFT] = this.vertices[V_BOTTOMLEFT].getEdge(this.vertices[V_TOPLEFT]);
        this.edges[E_TOPLEFT_TOPRIGHT] = new LineSegment2f(this.vertices[V_TOPLEFT], this.vertices[V_TOPRIGHT]);
        this.edges[E_TOPRIGHT_BOTTOMRIGHT] = new LineSegment2f(this.vertices[V_TOPRIGHT], this.vertices[V_BOTTOMRIGHT]);
        this.edges[E_BOTTOMRIGHT_BOTTOMLEFT] = new LineSegment2f(this.vertices[V_BOTTOMRIGHT], this.vertices[V_BOTTOMLEFT]);
        this.edges[E_BOTTOMLEFT_TOPLEFT] = new LineSegment2f(this.vertices[V_BOTTOMLEFT], this.vertices[V_TOPLEFT]);

    }
    @Override
    public Rectangle2f clone(){
        return new Rectangle2f(this.vertices[V_TOPLEFT], this.vertices[V_TOPRIGHT], this.vertices[V_BOTTOMRIGHT], this.vertices[V_BOTTOMLEFT]);
    }

    private int side(int edge, Vector2f testpoint){
        Vector2f edgepoint;
        switch(edge){
            case E_TOPLEFT_TOPRIGHT:
                edgepoint = this.vertices[V_TOPLEFT];
                break;
            case E_TOPRIGHT_BOTTOMRIGHT:
                edgepoint = this.vertices[V_TOPRIGHT];
                break;
            case E_BOTTOMRIGHT_BOTTOMLEFT:
                edgepoint = this.vertices[V_BOTTOMRIGHT];
                break;
            default:
                //E_BOTTOMLEFT_TOPLEFT:
                edgepoint = this.vertices[V_BOTTOMLEFT];
        }
        Vector2f perp = this.edges_v[edge].getPerpendicular();
        return (int) Math.signum(perp.x * (testpoint.x - edgepoint.x) + perp.y * (testpoint.y-edgepoint.y));
    }

    public boolean hasIntersection(Rectangle2f other){
        boolean hasSeparatingEdge = false;
        Vector2f edge;
        Vector2f selfpoint;
        int mySide;;
        boolean edgeintersectionfound;
        for(int e = 0; e < 4 && !hasSeparatingEdge; e++){
            edge = this.edges_v[e];

            switch(e){
                case E_TOPLEFT_TOPRIGHT:
                    selfpoint = this.vertices[V_BOTTOMLEFT];
                    break;
                case E_TOPRIGHT_BOTTOMRIGHT:
                    selfpoint = this.vertices[V_TOPLEFT];
                    break;
                case E_BOTTOMRIGHT_BOTTOMLEFT:
                    selfpoint = this.vertices[V_TOPLEFT];
                    break;
                default:
                    selfpoint = this.vertices[V_BOTTOMRIGHT];
            }
            edgeintersectionfound = false;
            mySide = side(e, selfpoint);
            for(int v = 0; v < 4 && !edgeintersectionfound; v++){
                edgeintersectionfound = (side(e, other.vertices[v])==mySide);
            }
            hasSeparatingEdge = !edgeintersectionfound;
        }
        for(int e = 0; e < 4 && !hasSeparatingEdge; e++){
            edge = other.edges_v[e];

            switch(e){
                case E_TOPLEFT_TOPRIGHT:
                    selfpoint = other.vertices[V_BOTTOMLEFT];
                    break;
                case E_TOPRIGHT_BOTTOMRIGHT:
                    selfpoint = other.vertices[V_TOPLEFT];
                    break;
                case E_BOTTOMRIGHT_BOTTOMLEFT:
                    selfpoint = other.vertices[V_TOPLEFT];
                    break;
                default:
                    selfpoint = other.vertices[V_BOTTOMRIGHT];
            }
            edgeintersectionfound = false;
            mySide = side(e, selfpoint);
            for(int v = 0; v < 4 && !edgeintersectionfound; v++){
                edgeintersectionfound = (side(e, this.vertices[v])==mySide);
            }
            hasSeparatingEdge = !edgeintersectionfound;
        }


        return !hasSeparatingEdge;
    }

    public Rectangle2f isotheticIntersection(Rectangle2f other){
        if(!hasIntersection(other)) return null;
        IsotheticBoundingBox2D bbMe = new IsotheticBoundingBox2D(this.vertices[V_TOPLEFT], this.vertices[V_BOTTOMRIGHT]);
        IsotheticBoundingBox2D bbOther = new IsotheticBoundingBox2D(other.vertices[V_TOPLEFT], other.vertices[V_BOTTOMRIGHT]);
        if(bbMe.containsIncl(bbOther)){
            return other.clone();
        }
        if(bbOther.containsIncl(bbMe)){
            return clone();
        }
        return new Rectangle2f(
                new Vector2f(
                    Math.max(this.vertices[V_TOPLEFT].x, other.vertices[V_TOPLEFT].x),
                    Math.max(this.vertices[V_TOPLEFT].y, other.vertices[V_TOPLEFT].y)
                ),
                new Vector2f(
                    Math.min(this.vertices[V_BOTTOMRIGHT].x, other.vertices[V_BOTTOMRIGHT].x),
                    Math.min(this.vertices[V_BOTTOMRIGHT].y, other.vertices[V_BOTTOMRIGHT].y)
                ));
    }

    public float width(){
        return this.vertices[V_TOPRIGHT].x-this.vertices[V_TOPLEFT].x;
    }
    public float height(){
        return this.vertices[V_BOTTOMLEFT].y-this.vertices[V_TOPLEFT].y;
    }

    public Vector2f topleft(){
        return this.vertices[V_TOPLEFT];
    }
    public Vector2f topright(){
        return this.vertices[V_TOPRIGHT];
    }

    public Vector2f bottomleft(){
        return this.vertices[V_BOTTOMLEFT];
    }
    public Vector2f bottomright(){
        return this.vertices[V_BOTTOMRIGHT];
    }


}

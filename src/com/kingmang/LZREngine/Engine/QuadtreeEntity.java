package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Enity.Entity;
import com.kingmang.LZREngine.Geometry.Rectangle2f;
import com.kingmang.LZREngine.Geometry.Vector2f;

import java.util.Arrays;

public class QuadtreeEntity extends Entity implements Quadtree.QuadtreeElement{
    private Rectangle2f bounds;
    private Rectangle2f previousBounds;
    private Quadtree quadtree;
    public QuadtreeEntity(String id){
        super(id);
        previousBounds = new Rectangle2f(new Vector2f(0,0), new Vector2f(0,0));
        bounds = new Rectangle2f(new Vector2f(0,0), new Vector2f(0,0));
    }



    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof QuadtreeEntity)) return false;
        return getId().equals(((QuadtreeEntity)o).getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    public int compareTo(Object o) {
        if(!(o instanceof QuadtreeEntity)) return -1;
        return getId().compareTo(((QuadtreeEntity)o).getId());
    }

    public Rectangle2f getBounds() {
        return bounds;
    }

    public Rectangle2f getPreviousBounds() {
        return previousBounds;
    }

    /**
     * @param bounds the bounds to set
     */
    public void setBounds(Rectangle2f bounds) {
        this.previousBounds = this.bounds;
        this.bounds = bounds;
    }

    @Override
    public void setPosition(Vector2f position){
        super.setPosition(position);
        setBounds(new Rectangle2f(position, getDrawWidth(), getDrawHeight()));
        if(quadtree!=null) quadtree.moved(this);
    }

    @Override
    public void setHeight(float height){
        super.setHeight(height);
        setBounds(new Rectangle2f(getPosition(), getDrawWidth(), getDrawHeight()));
        if(quadtree!=null) quadtree.moved(this);
    }
    @Override
    public void setWidth(float width){
        super.setWidth(width);
        setBounds(new Rectangle2f(getPosition(), getDrawWidth(), getDrawHeight()));
        if(quadtree!=null) quadtree.moved(this);
    }
    @Override
    public void setScale(float scale){
        super.setScale(scale);
        setBounds(new Rectangle2f(getPosition(), getDrawWidth(), getDrawHeight()));
        if(quadtree!=null) quadtree.moved(this);
    }

    public void addedToTree(Quadtree tree) {
        this.quadtree = tree;
    }

    public void removedFromTree(Quadtree tree) {
        this.quadtree = null;
    }


    public boolean isOnTree(){
        return getTree()!=null;
    }

    public Quadtree getTree(){
        return this.quadtree;
    }

    public QuadtreeEntity[] getPossibleCollisions(){
        Quadtree.QuadtreeElement[] elements = quadtree.findCollisions(this);
        return Arrays.copyOf(elements, elements.length, new QuadtreeEntity[elements.length].getClass());
    }

    public final void possiblyCollided(Quadtree.QuadtreeElement[] elements) {
        possiblyCollided(Arrays.copyOf(elements, elements.length, new QuadtreeEntity[elements.length].getClass()));
    }
    protected void possiblyCollided(QuadtreeEntity[] elements) {

    }

}

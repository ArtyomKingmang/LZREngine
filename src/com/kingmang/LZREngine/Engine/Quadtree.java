package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Geometry.Rectangle2f;
import com.kingmang.LZREngine.Geometry.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeSet;

public class Quadtree {
    public static interface QuadtreeElement extends Comparable{
        Rectangle2f getBounds();
        Rectangle2f getPreviousBounds();
        void addedToTree(Quadtree tree);
        void removedFromTree(Quadtree tree);
        void possiblyCollided(QuadtreeElement[] elements);
    }

    private class Node{
        Node quad1;
        Node quad2;
        Node quad3;
        Node quad4;
        Rectangle2f bounds;

        public Node(Rectangle2f bounds){
            this.bounds = bounds;
            if(isLeaf()) return;
            float h = bounds.height();
            float w = bounds.width();
            float newh = h/2f;
            float neww = w/2f;

            if(newh>leafsize&&newh>leafsize){
                quad1 = new Node(new Rectangle2f(bounds.topleft(), new Vector2f(bounds.topleft().x+neww, bounds.topleft().y+newh)));
                quad2 = new Node(new Rectangle2f(new Vector2f(bounds.topleft().x+neww, bounds.topleft().y), new Vector2f(bounds.topright().x, bounds.topright().y+newh)));
                quad3 = new Node(new Rectangle2f(new Vector2f(bounds.topleft().x, bounds.topleft().y+newh), new Vector2f(bounds.bottomleft().x+neww, bounds.bottomleft().y)));
                quad4 = new Node(new Rectangle2f(new Vector2f(bounds.topleft().x+neww, bounds.topleft().y+newh), bounds.bottomright()));
            }else{
                quad1 = new Leaf(new Rectangle2f(bounds.topleft(), new Vector2f(bounds.topleft().x+neww, bounds.topleft().y+newh)));
                quad2 = new Leaf(new Rectangle2f(new Vector2f(bounds.topleft().x+neww, bounds.topleft().y), new Vector2f(bounds.topright().x, bounds.topright().y+newh)));
                quad3 = new Leaf(new Rectangle2f(new Vector2f(bounds.topleft().x, bounds.topleft().y+newh), new Vector2f(bounds.bottomleft().x+neww, bounds.bottomleft().y)));
                quad4 = new Leaf(new Rectangle2f(new Vector2f(bounds.topleft().x+neww, bounds.topleft().y+newh), bounds.bottomright()));
            }
        }

        void moved(QuadtreeElement element){
                quad1.moved(element);
                quad2.moved(element);
                quad3.moved(element);
                quad4.moved(element);
        }

        void add(QuadtreeElement element){
            if(inBounds(element.getBounds())){
                quad1.add(element);
                quad2.add(element);
                quad3.add(element);
                quad4.add(element);
            }
        }
        void remove(QuadtreeElement element){
            if(inBounds(element.getBounds())){
                quad1.remove(element);
                quad2.remove(element);
                quad3.remove(element);
                quad4.remove(element);
            }
        }
        void findCollisions(QuadtreeElement element, ArrayList<QuadtreeElement> collisions){
            if(inBounds(element.getBounds())){
                quad1.findCollisions(element, collisions);
                quad2.findCollisions(element, collisions);
                quad3.findCollisions(element, collisions);
                quad4.findCollisions(element, collisions);
            }
        }
        boolean isLeaf(){
            return false;
        }
        boolean inBounds(Rectangle2f check){
            return (bounds.hasIntersection(check));
        }
        public void render(Graphics2D g2){
            quad1.render(g2);
            quad2.render(g2);
            quad3.render(g2);
            quad4.render(g2);
        }

    }

    private class Leaf extends Node{
        TreeSet<QuadtreeElement> elements;
        public Leaf(Rectangle2f bounds){
            super(bounds);
            this.elements = new TreeSet<QuadtreeElement>();
        }
        @Override
        void moved(QuadtreeElement element){
            if(!inBounds(element.getBounds())){
                elements.remove(element);
            }
            if(inBounds(element.getBounds())){
                elements.add(element);
                if(elements.size()>1) element.possiblyCollided(elements.toArray(new QuadtreeElement[elements.size()]));
            }
        }
        @Override
        void add(QuadtreeElement element){
            if(inBounds(element.getBounds())){
                elements.add(element);
                if(elements.size()>1) element.possiblyCollided(elements.toArray(new QuadtreeElement[elements.size()]));
            }
        }
        @Override
        void remove(QuadtreeElement element){
            if(inBounds(element.getBounds())){
                elements.remove(element);
            }
        }
        @Override
        public void render(Graphics2D g2){
            Rectangle2D rect = new Rectangle2D.Float(bounds.topleft().x, bounds.topleft().y, bounds.width(), bounds.height());
            g2.setColor(Color.green);
            g2.draw(rect);
            double alpha = 0;
            if(elements.size()>0) {
                alpha = Math.min((elements.size()/3d)*150d, 150d);
                g2.setColor(new Color(0, 255, 0, (int)alpha));
                g2.fill(rect);
            }
        }
        @Override
        void findCollisions(QuadtreeElement element, ArrayList<QuadtreeElement> collisions){
            if(inBounds(element.getBounds())){
                for(QuadtreeElement el : elements) {
                    if(!collisions.contains(el)&&!element.equals(el)) collisions.add(el);
                }
            }
        }

        @Override
        boolean isLeaf(){
            return true;
        }
    }
    private Node root;
    private float leafsize;
    public Quadtree(Rectangle2f bounds, float leafsize){
        if(leafsize<1)leafsize=1;
        this.leafsize = leafsize;
        this.root = new Node(bounds);
    }

    public void add(QuadtreeElement element){
        root.add(element);
        element.addedToTree(this);
    }
    public void remove(QuadtreeElement element){
        root.remove(element);
        element.removedFromTree(this);
    }

    public void moved(QuadtreeElement element){
        root.moved(element);
    }

    public QuadtreeElement[] findCollisions(QuadtreeElement element){
        ArrayList<QuadtreeElement> collisions = new ArrayList<QuadtreeElement>();
        root.findCollisions(element, collisions);
        return collisions.toArray(new QuadtreeElement[collisions.size()]);
    }


    public void render(Graphics2D g2){
        root.render(g2);
    }

}

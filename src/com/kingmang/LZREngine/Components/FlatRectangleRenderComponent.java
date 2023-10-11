package com.kingmang.LZREngine.Components;

import com.kingmang.LZREngine.Engine.RenderComponent;
import com.kingmang.LZREngine.Geometry.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class FlatRectangleRenderComponent extends RenderComponent {
    private Color colour;
    
    public FlatRectangleRenderComponent(String id, Color colour){
        super(id);
        this.colour = colour;
    }
    @Override
    public void render(Graphics2D g) {
        Vector2f pos = getOwner().getPosition();
        float scale = getOwner().getScale();
        float width = getOwner().getWidth();
        float height = getOwner().getHeight();
        Rectangle2D r = new Rectangle2D.Float(pos.x, pos.y, 
                width*scale, height*scale);
        Color orig = g.getColor();
        g.setColor(colour);
        g.fill(r);
        g.setColor(orig);
    }
}

package com.kingmang.LZREngine.Components;

import com.kingmang.LZREngine.Engine.RenderComponent;
import com.kingmang.LZREngine.Geometry.Vector2f;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public class LoadingBarComponent extends RenderComponent {
    private Color border;
    private Color fill;
    private float value;
    public LoadingBarComponent(String id, Color border, Color fill){
        super(id);
        this.border = border;
        this.fill = fill;
        this.value = 0;
    }
    @Override
    public void render(Graphics2D g) {
        Vector2f pos = getOwner().getPosition();
        float scale = getOwner().getScale();
        float width = getOwner().getWidth();
        float height = getOwner().getHeight();
        float drawWidth = width*scale;
        float drawHeight = height*scale;
        float spacer = 10f;
        Rectangle2D b = new Rectangle2D.Float(pos.x, pos.y,
                drawWidth, drawHeight);
        float fillWidth = drawWidth * this.getValue();

        Rectangle2D f = new Rectangle2D.Float(pos.x+spacer, pos.y+spacer,
                fillWidth-(spacer*2f), drawHeight-(spacer*2f));
        Color orig = g.getColor();
        Stroke origs = g.getStroke();
        g.setStroke(new BasicStroke(3f));
        g.setColor(border);
        g.draw(b);
        g.setColor(fill);
        g.fill(f);
        g.setColor(orig);
        g.setStroke(origs);

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if(value>1)value=1f;
        if(value<0)value=0f;
        this.value = value;
    }

}

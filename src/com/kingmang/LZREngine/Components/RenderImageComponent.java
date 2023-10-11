package com.kingmang.LZREngine.Components;

import com.kingmang.LZREngine.Engine.RenderComponent;
import com.kingmang.LZREngine.Geometry.Vector2f;
import com.kingmang.LZREngine.Images.ImageLoader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class RenderImageComponent extends RenderComponent {
    private BufferedImage img;
    
    public RenderImageComponent(String id, String imagekey){
        super(id);
        this.img = ImageLoader.get().getStaticImage(imagekey);
    }
    @Override
    public void render(Graphics2D g) {
        Vector2f pos = getOwner().getPosition();
        float scale = getOwner().getScale();
        float width = getOwner().getWidth();
        float height = getOwner().getHeight();
        g.drawImage(this.img, pos.getXi(), pos.getYi(), (int)(width*scale), (int)(height*scale), null);
        
//        Stroke origs = g.getStroke();
//        Color orig = g.getColor();
//        g.setStroke(new BasicStroke(3f));
//        g.setColor(Color.blue);
//        g.drawRect(pos.getXi(), pos.getYi(), (int)(width*scale), (int)(height*scale));
//        g.setColor(orig);
//        g.setStroke(origs);
    }

    /**
     * @param img the img to set
     */
    public void setImage(String imagekey) {
        this.img = ImageLoader.get().getStaticImage(imagekey);
    }


   

}

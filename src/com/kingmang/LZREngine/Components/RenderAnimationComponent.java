package com.kingmang.LZREngine.Components;

import com.kingmang.LZREngine.Engine.RenderComponent;
import com.kingmang.LZREngine.Geometry.Vector2f;
import com.kingmang.LZREngine.Animation.Animation;

import java.awt.Graphics2D;


public class RenderAnimationComponent extends RenderComponent {
    private Animation.AnimationInstance animation;
    
    public RenderAnimationComponent(String id, String animationKey){
        super(id);
        this.animation = Animation.getAnimationInstance(animationKey);
    }
    @Override
    public void render(Graphics2D g) {
        Vector2f pos = getOwner().getPosition();
        float scale = getOwner().getScale();
        float width = getOwner().getWidth();
        float height = getOwner().getHeight();
        g.drawImage(this.animation.getFrame(), pos.getXi(), pos.getYi(), (int)(width*scale), (int)(height*scale), null);

    }

    public void pause() {
        this.animation.pause();
    }

    public void stop() {
        this.animation.stop();
    }

    public void resume() {
        this.animation.stop();
    }
    public boolean isRunning() {
        return this.animation.isRunning();
    }
    public boolean isStarted() {
        return this.animation.isStarted();
    }
    public void reset() {
        this.animation.reset();
    }

}

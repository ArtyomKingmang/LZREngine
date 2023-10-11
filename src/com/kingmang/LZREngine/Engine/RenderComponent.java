package com.kingmang.LZREngine.Engine;

import com.kingmang.LZREngine.Enity.Component;
import com.kingmang.LZREngine.Exeptions.EntityException;
import com.kingmang.LZREngine.Enity.Input;

import java.awt.Graphics2D;

public abstract class RenderComponent extends Component {

    public RenderComponent(String id){
        super(id);
    }

    public abstract void render(Graphics2D g);

    @Override
    public void update(Input input, float updatesPerSecond) throws EntityException {
    }
}

package com.kingmang.LZREngine.Phisics;

import com.kingmang.LZREngine.Geometry.Vector2f;
import com.kingmang.LZREngine.GameObject.GameObject;

public class Physics {
    public static Vector2f GRAVITY = new Vector2f(0, 0);
    public static <T extends GameObject> Vector2f nextPosition(T object, float updatesPerSecond){
        object.setVelocity(object.getVelocity().proportion(object.getDrag()));
        object.addVelocity(GRAVITY);
        Vector2f nextPos = object.getPosition().clone();
        nextPos.add(
                    object.getVelocity().getX()/updatesPerSecond,
                    object.getVelocity().getY()/updatesPerSecond
                    );
        return nextPos;
    }
}


package com.kingmang.LZREngine.example;

import com.kingmang.LZREngine.GameObject.GameObject;

public class TestObject extends GameObject {
    public TestObject(){
        this.setPosition(200, 200);
        this.setDrag(.01f, .01f);

        this.addVertex(0, 0);
        this.addVertex(100, 0);
        this.addVertex(150, 50);
        this.addVertex(150, 150);
        this.addVertex(100, 200);
        this.addVertex(0, 200);
        this.addVertex(-50, 150);
        this.addVertex(-50, 50);
        this.addVertex(0, 0);

    }
}

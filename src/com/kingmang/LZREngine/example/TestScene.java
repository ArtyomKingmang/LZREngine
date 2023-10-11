package com.kingmang.LZREngine.example;

import com.kingmang.LZREngine.Scene.Scene;
import com.kingmang.LZREngine.Exeptions.SceneException;
import com.kingmang.LZREngine.Geometry.Polygon2D;
import com.kingmang.LZREngine.Geometry.Vector2f;
import com.kingmang.LZREngine.Phisics.Physics;
import com.kingmang.LZREngine.example.TestObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;


public class TestScene extends Scene {
    
    public TestScene(){
        super("test");
    }
    private float x = 0;
    private float y = 0;
    private TestObject object1;
    private TestObject object2;
    private Vector2f point = new Vector2f(600, 300);
    private Polygon2D gameArea;
    boolean collision = false;
    @Override
    public void initialise() throws SceneException {
        object1 = new TestObject();
        object2 = new TestObject();
        Physics.GRAVITY = new Vector2f(0, 0);
        gameArea = new Polygon2D();
        gameArea.addVertex(0f, 0f);
        gameArea.addVertex(1240, 0f);
        gameArea.addVertex(1240f, 650f);
        gameArea.addVertex(0f, 650f);
        gameArea.close();

    }

    @Override
    public void render(Graphics2D g) {
        GeneralPath path1 = object1.getPolygon().getPath();
        g.setColor((collision?Color.RED:Color.BLUE));
        g.draw(path1);
        GeneralPath path2 = object2.getPolygon().getPath();
        g.setColor((collision?Color.RED:Color.CYAN));
        g.draw(path2);

        g.setColor(Color.black);
        g.draw(gameArea.getPath());

        g.drawString(""+object1.getVelocity().toString(1), (int)object1.getPosition().getX(),
                (int)object1.getPosition().getY()-20);
    }



    @Override
    public void update(float updatesPerSecond) throws SceneException {
        Vector2f nextPosition = Physics.nextPosition(object1, updatesPerSecond);
        collision = object1.collidesWith(nextPosition, object2);
        if(!object1.isWithin(nextPosition, gameArea)){
            object1.getVelocity().multiplyY(0.9f);
            object1.getVelocity().multiplyX(0.9f);
            object1.getVelocity().flipY();
            object1.getVelocity().flipX();

        }
        object1.setPosition(nextPosition);
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_D){
            object1.addVelocity(30, 0);
            //object1.setVelocity(100,0);
        }
        if(e.getKeyCode()==KeyEvent.VK_A){
            object1.addVelocity(-30, 0);
            //object1.setVelocity(-100,0);
        }
        if(e.getKeyCode()==KeyEvent.VK_W){
            object1.addVelocity(0, -30);
            //object1.setVelocity(0,-100);
        }
        if(e.getKeyCode()==KeyEvent.VK_S){
            object1.addVelocity(0, 30);
            //object1.setVelocity(0,100);
        }


    }

    @Override
    public void onGameLoad() throws SceneException {
    }

}

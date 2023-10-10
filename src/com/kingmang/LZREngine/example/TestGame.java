
package com.kingmang.LZREngine.example;

import com.kingmang.LZREngine.Engine.Engine;
import com.kingmang.LZREngine.Game.Game;
import com.kingmang.LZREngine.Engine.SceneException;


public class TestGame extends Game {
    public TestGame(){
        super("Kingmang", 1280,720);

    }

    public static void main(String[] args) {
        TestGame game = new TestGame();
        game.setShowFPS(true);
        try {
            game.start();
        } catch (SceneException ex) {
            Engine.get().exceptionThrown(ex);
            Engine.get().fatalError("Scene Exception at Game Start: "+ex.getMessage());
        }
    }

    @Override
    public void setup() {
    }

    @Override
    public void addScenes() throws SceneException {

        newScene(new TestScene());
    }

}

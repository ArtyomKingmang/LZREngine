package com.kingmang.LZREngine.Game;

import java.awt.Graphics;

public interface GameContainer {
  
    public int getAvailableWidth();
    
    public int getAvailableHeight();

    
    public void repaint();

    public void paint(Graphics g);

    public void requestFocus();

}

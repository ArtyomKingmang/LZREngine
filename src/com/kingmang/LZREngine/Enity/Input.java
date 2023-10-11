package com.kingmang.LZREngine.Enity;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Input {
    private KeyEvent keyTyped;
    private KeyEvent keyPressed;
    private KeyEvent keyReleased;
    private MouseEvent mouseDragged;
    private MouseEvent mouseMoved;
    private MouseEvent mouseClicked;
    private MouseEvent mousePressed;
    private MouseEvent mouseReleased;
    private MouseEvent mouseEntered;
    private MouseEvent mouseExited;
    private MouseWheelEvent mouseWheelMoved;


    public boolean isKeyTyped() {
        return keyTyped!=null;
    }

    public boolean isKeyPressed() {
        return keyPressed!=null;
    }
    public boolean isKeyReleased() {
        return keyReleased!=null;
    }

    public boolean isMouseDragged() {
        return mouseDragged!=null;
    }

    public boolean isMouseMoved() {
        return mouseMoved!=null;
    }

    public boolean isMouseClicked() {
        return mouseClicked!=null;
    }

    public boolean isMousePressed() {
        return mousePressed!=null;
    }

    public boolean isMouseReleased() {
        return mouseReleased!=null;
    }

    public boolean isMouseEntered() {
        return mouseEntered!=null;
    }

    public boolean isMouseExited() {
        return mouseExited!=null;
    }

    public boolean isMouseWheelMoved() {
        return mouseWheelMoved!=null;
    }

    void keyTyped(KeyEvent e) {
        keyTyped = e;
    }

    void keyPressed(KeyEvent e) {
        keyPressed = e;
    }
    void keyReleased(KeyEvent e) {
        keyReleased = e;
    }

    void mouseDragged(MouseEvent e) {
        mouseDragged = e;
    }

    void mouseMoved(MouseEvent e) {
        mouseMoved = e;
    }

    void mouseClicked(MouseEvent e) {
        mouseClicked = e;
    }

    void mousePressed(MouseEvent e) {
        mousePressed = e;
    }

    void mouseReleased(MouseEvent e) {
        mouseReleased = e;
    }

    void mouseEntered(MouseEvent e) {
        mouseEntered = e;
    }

    void mouseExited(MouseEvent e) {
        mouseExited = e;
    }

    void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelMoved = e;
    }

    /**
     * @return the keyTyped
     */
    public KeyEvent getKeyTyped() {
        KeyEvent e = keyTyped;
//        keyTyped = null;
        return e;
    }

    /**
     * @return the keyPressed
     */
    public KeyEvent getKeyPressed() {
        KeyEvent e = keyPressed;
//        keyPressed = null;
        return e;
    }

    /**
     * @return the keyReleased
     */
    public KeyEvent getKeyReleased() {
        KeyEvent e = keyReleased;
//        keyReleased = null;
        return e;
    }

    /**
     * @return the mouseDragged
     */
    public MouseEvent getMouseDragged() {
        MouseEvent e = mouseDragged;
//        mouseDragged = null;
        return e;
    }

    /**
     * @return the mouseMoved
     */
    public MouseEvent getMouseMoved() {
        MouseEvent e = mouseMoved;
//        mouseMoved = null;
        return e;
    }

    /**
     * @return the mouseClicked
     */
    public MouseEvent getMouseClicked() {
        MouseEvent e = mouseClicked;
//        mouseClicked = null;
        return e;
    }

    /**
     * @return the mousePressed
     */
    public MouseEvent getMousePressed() {
        MouseEvent e = mousePressed;
//        mousePressed = null;
        return e;
    }

    /**
     * @return the mouseReleased
     */
    public MouseEvent getMouseReleased() {
        MouseEvent e = mouseReleased;
//        mouseReleased = null;
        return e;
    }

    /**
     * @return the mouseEntered
     */
    public MouseEvent getMouseEntered() {
        MouseEvent e = mouseEntered;
//        mouseEntered = null;
        return e;
    }

    /**
     * @return the mouseExited
     */
    public MouseEvent getMouseExited() {
        MouseEvent e = mouseExited;
//        mouseExited = null;
        return e;
    }

    /**
     * @return the mouseWheelMoved
     */
    public MouseWheelEvent getMouseWheelMoved() {
        MouseWheelEvent e = mouseWheelMoved;
//        mouseWheelMoved = null;
        return e;
    }

    void clear(){
        keyTyped = null;
        keyPressed = null;
        keyReleased = null;
        mouseDragged = null;
        mouseMoved = null;
        mouseClicked = null;
        mousePressed = null;
        mouseReleased = null;
        mouseEntered = null;
        mouseExited = null;
        mouseWheelMoved = null;
    }
}

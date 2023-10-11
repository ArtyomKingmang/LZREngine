/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kingmang.LZREngine.Exeptions;


public class EntityException extends SceneException {


    public EntityException() {
    }

    public EntityException(String msg) {
        super(msg);
    }

    public EntityException(Throwable throwable) {
        super(throwable);
    }

    public EntityException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

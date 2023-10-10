
package com.kingmang.LZREngine.Entity;


import com.kingmang.LZREngine.Engine.SceneException;


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

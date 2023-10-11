
package com.kingmang.LZREngine.Exeptions;

public class SceneException extends Exception {

    public SceneException() {
    }
    public SceneException(String msg) {
        super(msg);
    }

    public SceneException(Throwable throwable) {
        super(throwable);
    }

    public SceneException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

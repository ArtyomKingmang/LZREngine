package com.kingmang.LZREngine.Exeptions;

public class ImageException extends Exception {


    public ImageException() {
    }


    public ImageException(String msg) {
        super(msg);
    }

    public ImageException(Throwable throwable) {
        super(throwable);
    }

    public ImageException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

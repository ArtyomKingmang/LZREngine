package com.kingmang.LZREngine.utils.tools;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;

public class MouseButtonTool {
    public static boolean isLeft(int button){
        return (button == MouseEvent.BUTTON1);
    }
    public static boolean isRight(int button){
        if(button == MouseEvent.BUTTON3){
            return true;
        }
        if(MouseInfo.getNumberOfButtons()<3){
            return (button != MouseEvent.BUTTON1);
        }
        return false;
    }
    public static boolean isMiddle(int button){
        if(MouseInfo.getNumberOfButtons()>2){
            return (button == MouseEvent.BUTTON2);
        }
        return false;
    }
}

package com.kingmang.LZREngine.utils.tools;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DimensionTool {

    private static int dpi = -1; 
    private static Dimension screenDimensions;

    private static float inchInCm = 0.393700787f;
    private static float cmInInch = 2.54f;

    private static float ptInInch = 72f;


    public static int getScreenResolution(){
        if(dpi>0) return dpi;
        dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        return dpi;
    }
    public static Dimension getScreenSize(){
        if(screenDimensions!=null) return screenDimensions;
        screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        return screenDimensions;
    }

    public static int cmToPixels(float cm){
        float inches = inchInCm*cm;
        return (int)Math.round(getScreenResolution()*inches);
    }
    public static float pixelsToCm(int pixels){
        float inches = (float)pixels/(float)getScreenResolution();
        return cmInInch*inches;
    }

    public static float cmToPt(float cm){
        float inches = inchInCm*cm;
        return inches*ptInInch;
    }

    public static float ptToPixel(float pt) {
        float inches = pt/ptInInch;
        return getScreenResolution()*inches;
    }

}

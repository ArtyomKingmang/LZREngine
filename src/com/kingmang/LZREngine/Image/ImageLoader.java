
package com.kingmang.LZREngine.Image;

import com.kingmang.LZREngine.Engine.Renderer;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class ImageLoader {

    private static ImageLoader instance;

    private HashMap<String, ArrayList<BufferedImage>> imageMap;
    private HashMap<String, ArrayList<String>> groupNamesMap;

    private String imageResourceFolder;

    private ImageLoader(){
        imageMap = new HashMap<String, ArrayList<BufferedImage>>();
        groupNamesMap = new HashMap<String, ArrayList<String>>();
    }

    public static ImageLoader get(){
        if(instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }


    public void load(String imageListLocation) throws ImageException{

        if(imageListLocation.lastIndexOf('/')>0){
            imageResourceFolder = imageListLocation.substring(0, imageListLocation.lastIndexOf('/'))+"/";
        }else{
            imageResourceFolder = "";
        }
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream(imageListLocation);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            // BufferedReader br = new BufferedReader( new FileReader(imsFNm));
            String line;
            Pattern linePattern = Pattern.compile("^\\[" +
                        "([a-zA-Z]{3});" +
                        "([ a-zA-Z0-9_\\-\\.][ a-zA-Z0-9_\\-\\.]*);" +
                        "([^]]*)\\]\\s*$");
            Pattern extNumberPattern = Pattern.compile("^([a-zA-Z]{3,4});([0-9]{1,3})$");
            Pattern extXYPattern = Pattern.compile("^([a-zA-Z]{3,4});([0-9]{1,3});([0-9]{1,3})$");
            Pattern imageListPattern = Pattern.compile("([ a-zA-Z0-9_\\-\\.][ a-zA-Z0-9_\\-\\.]*);");
            Matcher lineMatcher;
            Matcher extNumberMatcher;
            Matcher extXYMatcher;
            Matcher imageListMatcher;
            String type;
            String ext;
            int number;
            int x;
            int y;
            String name;
            String theRest;
            ArrayList<String> imageList;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                
                lineMatcher = linePattern.matcher(line);
                if(!lineMatcher.matches()) {
                    throw new ImageException("Failed to match image info: "+line);
                }
                type = lineMatcher.group(1);
                name = lineMatcher.group(2);
                theRest = lineMatcher.group(3);
                if(type.equals("stc")){
                    ext = theRest;
                    loadStaticImage(name, ext);
                }else if(type.equals("num")){
                    extNumberMatcher = extNumberPattern.matcher(theRest);
                    if(!extNumberMatcher.matches()) {
                        throw new ImageException("Failed to match image info, extension and number for: "+line);
                    }
                    ext = extNumberMatcher.group(1);
                    try {
                        number = Integer.parseInt(extNumberMatcher.group(2));
                    } catch (NumberFormatException numberFormatException) {
                        throw new ImageException("Failed to match image info, number for: "+line);
                    }
                    loadSequenceImage(name, ext, number);
                }else if(type.equals("spr")){
                    extXYMatcher = extXYPattern.matcher(theRest);
                    if(!extXYMatcher.matches()) {
                        throw new ImageException("Failed to match image info, extension, x and y for: "+line);
                    }
                    ext = extXYMatcher.group(1);
                    try {
                        x = Integer.parseInt(extXYMatcher.group(2));
                        y = Integer.parseInt(extXYMatcher.group(3));
                    } catch (NumberFormatException numberFormatException) {
                        throw new ImageException("Failed to match image info, x/y for: "+line);
                    }
                    loadSpriteSheetImage(name, ext, x, y);
                }else if(type.equals("grp")){
                    imageListMatcher = imageListPattern.matcher(theRest);
                    imageList = new ArrayList<String>();
                    while(imageListMatcher.find()){
                        imageList.add(imageListMatcher.group(1));
                    }
                    loadGroupImage(name, imageList.toArray(new String[imageList.size()]));
                }else{
                    throw new ImageException("Image type "+type+" not recognised");
                }
            }
            br.close();
        } catch (IOException e) {
            throw new ImageException("Could not load images from: "+imageListLocation, e);
        }finally{
            if(stream!=null) try {stream.close();} catch (IOException ex) {}
        }
    }  

    public void loadStaticImage(String filename, String ext) throws ImageException{
        if(imageMap.containsKey(filename)){
            throw new ImageException("Image with filename "+filename+" already exists.");
        }

        BufferedImage image = loadImage(imageResourceFolder + filename + "." + ext);
        addImageToMap(filename, image);

    }
    public void loadSequenceImage(String filename, String ext, int number) throws ImageException{
        if(imageMap.containsKey(filename)){
            throw new ImageException("Image with filename "+filename+" already exists.");
        }
        BufferedImage image;
        for(int i=0; i< number; i++){
            image = loadImage(imageResourceFolder + filename + i + "." + ext);
            addImageToMap(filename, image);
        }
    }

    public BufferedImage[] getSpriteSheet(String name){
        if(imageMap.containsKey(name)){
            return imageMap.get(name).toArray(new BufferedImage[imageMap.get(name).size()]);
        }else{
            return new BufferedImage[0];
        }
    }
    public BufferedImage getGroupedImage(String groupName, String imageName){
        if(groupNamesMap.containsKey(groupName)){
            int index = groupNamesMap.get(groupName).indexOf(imageName);
            return getSequencedImage(groupName, index);
        }
        return null;
    }
    public BufferedImage getSpriteImage(String name, int index){
        return getSequencedImage(name, index);
    }
    public BufferedImage getSequencedImage(String name, int index){
        if(imageMap.containsKey(name)){
            ArrayList<BufferedImage> images =imageMap.get(name);
            if(images.size()>index && index>= 0){
                return images.get(index);
            }
        }
        return null;
    }
    public BufferedImage getStaticImage(String name){
        if(imageMap.containsKey(name)){
            ArrayList<BufferedImage> images =imageMap.get(name);
            if(images.size()>0){
                return images.get(0);
            }
        }
        return null;
    }
    public void loadSpriteSheetImage(String filename, String ext, int maxX, int maxY) throws ImageException{
        if (maxX <= 0) {
            throw new ImageException("Sprite sheet "+filename+" has no columns");
        }
        if (maxY <= 0) {
            throw new ImageException("Sprite sheet "+filename+" has no rows");
        }
        if(imageMap.containsKey(filename)){
            throw new ImageException("Image with filename "+filename+" already exists.");
        }
        imageMap.put(filename, new ArrayList<BufferedImage>());
        BufferedImage spriteImage;
     
        spriteImage = loadImage(imageResourceFolder + filename + "." + ext);

        int spriteWidth = (int) spriteImage.getWidth() / maxX;
        int spriteHeight = (int) spriteImage.getHeight() / maxY;
        int transparency = spriteImage.getColorModel().getTransparency();
        Graphics2D spriteGC;
        BufferedImage sprite;
        //Add them row by row to imageMap
        for (int y=0; y < maxY; y++) {
            for(int x = 0; x < maxX; x++){
                sprite = Renderer.get().createImage(spriteWidth,spriteHeight,transparency);
                spriteGC = sprite.createGraphics();
                spriteGC.drawImage(spriteImage, 0,0, spriteWidth,
                        spriteHeight, x*spriteWidth, y*spriteHeight,
                        (x*spriteWidth)+spriteWidth, y*spriteHeight+spriteHeight,
                        null);
                spriteGC.dispose();
                this.imageMap.get(filename).add(sprite);
            }
        }
        
    }
    public void loadGroupImage(String groupname, String[] imagenames) throws ImageException{
        if(imagenames.length<1){
            throw new ImageException("Group "+groupname+" is empty");
        }
        if(imageMap.containsKey(groupname)){
            throw new ImageException("Group with name "+groupname+" already exists.");
        }
        this.imageMap.put(groupname, new ArrayList<BufferedImage>());
        this.groupNamesMap.put(groupname, new ArrayList<String>());
        BufferedImage spriteImage;
        for(String imageName : imagenames){
            spriteImage = loadImage(imageResourceFolder + imageName);
            this.imageMap.get(groupname).add(spriteImage);
            this.groupNamesMap.get(groupname).add(imageName.substring(0,imageName.lastIndexOf('.')));
        }
    }

    private void addImageToMap(String name, BufferedImage image) throws ImageException{
        if(image == null)throw new ImageException("Error adding image to map: image is null");
        if(name == null)throw new ImageException("Error adding image to map: name is null");
        if(imageMap.containsKey(name)){
            imageMap.get(name).add(image);
        }else{
            ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
            images.add(image);
            imageMap.put(name, images);
        }
    }


    public BufferedImage loadImage(String imageResourceLocation) throws ImageException {
        URL resource = getClass().getResource(imageResourceLocation);
        if(resource==null){
            throw new ImageException("Could not find resource "+imageResourceLocation);
        }
        BufferedImage im =null;
        try {
            im = ImageIO.read(resource);
        } catch (IOException ex) {
             throw new ImageException("Could not load image '"+imageResourceLocation+"'", ex);
        }
        int transparency = im.getColorModel().getTransparency();

        BufferedImage copy = Renderer.get().createImage(im.getWidth(),
                im.getHeight(), transparency);
        // create a graphics context
        Graphics2D g2d = copy.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // copy image
        g2d.drawImage(im, 0, 0, null);
        g2d.dispose();
        return copy;
    }
}

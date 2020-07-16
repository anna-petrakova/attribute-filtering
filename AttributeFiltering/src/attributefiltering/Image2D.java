/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributefiltering;

import java.util.Arrays;

/**
 * Class representing a 2D image.
 * @author Anna Petráková
 */
public class Image2D {
    private Pixel[] data;
    private int width;
    private int height;
            
    public Image2D() {
        
    }
    /**
     * Creates image
     * @param width pixel width of the requested image
     * @param height pixel height of the requested image
     */
    public Image2D(int width, int height) {
        Pixel[] newData = new Pixel[width*height];
        
        this.width = width;
        this.height = height;
        this.data = newData;
    }
    /**
     * Creates image with defined data
     * @param width pixel width of the requested image
     * @param height pixel height of the requested image
     * @param data values in pixels
     */
    public Image2D(int width, int height, int[] data) {
        this.width = width;
        this.height = height;
        int pixelCount = width*height;
        this.data = new Pixel[width*height];
        for (int i = 0; i < pixelCount; i++) {
            this.data[i] = new Pixel(data[i], i%width, i/width);
        }
      
    }
    
    /**
     * Used for getting the value from pixel
     * @param x position on x axis of the requested pixel
     * @param y position on y axis of the requested pixel 
     * @return value on position (x,y) in image
     */    
    public Pixel getPixel(int x, int y) {
        if (y >= 0 && y <height && x >= 0 && x < width) {
            return data[y*width+x];
        }
        return null;
        
    }
    
    public Pixel getPixel(int position) {
        if (position < width*height) {
            return data[position];
        }
        return null;
    }
    
    public int index(int x, int y) {
        return y*width+x;
    }
    /**
     * Used for setting the value of pixel
     * @param x position on x axis of the requested pixel
     * @param y position on y axis of the requested pixel 
     * @param value new value for position (x,y) 
     */
    public void setPixel(int x, int y, Pixel value) {
        if (y >= 0 && y <height && x >= 0 && x < width) {
            data[y*width+x] = value;
        }
    }
    /**
     * Used for getting the height of the image
     * @return height of image
     */
    public int getHeight() {
        return height;
    }
    /**
     * Used for getting the width of the image
     * @return width of image
     */
    public int getWidth() {
        return width;
    }
    
    public Pixel[] getValues() {
        
        return data;
    }
    
    public int[] getData() {
        int[] new_data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            new_data[i] = data[i].getValue();
        }
        return new_data;
    }
    
    /**
     * Used for getting the number of the pixels
     * @return number of pixels
     */
    public int getNumPixels() {
        return width*height;
    }
    
    public String toString() {
        String str = "";
        for (int i = 0; i < width*height; i++) {
            if (data[i] == null) continue;
            str += data[i].toString() + "  ";
            if ((i +1) % width == 0) {
                str += System.lineSeparator();
            }
        }
        return str;
    }    
}

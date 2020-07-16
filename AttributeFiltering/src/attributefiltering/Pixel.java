/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributefiltering;

/**
 * Class reprsenting one pixel in an image.
 * @author Anna Petráková
 */
public class Pixel {
    private int value; // intensity of pixel
    private Point point;
    private String name;
           
    public Pixel(int value, int x, int y) {
        this.value = value;
        this.point = new Point(x, y);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public int getValue() {
        return value;
    }
    
    public int getX() {
        return point.getX();
    }
    
    public int getY() {
        return point.getY();
    }
    
    public String toString() {
        return Double.toString(value) + " " + name;
    }
       
    class Point {
        private int x;
        private int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
    }
}



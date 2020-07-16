/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

import attributefiltering.Pixel;

/**
 * Parameters in canonical nodes.
 * @author Anna Petráková
 */
public class Params {
    private int area;
    private double perimeter;
    private double centroid_x;
    private double centroid_y;
    private double compactness;
    private double eccentricity;
    private double elongation;
    private double simplicity;
    private int height;
    // moments
    private double greyArea; // moment00, sum of intensities of all pixels in the component
    private double moment01;
    private double moment10;
    private double moment11;
    private double moment02;
    private double moment20;
    // central moments
    private double mu11;
    private double mu20;
    private double mu02;
    
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int BB_x; // width of bounding box in x axis
    private int BB_y; // height of the bounding box in y axis
    
    
    public Params(Pixel pix) {
        this.greyArea = pix.getValue();
        this.moment10 = moments(1, 0, pix);
        this.moment01 = moments(0, 1, pix);
        this.moment11 = moments(1, 1, pix);
        this.moment02 = moments(0, 2, pix);
        this.moment20 = moments(2, 0, pix);
        
        this.minX = pix.getX();
        this.maxX = pix.getX();
        this.minY = pix.getY();
        this.maxY = pix.getY();
        
        area = 0;
        perimeter = 0.0;
        compactness = 0.0;
        eccentricity = 0.0;
        elongation = 0.0;
        simplicity = 0.0;
        height = pix.getValue();
    }
    
    public static double moments(int i, int j, Pixel p) {
        double value = Math.pow(p.getX(), i) * Math.pow(p.getY(), j) * p.getValue();
        return value;
    }
        
   public int getArea() {
        return area;
    }
    
    public double getPerim() {
        return perimeter;
    }
    
    public double getCompact() {
        return compactness;
    }
    
    public double getEccen() {
        return eccentricity;
    }
    
    public double getElongation() {
        return elongation;
    }
    
    public double getSimplicity() {
        return simplicity;
    }
    
    public int getHeight() {
        return height;
    }
    
    public double getCentroidX() {
        return centroid_x;
    }
    
    public double getCentroidY() {
        return centroid_y;
    }
    
    // moments
    public double getGreyArea() {
        return greyArea;
    }
    
    public double getMoment01() {
        return moment01;
    }
    
    public double getMoment10() {
        return moment10;
    }
    
    public double getMoment02() {
        return moment02;
    }
    
    public double getMoment20() {
        return moment20;
    }
    
    public double getMoment11() {
        return moment11;
    }
    
    public double getMu11() {
        return mu11;
    }
    
    public double getMu20() {
        return mu20;
    }
    
    public double getMu02() {
        return mu02;
    }
    
    public int getMinX() {
        return minX;
    }
    
    public int getMinY() {
        return minY;
    }
    
    public int getMaxX() {
        return maxX;
    }
    
    public int getMaxY() {
        return maxY;
    }
    
    public int getBBX() {
        return BB_x;
    }
    
    public int getBBY() {
        return BB_y;
    }
        
    // setters
    public void addToArea(int addition) {
        this.area += addition;
    }
    
    public void addToPerim(double perim) {
        this.perimeter += perim;
    }
        
    public void setCentroidX(double value) {
        this.centroid_x = value;
    }
    
    public void setCentroidY(double value) {
        this.centroid_y = value;
    }
    
    public void setCompactness(double comp) {
        this.compactness = comp;
    }
    
    public void setEccentricity(double value) {
        this.eccentricity = value;
    }
    
    public void setElongation(double value) {
        this.elongation = value;
    }
    
    public void setSimplicity(double value) {
        this.simplicity = value;
    }
    
    public void setHeight(int value) {
        this.height = value;
    }
    
    public void addToGreyArea(double value) {
        this.greyArea += value;
    }
    
    public void addToMoment10(double addition) {
        this.moment10 += addition;
    }
    
    public void addToMoment01(double addition) {
        this.moment01 += addition;
    }
    
    public void addToMoment11(double addition) {
        this.moment11 += addition;
    }
    
    public void addToMoment02(double addition) {
        this.moment02 += addition;
    }
    
    public void addToMoment20(double addition) {
        this.moment20 += addition;
    }
    
    public void addToMu11(double addition) {
        this.mu11 += addition;
    }
    
    public void addToMu20(double addition) {
        this.mu20 += addition;
    }
    
    public void addToMu02(double addition) {
        this.mu02 += addition;
    }
    
    public void setMinX(int minX) {
        this.minX = minX;
    }
    
    public void setMinY(int minY) {
        this.minY = minY;
    }
    
    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }
    
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
        
    public void updateBBInfo(int n_minX, int n_maxX, int n_minY, int n_maxY) {
        if (n_maxX > maxX) {
            maxX = n_maxX;
        } 
        if (n_minX < minX) {
            minX = n_minX;
        }

        if (n_maxY > maxY) {
            maxY = n_maxY;
        } 
        if (n_minY < minY) {
            minY = n_minY;
        }
        
        BB_x = maxX - minX + 1;
        BB_y = maxY - minY + 1;
    }
}

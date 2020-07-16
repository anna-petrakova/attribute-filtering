/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingattributes;

import treenodes.CanonicalNode;
import attributefiltering.Image2D;
import treenodes.TreeNode;

/**
 * Area attribute - number of pixels in a component.
 * @author Anna Petráková
 */
public class Area implements Attribute {
        
    public void setComponent(Image2D comp) {
        return;
    }

    public void compute(CanonicalNode canonical, TreeNode node) {
        canonical.getParams().addToArea(1);
    }

    public void computeAttribute(CanonicalNode canonical) {
        int area = canonical.getParams().getArea(); // result is directly stored in the parameter, no need to count anything else
        canonical.setAttribute(area);
    }
    
}

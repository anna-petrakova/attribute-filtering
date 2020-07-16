/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingattributes;

import attributefiltering.Image2D;
import treenodes.CanonicalNode;
import treenodes.TreeNode;

/**
 * Elongation computed by dividing the width and the length of the bounding box.
 * @author Anna Petráková
 */
public class Elongation implements Attribute {

    @Override
    public void setComponent(Image2D component) {
        return;
    }

    @Override
    public void compute(CanonicalNode canonical, TreeNode node) {
        return; // Bounding box is already computed, nothing else is needed
    }

    @Override
    public void computeAttribute(CanonicalNode canonical) {
        int widthBB = canonical.getParams().getBBX();
        int lengthBB = canonical.getParams().getBBY();
        
        double elongation;
        if (widthBB > lengthBB) {
            elongation = (double) widthBB / lengthBB;
        } else {
            elongation = (double) lengthBB / widthBB;
        }
        
        canonical.getParams().setElongation(elongation);
        canonical.setAttribute(elongation);
    }
    
}

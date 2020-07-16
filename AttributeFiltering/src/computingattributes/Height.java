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
 * Height of the tree node in the tree attribute, computed as the maximal level minus current level in the component.
 * @author Anna Petráková
 */
public class Height implements Attribute {

    @Override
    public void setComponent(Image2D component) {
       return;
    }

    @Override
    public void compute(CanonicalNode canonical, TreeNode node) {
        int value = node.getOrigPixel().getValue();
        int currentHeight = canonical.getParams().getHeight();
        canonical.getParams().setHeight(Math.max(value, currentHeight));
    }

    @Override
    public void computeAttribute(CanonicalNode canonical) {
        int currentLevel = canonical.getOrigPixel().getValue();
        int maxLevel = canonical.getParams().getHeight();
        
        int finalHeight = maxLevel - currentLevel;
        canonical.getParams().setHeight(finalHeight);
        canonical.setAttribute(finalHeight);
    }
    
}

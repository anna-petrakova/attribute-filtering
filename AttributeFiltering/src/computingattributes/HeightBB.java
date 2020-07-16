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
 * Height of the bounding box serving as an attribute.
 * @author Anna Petráková
 */
public class HeightBB implements Attribute {

    @Override
    public void setComponent(Image2D component) {
        return;
    }

    @Override
    public void compute(CanonicalNode canonical, TreeNode node) {
        return;
    }

    @Override
    public void computeAttribute(CanonicalNode canonical) {
        canonical.setAttribute(canonical.getParams().getBBY());
    }
    
}

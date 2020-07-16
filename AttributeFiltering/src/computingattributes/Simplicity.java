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
 * Simplicity attribute, area of the component divided by its perimeter.
 * @author Anna Petráková
 */
public class Simplicity implements Attribute {
    private Perimeter perim; 
    private Area area;
    
    public Simplicity() {
        perim = new Perimeter();
        area = new Area();
    }

    @Override
    public void setComponent(Image2D component) {
        perim.setComponent(component);
    }

    @Override
    public void compute(CanonicalNode canonical, TreeNode node) {
        perim.compute(canonical, node);
        area.compute(canonical, node);
    }

    @Override
    public void computeAttribute(CanonicalNode canonical) {
        double simplicity = canonical.getParams().getArea() / canonical.getParams().getPerim();
        canonical.getParams().setSimplicity(simplicity);
        canonical.setAttribute(simplicity);
    }
    
}

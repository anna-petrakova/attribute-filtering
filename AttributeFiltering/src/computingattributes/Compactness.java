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
 * Compactness attribute - perimeter of the component divided by 4*pi*area.
 * @author Anna Petráková
 */
public class Compactness implements Attribute {
    private Perimeter perim; 
    private Area area;
    
    public Compactness() {
        perim = new Perimeter();
        area = new Area();
    }

    public void setComponent(Image2D component) {
        perim.setComponent(component);
    }

    public void compute(CanonicalNode canonical, TreeNode node) {
        perim.compute(canonical, node);
        area.compute(canonical, node);
    }

    public void computeAttribute(CanonicalNode canonical) {
        double compactness = Math.pow(canonical.getParams().getPerim(), 2) / (4*Math.PI * canonical.getParams().getArea());
        canonical.getParams().setCompactness(compactness);
        canonical.setAttribute(compactness);
    }
    
}

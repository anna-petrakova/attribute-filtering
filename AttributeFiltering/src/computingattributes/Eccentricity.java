/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingattributes;

import treenodes.CanonicalNode;
import attributefiltering.Image2D;
import treenodes.Params;
import treenodes.TreeNode;

/**
 * Eccentricity attribute, computed with the help of central moments.
 * @author Anna Petráková
 */
public class Eccentricity implements Attribute {
    
    public void setComponent(Image2D component) {
        return;
    }

    public void compute(CanonicalNode canonical, TreeNode node) {
        Params parent_par = canonical.getParams();
        
        double centr_x = parent_par.getCentroidX();
        double centr_y = parent_par.getCentroidY();
        // compute central moments of node
        double mu11 = node.getOrigPixel().getValue() * (node.getOrigPixel().getX() - centr_x) * (node.getOrigPixel().getY() - centr_y);
        double mu20 = node.getOrigPixel().getValue() * Math.pow(node.getOrigPixel().getX() - centr_x, 2);
        double mu02 = node.getOrigPixel().getValue() * Math.pow(node.getOrigPixel().getY() - centr_y, 2);
        
        parent_par.addToMu11(mu11);
        parent_par.addToMu20(mu20);
        parent_par.addToMu02(mu02);
    }

    public void computeAttribute(CanonicalNode canonical) {
        double mu11 = canonical.getParams().getMu11();
        double mu20 = canonical.getParams().getMu20();
        double mu02 = canonical.getParams().getMu02();
        
        double ecc;
            double temp = Math.sqrt(Math.pow(mu20 - mu02, 2) + 4*mu11*mu11);
            if ((mu20 + mu02 - temp) == 0) {
                ecc = 0;
            } else {
                ecc = (mu20 + mu02 + temp) / (mu20 + mu02 - temp);
            }
        
        canonical.getParams().setEccentricity(ecc);
        canonical.setAttribute(ecc);
    }
    
}

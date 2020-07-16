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
 * Interface that represents attribute.
 * @author Anna Petráková
 */
public interface Attribute {
    
    /**
     * Sets component for given attribute, in case the attribute needs to
     * have the entire component for correct computation, if not, then function 
     * doesn't do anything. 
     * @param component entire component
     */
    public void setComponent(Image2D component);
    
    /**
     * Computes data for given tree node and sends it to canonical node. This function
     * is called for each node in the component.
     * @param canonical canonical node that represents component
     * @param node one node from this component
     */
    public void compute(CanonicalNode canonical, TreeNode node);
    
    /**
     * Computes final value of the attribute and sets the value of "attribute" in
     * the canonical node to this value. This function is called only once for each
     * canonical node and assumes that all neccessary data from all nodes in the component 
     * were already computed  via "compute" function.
     * @param canonical 
     */
    public void computeAttribute(CanonicalNode canonical);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

import attributefiltering.Pixel;

/**
 * Canonical node is a tree node with additional information, representing the entire component.
 * @author Anna Petráková
 */
public class CanonicalNode extends TreeNode {
    private int canonicalIndex;
    private boolean singleton;
    private double attribute;  // different attribute based on requested type
    private Params componentParams;
    
    public CanonicalNode(Pixel p, Pixel m) {
        super(p, m);
    }
    
    public CanonicalNode(TreeNode node) {
        super(node);
        this.canonicalIndex = -1;
        this.attribute = 0;
        this.componentParams = new Params(node.origPixel);
        this.singleton = false;
    }
    
    public double getAttribute() {
        return attribute;
    }
    
    public int getCanonicalIndex() {
        return canonicalIndex;
    }
    
    public Params getParams() {
        return componentParams;
    }
    
    public void setCanonicalIndex(int value) {
        this.canonicalIndex = value;
    }
    
    public boolean isSingleton() {
        return singleton;
    }
    
    public void setAttribute(double value) {
        this.attribute = value;
    }
    
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}

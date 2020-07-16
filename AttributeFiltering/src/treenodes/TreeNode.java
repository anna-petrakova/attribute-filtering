/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

import attributefiltering.Pixel;

/**
 * Represents basic attributes of each node in the tree.
 * @author Anna Petráková
 */
public class TreeNode {
    protected final Pixel origPixel; // the original pixel for which the TreeNode for created
    protected final Pixel newPixel; // pixel with same coordinates are origPixel, but new intensity after filtering
    private CanonicalNode parent;
    private Boolean isActive;
    private int indexInS; // index to the array of TreeNodes S, used in canonicalize
    private TreeNodeImpl impl;
    protected TreeNode tempParent; // parent used during MaxTree construction  
    
    
    public TreeNode(Pixel p) {
        this.origPixel = p;
        this.newPixel = new Pixel(p.getValue(), p.getX(), p.getY());
        
        this.tempParent = this;
        this.isActive = true;
        this.impl = new MTBTreeNode();
    }
    
    public TreeNode(Pixel p, Pixel m) {
        this.origPixel = p;
        this.newPixel = new Pixel(p.getValue(), p.getX(), p.getY());
        
        this.tempParent = this;
        this.isActive = true;
        this.impl = new DITreeNode(m);
    }
    
    public TreeNode(TreeNode node) {
        this.origPixel = node.getOrigPixel();
        this.newPixel = node.getNewPixel();
        this.tempParent = node.getTempParent();
        this.isActive = true;        
    }
    
    public CanonicalNode getParent() {
        return parent;
    }
    
    public TreeNode getTempParent() {
        return tempParent;
    }
    
    public Pixel getOrigPixel() {
        return origPixel;
    }
    
    public Pixel getNewPixel() {
        return newPixel;
    }
    
    public int getIndexInS() {
        return indexInS;
    }
    
     public TreeNodeImpl getImpl() {
        return impl;
    }
    
    public boolean isActive() {
        return isActive;
    }
 
    public void setParent(CanonicalNode parent) {
     this.parent = parent;
     this.tempParent = parent; // replace TreeNode used during construction with real parent
    }
    
    public void setTempParent(TreeNode parent) {
     this.tempParent = parent;   
    }
    
    public void setActive(Boolean value) {
        this.isActive = value;
    }    
    
    public void setIndexInS(int index) {
        this.indexInS = index;
    }
    
}

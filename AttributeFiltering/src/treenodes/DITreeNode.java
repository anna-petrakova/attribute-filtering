/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

import attributefiltering.Pixel;

/**
 * Class that represents specific attributes of max tree used during the construction 
 * of max tree using dual-input algorithm.
 * @author Anna Petráková
 */
public class DITreeNode extends TreeNodeImpl {
    private final Pixel maskPixel;
    
    public DITreeNode(Pixel m) {
        super();
        this.maskPixel = m;
    }
    
    public Pixel getMaskPixel() {
        return maskPixel;
    }
    
}

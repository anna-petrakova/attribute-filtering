/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

/**
 * Class that represents specific attributes of max tree used during the construction 
 * of max tree using algorithm by Berger.
 * @author Anna Petráková
 */
public class MTBTreeNode extends TreeNodeImpl {
    private int rank;
    
    public MTBTreeNode() {
        super();
        this.rank = 0;
    }
    
    public int getRank() {
        return rank;
    }
    
    public void incrementRank() {
        this.rank++;
    }
    
}

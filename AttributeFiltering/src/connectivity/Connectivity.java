/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import attributefiltering.Pixel;
import treenodes.TreeNode;

/**
 * Interface that represents connectvity.
 * @author Anna Petráková
 */
public interface Connectivity {
    
    /**
     * Number of neighbours for given connectivity.
     * @return number of neighbours
     */
    public int numberOfNeighb();
   
    /**
     * For given pixel p returns the neighbours of p in the array of tree nodes.
     * Neighbours are stored in given array. 
     * @param p specific pixel for which the neighbours are wanted
     * @param im array from which the neighbours of p are picked
     * @param neighb array in which the neighbours are stored
     */
    public void getNeighbours(Pixel p, TreeNode[] im, TreeNode[] neighb);
    
    /**
     * Return only the positions of neighbours of the TreeNode p.
     * @param p specific tree node for which the neighbours are wanted
     * @return array filled with indexes of p's neighbours
     */
    public int[] getNeighboursIndexes(TreeNode p);
    
}

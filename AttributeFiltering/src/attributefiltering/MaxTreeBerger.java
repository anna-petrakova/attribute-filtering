/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributefiltering;

import treenodes.*;
import connectivity.Connectivity;

/**
 * MaxTree constructed using algorithm provided by Berger.
 * @author Anna Petráková
 */
public class MaxTreeBerger extends MaxTree {
            
    public MaxTreeBerger(Image2D image, Connectivity conn) {
        super(image, conn);
    }
    
    @Override
    public void constructTree() {
        Image2D image = getImage();
        TreeNode[] S = getTree();
        Connectivity connectivity = getConnectivity();
        
        int pixelCount = image.getNumPixels();
        TreeNode[] zpar = new TreeNode[pixelCount]; // storing already processed TreeNodes for finding neighbours of the currently processed node
        TreeNode[] repr = new TreeNode[pixelCount];
        
        Pixel[] pixels = image.getValues();
        Pixel[] pixels_sorted = new Pixel[pixels.length];
        RadixSort.radixsort(pixels, pixels_sorted);
                                
        TreeNode[] parentSets = new TreeNode[pixelCount];
        TreeNode[] zparSets = new TreeNode[pixelCount];
        for (Pixel p : pixels) {
            int idx = index(p.getX(), p.getY(), image);
            parentSets[idx] = new TreeNode(p);
            zparSets[idx] = new TreeNode(p);
        }
        
        TreeNode[] neighb = new TreeNode[connectivity.numberOfNeighb() + 1];
        for (int i = pixels_sorted.length - 1; i >= 0; i--) {
            Pixel pix = pixels_sorted[i];	
            int idx = index(pix.getX(), pix.getY(), image);
            S[i] = parentSets[idx];
            S[i].setIndexInS(i);
		
            TreeNode s1 = parentSets[idx];
            TreeNode s2 = zparSets[idx];

            repr[idx] = s1;												
            zpar[idx] = s2;

            connectivity.getNeighbours(pix, zpar, neighb);	
            for (int j = 0; j < 8; j++)
            {
                TreeNode n = neighb[j];
                if (n == null) break;

                TreeNode n_rep = SetUnionFind.find(n); // get representant of zpar component
                if (s2.getParent() == n_rep) continue;
                // hang component under s1, to create hierarchy
                SetUnionFind.union_in_order(s1, repr[index(n_rep.getOrigPixel().getX(), n_rep.getOrigPixel().getY(), image)]);	
                
                TreeNode head = SetUnionFind.union_by_rank(s2, n_rep);	// union by rank in zpar
                repr[index(head.getOrigPixel().getX(), head.getOrigPixel().getY(), image)] = s1;							
            }
        }
        
        canonicalize();
    }
        
    private int index(int x, int y, Image2D image) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) return -1;
        return y * image.getWidth() + x;
    }
}

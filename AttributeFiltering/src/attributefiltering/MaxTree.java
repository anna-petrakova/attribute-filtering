/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributefiltering;

import computingattributes.ComputeAttribute;
import connectivity.Connectivity;
import treenodes.*;

/**
 * Common interface for MaxTree and its variant DualInput MaxTree.
 * @author Anna Petráková
 */
public abstract class MaxTree {
    private int canonicalCount;
    private final TreeNode[] S;
    private final Image2D image;
    private final Connectivity connectivity;
    
    public MaxTree(Image2D image, Connectivity conn) {
        this.S = new TreeNode[image.getNumPixels()];
        this.image = image;
        this.connectivity = conn;
    }
    
    /**
     * Constructs the max tree according to chosen algorithm and stores it in S.
     */
    public abstract void constructTree();
        
    public TreeNode[] getTree() {
        return S;
    }
    
    public int getCanonicalCount() {
        return canonicalCount;
    }
    
    protected Image2D getImage() {
        return image;
    }
    
    protected Connectivity getConnectivity() {
        return connectivity;
    }
        
    protected void setCanonicalCount(int count) {
        this.canonicalCount = count;
    }
    
    /**
     * Reconstruct the final image with values stored in new pixels.
     * @return filtered image
     */    
    public Image2D reconstructImage() {
	Image2D newImage = new Image2D(image.getWidth(), image.getHeight());
        if (!S[0].isActive()) return newImage;

	for (int i = 0; i < image.getNumPixels(); i++)
	{
            TreeNode p = S[i];
            newImage.setPixel(p.getNewPixel().getX(), p.getNewPixel().getY(), p.getNewPixel());
	}
        return newImage;
    }
    
    public void canonicalize() {
        int counter = 1;
        
        CanonicalNode root = new CanonicalNode(S[0]); //root is always cannonical
	root.setCanonicalIndex(0);
        root.setParent(root); // set root's parent to itself
        S[0] = root;
        
	for (int i = 1; i < image.getNumPixels(); i++)
	{
            TreeNode p = S[i];
            TreeNode q = p.getTempParent();
                        
            if (q.getNewPixel().getValue() == q.getTempParent().getNewPixel().getValue()) {
                    p.setTempParent(q.getTempParent());
            }
            
            if (p.getNewPixel().getValue() != q.getNewPixel().getValue()) {
                
                if (!(p instanceof CanonicalNode)) {
                    p = new CanonicalNode(p);
                }
                ((CanonicalNode) p).setCanonicalIndex(counter);
                counter++;
             
            } else {
                if (!(p instanceof LeafNode)) {
                    p = new LeafNode(p);
                }
            }
            int parentIndex = p.getTempParent().getIndexInS(); // parent's index in S
            p.setParent((CanonicalNode) S[parentIndex]); // parent was already processed and is always canonical            
            S[i] = p;
            if (p instanceof LeafNode) {
                ComputeAttribute.computeData((LeafNode) p); // compute some data for all non-canonical nodes of the tree
            }
	}
	
	setCanonicalCount(counter);
    }
}

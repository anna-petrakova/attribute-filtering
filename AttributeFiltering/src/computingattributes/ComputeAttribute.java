/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingattributes;

import connectivity.Connectivity;
import attributefiltering.Image2D;
import attributefiltering.Pixel;
import java.util.Stack;
import treenodes.*;

/**
 * Computes attribute for each component in the image and mask according to chosen connectivity. 
 * @author Anna Petráková
 */
public class ComputeAttribute {
   private TreeNode[] S;
    private TreeNode[] image;
    private Image2D mask;
    private Connectivity connectivity;
    private int height;
    private int width;
    private final Stack<TreeNode> stack;
    
    public ComputeAttribute(TreeNode[] set, Image2D mask, Connectivity conn) {
        this.S = set;
        this.mask = mask;
        this.connectivity = conn;
        this.width = mask.getWidth();
        this.height = mask.getHeight();
        this.stack = new Stack<TreeNode>();
    } 
    
    public void compute(Attribute attr) {
        this.image = new TreeNode[S.length];
        
        for (int i = S.length-1; i >= 0; i--) {
            TreeNode currentNode = S[i];
            Pixel p = S[i].getOrigPixel();
            image[index(p.getX(), p.getY())] = currentNode; // save node to its original place in image
            
            if (currentNode instanceof CanonicalNode) {
                CanonicalNode canonical = (CanonicalNode) currentNode;
                computeCentroid(canonical);
                Image2D component = extractComponent(canonical);
                attr.setComponent(component);
                
                if (component.getNumPixels() == 1) { // component contains only singleton node
                    attr.compute(canonical, canonical);
                    attr.computeAttribute(canonical);
                    updateParentInfo(canonical);
                    continue;
                }
                
                Params par = canonical.getParams();
                
                for (int k = par.getMinX(); k <= par.getMaxX(); k++) {
                    for (int l = par.getMinY(); l <= par.getMaxY(); l++) {
                        
                        if (component.getPixel(k, l) != null) {
                            attr.compute(canonical, image[index(k,l)]);
                            
                        }
                    }
                }
                attr.computeAttribute(canonical);
                updateParentInfo(canonical); // updates parent with info about the whole component rooted in currentNode
            }
        }
    }
    /**
     * Extracts component from its canonical node. 
     * @param canonical node representing the component
     * @return component stored in image, with pixels beloging to the component stored at their original positions
     */    
    public Image2D extractComponent(TreeNode canonical) {
        if (!(canonical instanceof CanonicalNode)) {
            canonical = canonical.getParent();
        }
        
        if ( ((CanonicalNode) canonical).isSingleton() ) {
            Image2D component = new Image2D(1, 1);
            component.setPixel(0, 0, canonical.getOrigPixel());
            return component;
        }
                
        Image2D component = new Image2D(width, height);
        boolean[] status = new boolean[width * height]; // whether the position in the image was already processed
        status[index(canonical.getOrigPixel().getX(), canonical.getOrigPixel().getY())] = true; // position of canonical node was processed
        stack.push(canonical);
        
        TreeNode node;
        while (!stack.isEmpty()) {
            node = stack.pop();
            processNode(canonical, node, component, status);
        }
        
        return component;
        
    }
    
    private void processNode(TreeNode canonical, TreeNode p,Image2D component, boolean[] status) {
        int componentLevel = canonical.getOrigPixel().getValue();
        
        int[] indexes = connectivity.getNeighboursIndexes(p);
                
        if (p.getOrigPixel().getValue() != -1) { // if p is a valid tree node, not a dummy node
            component.setPixel(p.getOrigPixel().getX(), p.getOrigPixel().getY(), p.getOrigPixel()); // store pixel from retrieved node p in image component 
        }
        
        for (int i = 0; i < 8; i++) {
            if (indexes[i] == -1) continue; // position doesnt exist
            if (!status[indexes[i]]) { //  position exists and was not yet processed
                status[indexes[i]] = true;
                 if (image[indexes[i]] != null) { // node from S was seen, belongs to this component and is stored in image 
                     stack.push(image[indexes[i]]);
                 } else { // deal with mask
                     if (mask.getPixel(indexes[i]).getValue() >= componentLevel) {
                         int[] position = position(indexes[i], width);
                         TreeNode dummyNode = new TreeNode(new Pixel(-1, position[0], position[1]));
                         stack.push(dummyNode);
                     }
                 }
            }
        }
    }
    
    /**
     * Compute information about bounding box and centroid and send this information to the parent node.
     * @param node leaf node that sends information about itself to the parent
     */
    public static void computeData(LeafNode node) {
        // info for BB
        Params parent_par = (node.getParent()).getParams();
        int node_x = node.getOrigPixel().getX();
        int node_y = node.getOrigPixel().getY();
        // sends parent only the node info
        parent_par.updateBBInfo(node_x, node_x, node_y, node_y);
        
        // centroid info
        parent_par.addToGreyArea(Params.moments(0, 0, node.getOrigPixel()));
        parent_par.addToMoment10(Params.moments(1, 0, node.getOrigPixel()));
        parent_par.addToMoment01(Params.moments(0, 1, node.getOrigPixel()));
        
    }
    
    /**
     * Centroid is computed during the computation of the attribute.
     * @param canonical node representing the component
     */
    public void computeCentroid(CanonicalNode canonical) {
        Params par = canonical.getParams();
        if (par.getGreyArea()!= 0) {
            par.setCentroidX(par.getMoment10() / par.getGreyArea());
            par.setCentroidY(par.getMoment01() / par.getGreyArea());
        } else {
            par.setCentroidX(canonical.getOrigPixel().getX());
            par.setCentroidY(canonical.getOrigPixel().getY());
        }        
    }
    
    /**
     * Update parent of some node with information about the bounding box and centroid (unless it is root node).
     * @param canonical node that sends information about the whole component to its parent
     */
    private void updateParentInfo(CanonicalNode canonical) {
        // send data from canonical node to parent - info about whole component, excluding root node
        if (canonical.getCanonicalIndex() != 0) {
            
            Params par = canonical.getParams();
            Params parent_par = (canonical.getParent()).getParams();
            // BB info
            int min_x = canonical.getParams().getMinX();
            int max_x = canonical.getParams().getMaxX();
            int min_y = canonical.getParams().getMinY();
            int max_y = canonical.getParams().getMaxY();
            
            parent_par.updateBBInfo(min_x, max_x, min_y, max_y);
            // centroid info 
            parent_par.addToGreyArea(par.getGreyArea());
            parent_par.addToMoment10(par.getMoment10());
            parent_par.addToMoment01(par.getMoment01());
        }
    } 
        
    private int index(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return -1;
        return y * width + x;
    }
        
    private int[] position(int idx, int width) {
        int[] pos = new int[2];
        pos[0] = idx % width; // x coordinate
        pos[1] = idx / width; // y coordinate
        return pos;
    }    
}

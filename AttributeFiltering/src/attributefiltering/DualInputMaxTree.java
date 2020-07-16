/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributefiltering;

import treenodes.*;
import connectivity.Connectivity;
import computingattributes.ComputeAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Construction of MaxTree from input image and mask image.
 * @author Anna Petráková
 */
public class DualInputMaxTree extends MaxTree {
    private Image2D mask;
    private int[] status;
    private int[] numberOfNodes; // number of nodes at given intensity level h
    private boolean[] nodeAtLevel; // indicator of an opened node at a given level h
    private Queue<TreeNode>[] HQueue;
    private ArrayList<TempNode>[] tree;
    private TreeNode[] treeNodes; 
    
        
    public DualInputMaxTree(Image2D image, Image2D mask, Connectivity conn) {
        super(image, conn);
        this.mask = mask;
        
        int max = getMaximumIntensityLevel(); // maximum intensity level in mask
        // each pixel in status has assigned either -1 for "Not analyzed", -2 for "In the queue" or the number of the TempNode it belongs to
        this.status = new int[image.getNumPixels()];
        this.numberOfNodes = new int[max+1]; // int value for each intensity level in mask, contains number of currently existing nodes at a given level
        this.nodeAtLevel = new boolean[max+1]; // boolean for each intensity level - true if there is a node flooded at a given level (active node)
        this.HQueue = new Queue[max+1]; // queue for each intensity level - contains already seen nodes that are going to be processed
        this.tree = new ArrayList[max+1]; // one arraylist for each intensity level - one arraylist contains all TempNodes at a given level
        this.treeNodes = new TreeNode[image.getNumPixels()]; 
        for (int i = 0; i < tree.length; i++)  {
            HQueue[i] = new LinkedList<TreeNode>();
            tree[i] = new ArrayList<TempNode>();
        }
        
        // Initialization
        Pixel[] pixels = image.getValues();
        Pixel[] maskPixels = mask.getValues();
        
        for (int i = 0; i < image.getNumPixels(); i++) {
            status[i] = -1; // set status of pixels to "Not Analyzed"
            treeNodes[i] = new TreeNode(pixels[i], maskPixels[i]);
        }
        
        for (int i = 0; i <= max; i++) {
            numberOfNodes[i] = 0; // number of nodes at each level is 0
            nodeAtLevel[i] = false; // these is no opened node at any level
        }       
    }
    
    @Override
    public void constructTree() {
        // retrieve root pixel and update attributes
        TreeNode hmin = getMinimum(); // find any node with minimal intensity
        DITreeNode hmin_impl = (DITreeNode) hmin.getImpl();
        Pixel maskPixel = hmin_impl.getMaskPixel();
        HQueue[maskPixel.getValue()].add(hmin); // add hmin to queue at its intensity level 
        status[idx(maskPixel)] = -2; // set status to "In the queue"
        
        int level = flood(maskPixel.getValue());
        while (level >= 0) {
            level = flood(level);
        }
                
        changeRepresentation(); 
    }
    
    /**
     * Determines the maximum intensity level - the highest value in the mask image
     * @return highest value in the mask
     */    
    private int getMaximumIntensityLevel() {
        Pixel[] m_values = mask.getValues();
        Pixel[] i_values = getImage().getValues();
        int max = 0;
        for (int i = 0; i < mask.getNumPixels(); i++) {
            if (m_values[i].getValue() > max) {
                max = m_values[i].getValue();
            }
            if (i_values[i].getValue() > max) {
                max = i_values[i].getValue();
            }
        }
        return max;
    }
    /**
     * Finds any TreeNode from array of treenodes that has the minimum value in the mask - is on the lowest intensity level 
     * @return any TreeNode that is representing a pixel with minimum value in the mask
     */
    private TreeNode getMinimum() {
        TreeNode min = treeNodes[0];
        DITreeNode min_impl = (DITreeNode) min.getImpl();
        
        for (int i = 1; i < treeNodes.length; i++) {
            DITreeNode node_impl = (DITreeNode) treeNodes[i].getImpl();
            if (node_impl.getMaskPixel().getValue() < min_impl.getMaskPixel().getValue()) {
                min = treeNodes[i];
            }            
        }
        return min;
    }
    /**
     * Floods given intensity level h.
     * @param h intensity level that is flooded
     * @return level that should be flooded next
     */
    private int flood(int h) {
        if (!nodeAtLevel[h]) { // since there is currently no node that is flooded at intensity level h, create one
            nodeAtLevel[h] = true;
            tree[h].add(new TempNode());
            numberOfNodes[h] += 1;
        }
        
        while (!HQueue[h].isEmpty()) {
            TreeNode p = HQueue[h].remove();
            DITreeNode p_impl = (DITreeNode) p.getImpl();
            int origValue = p.getOrigPixel().getValue(); // value of TreeNode in the original image
                       
            int k; // index of the currently flooded node at TreeNode's original level
            if (origValue != h) { // TreeNode p will be stored at a different level than the currently flooded one h
                if (!nodeAtLevel[origValue]) { // there is no currently flooded node at the level where p is going to be stored
                    nodeAtLevel[origValue] = true;
                    tree[origValue].add(new TempNode());
                    numberOfNodes[origValue] += 1;
                } 
                k = numberOfNodes[origValue]-1;
                tree[origValue].get(k).addNode(p); // add TreeNode p to currently flooded TempNode at p's original level
                
                if (origValue > h) { // value in the mask is lower that value in the original image, so TempNode to which p was added needs to be finalized
                    TempNode parentLevel = tree[h].get(numberOfNodes[h]-1); // current level h is the parental level, so currenty flooded TempNode is going to be parent
                    TempNode singletonLevel = tree[origValue].get(k); // TempNode to which p was added - contains only p and is a singleton
                    singletonLevel.setParent(parentLevel);
                    singletonLevel.setSingleton(true);
                    nodeAtLevel[origValue] = false;
                }
            } else { // value in the mask and in the original image is the same
            
                k = numberOfNodes[origValue]-1;
                tree[origValue].get(k).addNode(p); // add TreeNode p to k-th TempNode at p's original intensity level (same as p's mask level)
            }
            
            status[idx(p_impl.getMaskPixel())] = numberOfNodes[origValue]; // set p's value in the status to the number of the TempNode where p is stored
                        
            TreeNode[] neighb = new TreeNode[getConnectivity().numberOfNeighb() + 1];
            getConnectivity().getNeighbours(p_impl.getMaskPixel(), treeNodes, neighb); // find all p's neighbours based on chosen connectivity
            for (int j = 0; j < 8; j++) { // go through the neighbours
                TreeNode q = neighb[j];
                                
                if (q == null) break; // all p's neighbours were processed
                DITreeNode q_impl = (DITreeNode) q.getImpl();
                if (status[idx(q_impl.getMaskPixel())] == -1) { // status of the neighbour is "Not analyzed"
                    HQueue[q_impl.getMaskPixel().getValue()].add(q);
                    status[idx(q_impl.getMaskPixel())] = -2;
                    // if there is no node currently flooded at the mask level of the neighbouring pixel, create one
                    if (!nodeAtLevel[q_impl.getMaskPixel().getValue()]) { 
                        int neigh_level = q_impl.getMaskPixel().getValue(); // mask level of the neighbouring pixel
                        nodeAtLevel[neigh_level] = true;
                        tree[neigh_level].add(new TempNode());
                        numberOfNodes[neigh_level] += 1;
                    }
                    
                    if (q_impl.getMaskPixel().getValue() > p_impl.getMaskPixel().getValue()) { // neighbour has higher value than p so its level needs to be processed first
                        int l = q_impl.getMaskPixel().getValue();
                        while (l > h) { // continue flooding all levels higher than the current one
                            l = flood(l);
                        }
                    }
                }
                
            }
        }
        
        int m = h-1; // find parental level - one that is lower than the current level and has node that is still being flooded
        while (m >= 0 && nodeAtLevel[m] == false) { 
            m = m-1; 
        }
        if (m != -1) { // if current level is not the lowest level in the image, set parental relationship
            TempNode thisLevel = tree[h].get(numberOfNodes[h]-1);
            TempNode parent = tree[m].get(numberOfNodes[m]-1);
            thisLevel.setParent(parent);
        } else {
            TempNode thisLevel = tree[h].get(numberOfNodes[h]-1);
            thisLevel.setParent(thisLevel);
        }
        nodeAtLevel[h] = false;
        return m;
    }
   /**
    * Index of pixel p in the original image.
    * @param p pixel whose index we want to know
    * @return index of pixel p or -1 if the resides outside of the image domain
    */     
   private int idx(Pixel p) {
        int x = p.getX();
        int y = p.getY();
        Image2D image = getImage();
        
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) return -1;
        return y * image.getWidth() + x;
    }
   /**
    * Changes representation of the tree from array of arraylists of TempNodes created by DualInput algorithm 
    * to an array of Canonical and Leaf TreeNodes. Also sets the correct number of canonicalNodes.
    */
    private void changeRepresentation() {
        TreeNode[] S = getTree();
        int pos = 0; // position in the S array
        int counter = 0; // canonical index for canonical nodes - also counts the number of canonical nodes
        
        for (int i = 0; i < tree.length; i++) {
            ArrayList<TempNode> arr = tree[i];
            
            for (TempNode temp : arr) {
               
                CanonicalNode canonical = null;
                Set<TreeNode> set = temp.getSet();
                if (set.isEmpty()) {
                    temp.setCanonNode(temp.getParent().getCanonical());
                    continue;
                }
                
                for (TreeNode node : set) {
                    
                    if (canonical == null) { // currently processed TempNode (component) has no assigned canonical node
                        node = new CanonicalNode(node);
                        temp.setCanonNode((CanonicalNode) node);
                        canonical = (CanonicalNode) node;
                        canonical.setParent(temp.getParent().getCanonical()); // set node's parent to the canonical node of parental TempNode
                        canonical.setCanonicalIndex(counter);
                        counter++;
                        if (temp.isSingleton()) {
                            canonical.setSingleton(true);
                        }
                    } else {// curently processed component has a canonical node, so all other nodes in the TempNode must be leaves and canonical node's children
                        node = new LeafNode(node);
                        node.setParent(temp.getCanonical());
                        ComputeAttribute.computeData((LeafNode) node); // compute som data for all non-canonical nodes
                    }
                    
                    S[pos] = node; // place node into the S array
                    node.setIndexInS(pos); // set node's position in S array
                    pos++;
                }
            }
        }
        setCanonicalCount(counter);        
    }
    /**
     * Class that stores all TreeNodes that belong to one component in one TempNodein a set. 
     * Canonical node is assigned during the change of the representation as one of the nodes in the set (arbitrarily picked).
     */    
    private class TempNode {
        private Set<TreeNode> set;
        private CanonicalNode canonicalNode;
        private TempNode parent;
        private boolean singleton;
        
        public TempNode() {
            this.set = new HashSet<TreeNode>();
            this.canonicalNode = null;
        }
        
        public Set<TreeNode> getSet() {
            return set;
        }
        
        public CanonicalNode getCanonical() {
            return canonicalNode;
        }
        
        public TempNode getParent() {
            return parent;
        }
        
        public boolean isSingleton() {
            return singleton;
        }
        
        public void setCanonNode(CanonicalNode node) {
            this.canonicalNode = node;
        }
        
        public void setParent(TempNode parent) {
            this.parent = parent;
        } 
        
        public void setSingleton(boolean singleton) {
            this.singleton = singleton;
        }
        
        
        public void addNode(TreeNode node) {
            this.set.add(node);
        } 
    }
    
}

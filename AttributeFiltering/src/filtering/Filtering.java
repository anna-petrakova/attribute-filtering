/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filtering;

import treenodes.CanonicalNode;
import treenodes.TreeNode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing the implementation of different filtering rules.
 * @author Anna Petráková
 */
public class Filtering {
    private TreeNode[] S;
    private double lThreshold;
    private double uThreshold;
    
    public Filtering(TreeNode[] tree, double lowerThreshold, double upperThreshold) {
        this.S = tree;
        this.lThreshold = lowerThreshold;
        this.uThreshold = upperThreshold;
    }
    
    public void directFiltering() {
        for (int i = 0; i < S.length; i++) {
            if (!(S[i] instanceof CanonicalNode)) continue; // skip all non-canonical nodes and correct them later
            
            CanonicalNode canonical = (CanonicalNode) S[i];
            CanonicalNode parent = canonical.getParent();
            if (canonical.getAttribute() < lThreshold || canonical.getAttribute() > uThreshold) {
                canonical.setActive(false);
                canonical.getNewPixel().setValue(parent.getNewPixel().getValue());
            }
        }
        correctNodes(); // correct non-canonical nodes
    }
    
    public void subtractiveFiltering() {
        
        for (int i = 0; i < S.length; i++) {
            CanonicalNode parent = S[i].getParent();
            // direct filtering
            if ((S[i] instanceof CanonicalNode)) {
                CanonicalNode canonical = (CanonicalNode) S[i];
                
                if (canonical.getAttribute() < lThreshold || canonical.getAttribute() > uThreshold) {
                    S[i].setActive(false);
                    S[i].getNewPixel().setValue(parent.getNewPixel().getValue());
                }
            }
            // adjustment of grey levels of either active nodes or non-canonical nodes
            int diff = parent.getOrigPixel().getValue() - parent.getNewPixel().getValue();
            if ((S[i] instanceof CanonicalNode) & S[i].isActive() & diff != 0) {
                S[i].getNewPixel().setValue(S[i].getNewPixel().getValue() - diff);
            }
            if (!(S[i] instanceof CanonicalNode) & diff != 0) {
                S[i].getNewPixel().setValue(parent.getNewPixel().getValue());
                S[i].setActive(S[i].getParent().isActive());
            }
        }
    }
    
    public void kSubtractiveFiltering(int k, int canonCount) {
        int[] kprime = new int[canonCount];
        int[] peakComp = new int[canonCount];
        // set peak components
        for (int i = S.length - 1; i >= 0; i--) {
            if (S[i] instanceof CanonicalNode) {
                int nodeIndex = ((CanonicalNode)S[i]).getCanonicalIndex();
                int parentIndex = S[i].getParent().getCanonicalIndex();
                kprime[nodeIndex] = 0;
                if (S[i].getOrigPixel().getValue() > peakComp[nodeIndex]) {
                    peakComp[nodeIndex] = S[i].getOrigPixel().getValue();
                }
                if (peakComp[parentIndex] < peakComp[nodeIndex]) {
                    peakComp[parentIndex] = peakComp[nodeIndex];
                }
            }            
        }
        // process tree
        // process root first separately
        if (((CanonicalNode)S[0]).getAttribute() >= lThreshold && ((CanonicalNode) S[0]).getAttribute() <= uThreshold) {
            kprime[0] = k;
        } else {
            kprime[0] = 0;
            S[0].getNewPixel().setValue(0); // set value to 0 if bcg does not satisfy attr
        }
        // process all canonical nodes
        for (int i = 1; i < S.length; i++) {
            if (S[i] instanceof CanonicalNode) {
                CanonicalNode canonical = (CanonicalNode) S[i];
                CanonicalNode parent = S[i].getParent();
                int nodeIndex = canonical.getCanonicalIndex();
                int parentIndex = parent.getCanonicalIndex();
                
                int diff = canonical.getOrigPixel().getValue() - parent.getOrigPixel().getValue();
                
                if ((peakComp[nodeIndex] - parent.getOrigPixel().getValue()) > k & 
                        (canonical.getAttribute() >= lThreshold & canonical.getAttribute() <= uThreshold)) { 
                        // satisfy contrast criterion and attribute criterion 
                    canonical.getNewPixel().setValue(parent.getNewPixel().getValue() + diff);
                    kprime[nodeIndex] = k;
                } else {
                    if (diff > kprime[parentIndex]) {
                        canonical.getNewPixel().setValue(parent.getNewPixel().getValue() + kprime[parentIndex]);
                        kprime[nodeIndex] = 0;
                    } else {
                        canonical.getNewPixel().setValue(parent.getNewPixel().getValue() + diff);
                        kprime[nodeIndex] = kprime[parentIndex] - diff;
                    }
                }
                
            }
        }
        correctNodes();
        
    }
    
    public void minFiltering() {
        for (int i = 0; i < S.length; i++) {
            if (!(S[i] instanceof CanonicalNode)) continue;
            
            CanonicalNode canonical = (CanonicalNode) S[i];
            CanonicalNode parent = canonical.getParent();
            if (canonical.getAttribute() < lThreshold || canonical.getAttribute() > uThreshold || !parent.isActive()) {
                canonical.setActive(false);
                canonical.getNewPixel().setValue(parent.getNewPixel().getValue());
            }
        }
        correctNodes();
    }
    
    public void maxFiltering(int canonicalCount) {
        boolean[] preserve = new boolean[canonicalCount];
        Arrays.fill(preserve, false);
        
        for (int i = S.length - 1; i >=  0; i--) {
            if (!(S[i] instanceof CanonicalNode)) continue;
            
            CanonicalNode canonical = (CanonicalNode) S[i];
            CanonicalNode parent = canonical.getParent();
            
            if (preserve[canonical.getCanonicalIndex()]) { // if the node should be preserved because of one of his descendants, continue
                continue;
            } 
            
            if (canonical.getAttribute() >= lThreshold && canonical.getAttribute() <= uThreshold) { // if the node satisfies criterion, propagate this decision to the root
                while (!preserve[canonical.getCanonicalIndex()]) {
                    preserve[canonical.getCanonicalIndex()] = true;
                    parent = canonical.getParent();
                    if (parent == canonical) {
                        break;
                    }
                    canonical = canonical.getParent();                
                }                
            }   
        }
        
        for (int i = 0; i < S.length; i++) { // correct values in nodes
            if (!(S[i] instanceof CanonicalNode)) {
                S[i].getNewPixel().setValue(S[i].getParent().getNewPixel().getValue());
                S[i].setActive(S[i].getParent().isActive());
            } else {
                if (!preserve[((CanonicalNode) S[i]).getCanonicalIndex()]) {
                    S[i].getNewPixel().setValue(S[i].getParent().getNewPixel().getValue());
                    S[i].setActive(false);
                }
            }
        }
        
    }
    
    public void viterbiFiltering(int canonCount) {
        TrellisNode[] mainNodes = new TrellisNode[canonCount];
        
        int counter = 0;
        for (int i = 0; i < S.length; i++) {
            if (S[i] instanceof CanonicalNode) {
                TrellisNode newNode = new TrellisNode((CanonicalNode) S[i]);
                mainNodes[counter] = newNode;
                TrellisNode parent = mainNodes[S[i].getParent().getCanonicalIndex()];
                newNode.setParent(parent);
                counter++;
            }
        }
        
        for (int i = canonCount-1; i >= 0; i--) {
            TrellisNode node = mainNodes[i];
            int nodePreserveCost = 0;
            int nodeRemoveCost = 1;
            if (node.getSet().getAttribute() < lThreshold || node.getSet().getAttribute() > uThreshold) {
                nodePreserveCost = 1;
                nodeRemoveCost = 0;
            }
            node.decideCost(nodePreserveCost, nodeRemoveCost);
        }
        
        
        if (mainNodes[0].costRemove < mainNodes[0].costPreserve) {
            S[0].setActive(false);
            mainNodes[0].removeList.remove(mainNodes[0].removeList.size() - 1);
            setActiveNodes(mainNodes[0].removeList);
        } else {
            mainNodes[0].preserveList.remove(mainNodes[0].preserveList.size() - 1);
            setActiveNodes(mainNodes[0].preserveList);
        }
        // correct nodes
        correctNodes();
    }
    
    public void weightedViterbiFiltering(int canonCount) {
        TrellisNode[] mainNodes = new TrellisNode[canonCount];
        
        int counter = 0;
        for (int i = 0; i < S.length; i++) {
            if (S[i] instanceof CanonicalNode) {
                TrellisNode newNode = new TrellisNode((CanonicalNode) S[i]);
                mainNodes[counter] = newNode;
                TrellisNode parent = mainNodes[S[i].getParent().getCanonicalIndex()];
                newNode.setParent(parent);
                counter++;
            }
        }
        
        for (int i = canonCount-1; i >= 0; i--) {
            TrellisNode node = mainNodes[i];
            double attrValue = node.getSet().getAttribute();
            double nodePreserveCost, nodeRemoveCost;
            if (uThreshold != Double.POSITIVE_INFINITY) {
                nodePreserveCost = Math.max(lThreshold - attrValue, attrValue - uThreshold);
                nodeRemoveCost = Math.min(attrValue - lThreshold, uThreshold - attrValue);
            } else {
                nodePreserveCost = lThreshold - attrValue;
                nodeRemoveCost = attrValue - lThreshold;
            }
            node.decideCost(nodePreserveCost, nodeRemoveCost);
        }
        
        
        if (mainNodes[0].costRemove < mainNodes[0].costPreserve) {
            S[0].setActive(false);
            mainNodes[0].removeList.remove(mainNodes[0].removeList.size() - 1);
            setActiveNodes(mainNodes[0].removeList);
        } else {
            mainNodes[0].preserveList.remove(mainNodes[0].preserveList.size() - 1);
            setActiveNodes(mainNodes[0].preserveList);
        }
        // correct nodes
        correctNodes();
        
    }
    
    private void setActiveNodes(ArrayList<DecisionNode> listNodes) {
        for (DecisionNode n : listNodes) {
            if (!n.preserve) {
                n.node.set.setActive(false);
                int value = n.node.set.getParent().getNewPixel().getValue();
                n.node.set.getNewPixel().setValue(value);
                setActiveNodes(n.node.removeList);
            } else {
                setActiveNodes(n.node.preserveList);
            }
        } 
        
    }
    
    /**
     * Correct values of all leaf nodes according to new values of their parents.
     */
    private void correctNodes() {
        for (int i = 0; i < S.length; i++) {
            if (S[i] instanceof CanonicalNode) continue;
            
            CanonicalNode parent = S[i].getParent();
            int diff = parent.getNewPixel().getValue() - S[i].getNewPixel().getValue();
            if (diff != 0) {
                S[i].getNewPixel().setValue(parent.getNewPixel().getValue());
                S[i].setActive(parent.isActive());
            }
        }
    }
    
    /**
     * One node in trellis used during Viterbi filtering. Contains info about its 
     * possible state (preserve or remove), the costs of decisions to keep or remove
     * it, the list of all nodes it should preserve if it is preserved and the list of
     * all nodes it should remove if it is removed, the set it represents and parent.
     */
    private class TrellisNode {
        private DecisionNode preserve = new DecisionNode(true, this);
        private DecisionNode remove = new DecisionNode(false, this);
        private double costPreserve = 0;
        private double costRemove = 0;
        private ArrayList<DecisionNode> preserveList = new ArrayList();
        private ArrayList<DecisionNode> removeList = new ArrayList();
        private CanonicalNode set;
        private TrellisNode parent;
        
        public TrellisNode(CanonicalNode s) {
            this.set = s;
        }
        
        public CanonicalNode getSet() {
            return set;
        }
        
        public void setParent(TrellisNode parent) {
            this.parent = parent;
        }
        
        public void decideCost(double nodePreserveCost, double nodeRemoveCost) {
            
            costPreserve += nodePreserveCost;
            costRemove += nodeRemoveCost;
            
            if (costRemove < costPreserve) {
                parent.costPreserve += costRemove;
                parent.preserveList.add(remove);
            } else {
                parent.costPreserve += costPreserve;
                parent.preserveList.add(preserve);
            }
            
            parent.costRemove += costRemove;
            parent.removeList.add(remove);
        }
                
    }
    
    private class DecisionNode {
        private boolean preserve;
        private TrellisNode node;
        
        public DecisionNode(boolean keep, TrellisNode node) {
            this.preserve = keep;
            this.node = node;
        }
        
        public boolean isPreseved() {
            return preserve;
        }
    }
    
}

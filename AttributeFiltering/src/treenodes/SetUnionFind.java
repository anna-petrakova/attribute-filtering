/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treenodes;

/**
 * SetUnionFind structure.
 * @author Anna Petráková
 */
public class SetUnionFind {
    public static TreeNode find(TreeNode set) {
        if (set.getTempParent() == set) {
            return set;
        }
        set.setTempParent(find(set.getTempParent()));
        return set.getTempParent();
    }
    
    public static TreeNode union_by_rank(TreeNode firstSet, TreeNode secondSet) {
        TreeNode repre_first = find(firstSet);
	TreeNode repre_second = find(secondSet);
        
        MTBTreeNode impl_repre1 = (MTBTreeNode) repre_first.getImpl();
        MTBTreeNode impl_repre2 = (MTBTreeNode) repre_second.getImpl();

	if (repre_first == repre_second) {
		return repre_first;
	}

	if (impl_repre1.getRank() < impl_repre2.getRank()) {
		TreeNode tmp = repre_first;
		repre_first = repre_second;
		repre_second = tmp;
	}

	repre_second.setTempParent(repre_first);
	if (impl_repre1.getRank() == impl_repre2.getRank()) {
		impl_repre1.incrementRank();
	}

	return repre_first;
    }
    
    public static TreeNode union_in_order(TreeNode firstSet, TreeNode secondSet) {
        if (firstSet == secondSet)
	{
		return firstSet;
	}

	secondSet.setTempParent(firstSet);
	return firstSet;
    }
}

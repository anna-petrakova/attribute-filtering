/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingattributes;

import treenodes.CanonicalNode;
import attributefiltering.Image2D;
import treenodes.TreeNode;

/**
 * Perimeter of the component, computed as the number of edges separating the component from its complement.
 * @author Anna Petráková
 */
public class Perimeter implements Attribute {    
    private Image2D component;
     
    public void setComponent(Image2D component) {
        this.component = component;
    }

    public void compute(CanonicalNode canonical, TreeNode node) {
                
        int[] idx = neighbours(node);
        for (int j = 0; j < 4; j++) {
            if (idx[j] == -1 || component.getPixel(idx[j]) == null) {
                canonical.getParams().addToPerim(1);
            }
        }
    }

    public void computeAttribute(CanonicalNode canonical) {
        double perimeter = canonical.getParams().getPerim();
        canonical.setAttribute(perimeter);
    }
    
    private int[] neighbours(TreeNode p) {
        int x = p.getOrigPixel().getX();
        int y = p.getOrigPixel().getY();
        
	int i1 = index(x    , y - 1);
	int i3 = index(x + 1, y    );
        int i5 = index(x    , y + 1);
	int i7 = index(x - 1, y    );

	int[] indexes = new int[]{ i1, i3, i5, i7 };
	return indexes;
    }
    
    private int index(int x, int y) {
        if (x < 0 || x >= component.getWidth() || y < 0 || y >= component.getHeight()) return -1;
        return y * component.getWidth() + x;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import attributefiltering.Pixel;
import treenodes.TreeNode;

/**
 * 8-connectivity on 2D images.
 * @author Anna Petráková
 */
public class Connectivity8 implements Connectivity {
    private int width;
    private int height;
    
    public Connectivity8(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int index(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return -1;
        return y * width + x;
    }
    
    public int numberOfNeighb() {
        return 8;
    }
    
    public void getNeighbours(Pixel p, TreeNode[] nodes, TreeNode[] neighb) {
        int x = p.getX();
        int y = p.getY();
        
        int i0 = index(x - 1, y - 1);
	int i1 = index(x    , y - 1);
	int i2 = index(x + 1, y - 1);
	int i3 = index(x + 1, y    );
	int i4 = index(x + 1, y + 1);
	int i5 = index(x    , y + 1);
	int i6 = index(x - 1, y + 1);
	int i7 = index(x - 1, y    );

	int[] idx = new int[]{ i0, i1, i2, i3, i4, i5, i6, i7 };
	
	int pos = 0;
	for (int i = 0; i < 8; i++)
	{
            if (idx[i] == -1) continue;
            TreeNode n = nodes[idx[i]];
            if (n == null) continue;
            neighb[pos] = n;
            pos++;
	}
	neighb[pos] = null;
    }
    
    public int[] getNeighboursIndexes(TreeNode p) {
        int x = p.getOrigPixel().getX();
        int y = p.getOrigPixel().getY();
        
        int i0 = index(x - 1, y - 1);
	int i1 = index(x    , y - 1);
	int i2 = index(x + 1, y - 1);
	int i3 = index(x + 1, y    );
	int i4 = index(x + 1, y + 1);
	int i5 = index(x    , y + 1);
	int i6 = index(x - 1, y + 1);
	int i7 = index(x - 1, y    );

	int[] indexes = new int[]{ i0, i1, i2, i3, i4, i5, i6, i7 };
	return indexes;
    } 
    
    
}

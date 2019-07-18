package tetris;

import java.awt.Color;
import java.util.*;

/**
 * Class for Shapes
 */
class Shapes {

	Shape type;
	Color color;
	int[] pt;
	boolean[][] pos;

	static Set<Shape> unusedShapes = new HashSet<>(Arrays.asList(Shape.Shape_N1, Shape.Shape_N2, Shape.Shape_N3,
			Shape.Shape_N4, Shape.Shape_N5, Shape.Shape_N6, Shape.Shape_N7, Shape.Shape_N8));

	Shapes() {
		do {
			type = Shape.getRandomShape();
		} while (unusedShapes.contains(type));
		pos = type.getInitPos();
		pt = type.getInitPt();
		color = type.getColor();
	}

	public int[] getPt() {
		return Arrays.copyOf(pt, pt.length);
	}

	public void setPt(int[] pt) {
		this.pt = pt;
	}

	public boolean[][] getPos() {
		boolean[][] res = new boolean[pos.length][];
		for (int i = 0; i < pos.length; i++)
			res[i] = Arrays.copyOf(pos[i], pos[i].length);
		return res;
	}

	public void setPos(boolean[][] pos) {
		this.pos = pos;
	}

	public static int[][] getNextPos(int[] pt, boolean[][] pos) {
		int[][] np = new int[4][2];
		int idx = 0;
		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos[i].length; j++)
				if (pos[i][j]) {
					np[idx][0] = j + pt[0];
					np[idx++][1] = i + pt[1];
				}
		}
		int[][] res = new int[idx][2];
		for (int i = 0; i < idx; i++)
			res[i] = np[i];
		return res;
	}

	public int[][] getBlockPos() {
		return getNextPos(pt, pos);
	}
}

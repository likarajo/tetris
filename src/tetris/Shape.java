package tetris;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

public enum Shape {
	Shape_S(Color.yellow, new boolean[][] { { false, true, true }, { true, true, false }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_Z(new Color(90, 40, 130),
			new boolean[][] { { true, true, false }, { false, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_J(new Color(0, 90, 170),
			new boolean[][] { { true, false, false }, { true, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_L(Color.red, new boolean[][] { { false, false, true }, { true, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_Sq(Color.green,
			new boolean[][] { { false, true, true, false }, { false, true, true, false },
					{ false, false, false, false }, { false, false, false, false } },
			new int[] { 3, -2 }),
	Shape_T(new Color(255, 175, 0),
			new boolean[][] { { false, true, false }, { true, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_I(new Color(0, 160, 230),
			new boolean[][] { { false, false, false, false }, { true, true, true, true },
					{ false, false, false, false }, { false, false, false, false } },
			new int[] { 3, -2 }),
	Shape_N1(new Color(155, 155, 155),
			new boolean[][] { { false, true, false }, { true, true, false }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N2(new Color(135, 200, 70),
			new boolean[][] { { true, false, false }, { false, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N3(new Color(210, 140, 140),
			new boolean[][] { { false, false, false }, { true, true, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N4(new Color(225, 100, 15),
			new boolean[][] { { false, true, false }, { true, false, false }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N5(new Color(155, 200, 165),
			new boolean[][] { { false, true, false }, { false, true, false }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N6(new Color(135, 125, 75),
			new boolean[][] { { false, true, false }, { true, false, true }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N7(new Color(0, 0, 100),
			new boolean[][] { { false, false, false }, { false, true, false }, { false, false, false } },
			new int[] { 3, -2 }),
	Shape_N8(new Color(45, 120, 145),
			new boolean[][] { { false, false, true }, { false, true, false }, { true, false, false } },
			new int[] { 3, -2 });

	Color color;
	private boolean[][] initPos;
	private int[] initPt;

	Shape(Color color, boolean[][] initPos, int[] initPt) {
		this.color = color;
		this.initPos = initPos;
		this.initPt = initPt;
	}

	public Color getColor() {
		return color;
	}

	public boolean[][] getInitPos() {
		boolean[][] res = new boolean[initPos.length][];
		for (int i = 0; i < initPos.length; i++)
			res[i] = Arrays.copyOf(initPos[i], initPos[i].length);
		return res;
	}

	public int[] getInitPt() {
		return Arrays.copyOf(initPt, initPt.length);
	}

	public static Shape getRandomShape() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}

}

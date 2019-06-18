package tetris;

import java.awt.Color;

public class Shapes {

	enum AllShapes {
		Square, Line, Z, S, J, L, T 
	};

	private AllShapes currShape;
	private int shapeMatrix[][];
	private int[][][] shapeTable;

	public Shapes() {
		shapeMatrix = new int[4][2];
		setShape(AllShapes.S); // default
	}

	public void setShape(AllShapes shape) {
		shapeTable = new int[][][] {
				{ { -1, -1 }, { -1, 0 }, { 0, -1 }, { 0, 0 } }, 	// Square
				{ { -1, -1 }, { 0, -1 }, { 1, -1 }, { 2, -1 } },	// Line
				{ { -1, 0 }, { 0, 0 }, { 0, -1 }, { 1, -1 } },		// Z
				{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 1, 0 } },		// S
				{ { -1, -1 }, { -1, 0 }, { 0, -1 }, { 1, -1 } },	// J 
				{ { -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 } },		// L	
				{ { -1, -1 }, { 0, -1 }, { 1, -1 }, { 0, 0 } } 		// T				
		}; 
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; ++j) {
				shapeMatrix[i][j] = shapeTable[shape.ordinal()][i][j];
			}
		}	
		currShape = shape;
	}

	public void setX(int k, int x) {
		shapeMatrix[k][0] = x;
	}

	public void setY(int k, int y) {
		shapeMatrix[k][1] = y;
	}

	public int getX(int k) {
		return shapeMatrix[k][0];
	}

	public int getY(int k) {
		return shapeMatrix[k][1];
	}

	public AllShapes getShape() {
		return currShape;
	}

	public void selectRandom() {

		int x = (int) (50 * Math.random() % 6) + 1;
		AllShapes[] values = AllShapes.values();
		setShape(values[x]);
	}

	public Shapes rotateCCW() {
		if (currShape == AllShapes.Square)
			return this;

		Shapes result = new Shapes();
		result.currShape = currShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, 1 + getY(i));
			result.setY(i, -1 - getX(i));
		}
		return result;
	}

	public Shapes rotateCW() {
		if (currShape == AllShapes.Square)
			return this;

		Shapes result = new Shapes();
		result.currShape = currShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, -1 - getY(i));
			result.setY(i, getX(i) - 1);
		}
		return result;
	}

	public Color getColor() {
		Color color;
		switch (currShape.name()) {
		case "Square":
			color = Color.green;
			break;
		case "Line":
			color = Color.cyan;
			break;
		case "Z":
			color = new Color(102,0,204);
			break;
		case "S":
			color = Color.yellow;
			break;
		case "J":
			color = Color.blue;
			break;
		case "L":
			color = Color.red;
			break;
		case "T":
			color = Color.orange;
			break;
		default:
			color = Color.white;
		}
		return color;
	}
}
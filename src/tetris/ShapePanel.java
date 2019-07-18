package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

class ShapePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Shape shape;

	public ShapePanel(Shape shape) {
		this.shape = shape;
	}

	public Dimension getPreferredSize() {
		return new Dimension(Tetris.BLOCKSIZE * 3 + 1, Tetris.BLOCKSIZE * 3 + 1);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int[][] pos = Shapes.getNextPos(new int[] { 0, 0 }, shape.getInitPos());
		for (int i = 0; i < pos.length; i++) {
			int x = pos[i][0], y = pos[i][1];
			g.setColor(shape.color);
			g.fillRect(x * Tetris.BLOCKSIZE, y * Tetris.BLOCKSIZE, Tetris.BLOCKSIZE, Tetris.BLOCKSIZE);
			g.setColor(Color.black);
			g.drawRect(x * Tetris.BLOCKSIZE, y * Tetris.BLOCKSIZE, Tetris.BLOCKSIZE, Tetris.BLOCKSIZE);
		}
	}
}

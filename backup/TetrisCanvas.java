package tetris;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TetrisCanvas extends Canvas implements ActionListener {

	private static final long serialVersionUID = 1L; // Since serializabile

	// Speed of drop in Milliseconds
	private static int SPEED = 1000;

	Shapes currentShape;
	Shapes nextShape;

	// Play Area Size
	static int W = Setup.W;
	static int H = Setup.H;

	int[][] playAreaBlock = new int[W][H];
	Color[][] playAreaColor = new Color[W][H];

	int centerX, centerY;
	float pixelSize, rWidth = 600, rHeight = 600;

	// Starting position of shapes in play area
	private static final int INITX = W / 2;
	private static final int INITY = H;

	// current position of shapes in play area
	int currX, currY;

	int level = 1;
	int lines = 0;
	int linesCurr = 0;
	int score = 0;

	boolean hover = false;

	boolean touchBase = false;

	Timer timer = null;
	GameThread thread;

	int numLinesRemoved = 0;

	// scoring factor (range: 1-10)
	int M = Setup.M;

	// number of rows required for each Level of difficulty (range: 20-50)
	int N = Setup.N;

	// speed factor (range: 0.1-1.0)
	double S = Setup.S;

	// ************** Constructor ****************
	TetrisCanvas() {

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				boolean dispPause = hover;
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;
				if (mouseX > -250 && mouseX < 0 && mouseY > -250 && mouseY < 250) {
					hover = true;
					thread.stop();
				} else {
					hover = false;
					thread.start();
				}
				if (dispPause != hover)
					repaint();

			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;
				if (mouseX > 50 && mouseX < 150 && mouseY > -250 && mouseY < -210) {
					System.exit(1);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!hover) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (moveRight())
							currX--;
					} else if (SwingUtilities.isRightMouseButton(e)) {
						if (moveLeft())
							currX++;
					}
					repaint();

				}
			}
		});

		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (!hover) {
					if (e.getWheelRotation() > 0) {
						if (canRotate(1))
							currentShape = currentShape.rotateCW();
					}
					if (e.getWheelRotation() < 0) {
						if (canRotate(0))
							currentShape = currentShape.rotateCCW();
					}
					repaint();
				}
			}
		});

		currX = INITX;
		currY = INITY;

		thread = new GameThread(timer);
		thread.timer = new Timer(SPEED, this);
		thread.start();
	}
	// ***** End of Constructor *******

	int iX(float x) {
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y) {
		return Math.round(centerY - y / pixelSize);
	}

	void start() {
		currentShape = new Shapes();
		currentShape.selectRandom();

		nextShape = new Shapes();
		nextShape.selectRandom();

		thread.start();
	}

	void initgr(Graphics g) {
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;

		// Draw playing area
		g.drawRect(iX(-250), iY(250), Math.round(250 / pixelSize), Math.round(500 / pixelSize));

		// Draw nextShape area
		g.drawRect(iX(50), iY(250), Math.round(170 / pixelSize), Math.round(100 / pixelSize));

		// Draw QUIT button
		g.drawRect(iX(50), iY(-210), Math.round(100 / pixelSize), Math.round(40 / pixelSize));

		// strings
		g.setFont(new Font("Helvetica", Font.BOLD, (int) (20 / pixelSize)));
		g.drawString("Level:                        " + level, iX(50), iY(50));
		g.drawString("Lines:                        " + lines, iX(50), iY(0));
		g.drawString("Score:                       " + score, iX(50), iY(-50));
		g.drawString("QUIT", iX(75), iY(-235));
	}

	void drawPlayArea(Graphics g, int x, int y, Color c) {
		x = iX(-250 + 25 * x - 25);
		y = iY(-250 + 25 * y);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.black);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void drawNextArea(Graphics g, int x, int y, Color c) {
		x = iX(170 - 25 * 2 + 25 * x);
		y = iY(250 - 25 + 25 * y);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	boolean moveLeft() {
		for (int i = 0; i < 4; ++i) {
			int x = currX + currentShape.getX(i);
			int y = currY + currentShape.getY(i);
			if (x == 10 || playAreaBlock[x][y - 1] == 1 || y == 1 || playAreaBlock[x - 1][y - 2] == 1)
				return false;
		}
		return true;
	}

	boolean moveRight() {
		for (int i = 0; i < 4; ++i) {
			int x = currX + currentShape.getX(i);
			int y = currY + currentShape.getY(i);
			if (x == 1 || playAreaBlock[x - 2][y - 1] == 1 || y == 1 || playAreaBlock[x - 1][y - 2] == 1)
				return false;
		}
		return true;
	}

	public void addShape() {
		for (int i = 0; i < 4; ++i) {
			int x = currX + currentShape.getX(i);
			int y = currY + currentShape.getY(i);
			playAreaBlock[x - 1][y - 1] = 1;
			playAreaColor[x - 1][y - 1] = currentShape.getColor();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (touchBase) {
			touchBase = false;
			currentShape = nextShape;
			currX = INITX;
			currY = INITY;
			nextShape = new Shapes();
			nextShape.selectRandom();
		} else {
			currY--;
		}
		repaint();
	}

	public void fillPlayArea(Graphics g) {
		for (int x = 0; x < W; x++) {
			for (int y = 0; y < H; y++) {
				if (playAreaBlock[x][y] == 1)
					drawPlayArea(g, x + 1, y + 1, playAreaColor[x][y]);
			}
		}
	}

	public void fillNextArea(Graphics g) {
		for (int i = 0; i < 4; ++i) {
			int x = nextShape.getX(i);
			int y = nextShape.getY(i);
			drawNextArea(g, x, y, nextShape.getColor());
		}
	}

	public void fillWithShapes(Graphics g) {
		for (int i = 0; i < 4; ++i) {
			int x = currX + currentShape.getX(i);
			int y = currY + currentShape.getY(i);
			drawPlayArea(g, x, y, currentShape.getColor());
		}
	}

	public boolean canRotate(int rot) {
		Shapes changedShape;
		if (rot > 0) {
			changedShape = currentShape.rotateCW();
		} else {
			changedShape = currentShape.rotateCCW();
		}
		for (int i = 0; i < 4; ++i) {
			int x = currX + changedShape.getX(i);
			int y = currY + changedShape.getY(i);
			// check if it crosses margin or overlaps with existing blocks
			if (x < 1 || x > W || y < 1 || y > H || playAreaBlock[x - 1][y - 1] == 1)
				return false;
		}
		return true;
	}

	public void checkDrop() {
		for (int i = 0; i < 4; ++i) {
			int x = currX + currentShape.getX(i);
			int y = currY + currentShape.getY(i);
			if (y == 1) {
				touchBase = true;
				break;
			} else if (playAreaBlock[x - 1][y - 2] == 1) {
				if (y >= H - 1) {
					System.exit(0);
				}
				touchBase = true;
				break;
			}
		}
	}

	private void removeFullLines() {
		int numFullLines = 0;
		for (int i = playAreaBlock[0].length - 1; i >= 0; i--) {
			boolean lineIsFull = true;
			for (int j = 0; j < playAreaBlock.length; j++) {
				if (playAreaBlock[j][i] == 0) {
					lineIsFull = false;
				}
			}
			if (lineIsFull) {
				numFullLines++;
				for (int j = 0; j < playAreaBlock.length; j++) {
					for (int k = i; k < playAreaBlock[0].length - 1; k++) {
						playAreaBlock[j][k] = playAreaBlock[j][k + 1];
						playAreaColor[j][k] = playAreaColor[j][k + 1];
					}
				}
			}
		}
		if (numFullLines > 0) {
			lines += numFullLines;
			linesCurr += numFullLines;
			score = score + (level * M);
			if (linesCurr >= N) {
				level += 1;
				SPEED = (int) (SPEED * (1 + (level * S)));
			}
		}
	}

	public void paint(Graphics g) {
		initgr(g);
		fillNextArea(g);
		fillPlayArea(g);
		fillWithShapes(g);
		removeFullLines();
		checkDrop();
		if (touchBase) {
			addShape();
		}
		if (hover) {
			g.setColor(Color.blue);
			g.drawRect(iX(-125 - 60), iY(25), Math.round(120 / pixelSize), Math.round(50 / pixelSize));
			g.drawString("PAUSE", iX(-160), iY(-5));
			repaint();
		}
	}

}

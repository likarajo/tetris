package cg;

/** @author Rajarshi Chattopadhyay
* Squares.java: This program draws concentric squares rotated by 45 degrees to one another.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class Tetris extends Frame {
	public static void main(String[] args) {
		Frame f = new Frame("Tetris Game");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setSize(600, 600);
		InterfaceCanvas game = new InterfaceCanvas();
		game.init();
		f.add("Center", game);
		f.setVisible(true);

		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					game.rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.dropDown();
					game.score += 1;
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		// Make the falling piece drop every second
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						game.dropDown();
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

}

@SuppressWarnings("serial")
class InterfaceCanvas extends Canvas // implements MouseListener, MouseMotionListener
{
	int maxX, maxY;
	int centerX, centerY;
	int cx, cy;
	float pixelSize, rWidth = 600, rHeight = 600;
	int level = 1;
	int lines = 0;
	int score = 0;
	boolean hover = false;

	InterfaceCanvas() {
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;

				if (mouseX > -250 && mouseX < 0 && mouseY > -250 && mouseY < 250) {
					hover = true;
					repaint();
				} else {
					hover = false;
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// System.exit(1);
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;

				if (mouseX > 50 && mouseX < 150 && mouseY > -250 && mouseY < -210) {
					System.exit(1);

				}
			}
		});
	}

	int iX(float x) {
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y) {
		return Math.round(centerY - y / pixelSize);
	}

	private final Point[][][] Tetraminos = {
			// I-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },

			// J-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },

			// L-Piece
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) } },

			// O-Piece
			{ { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) } },

			// S-Piece
			{ { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },

			// T-Piece
			{ { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },

			// Z-Piece
			{ { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } } };

	private final Color[] tetraminoColors = { Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green,
			Color.pink, Color.red };

	private Point pieceOrigin;
	private int currentPiece;
	private int rotation;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

	private Color[][] well;

	// Creates a border around the well and initializes the dropping piece
	void init() {
		well = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || i == 11 || j == 22) {
					well[i][j] = Color.GRAY;
				} else {
					well[i][j] = Color.WHITE;
				}
			}
		}
		newPiece();
	}

	// Put a new, random piece into the dropping position
	public void newPiece() {
		pieceOrigin = new Point(5, 2);
		rotation = 0;
		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}

	// Collision test for the dropping piece
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.WHITE) {
				return true;
			}
		}
		return false;
	}

	// Rotate the piece clockwise or counterclockwise
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}

	// Move the piece left or right
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;
		}
		repaint();
	}

	// Drops the piece one line or fixes it to the well if it can't drop
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			fixToWell();
		}
		repaint();
	}

	// Make the dropping piece part of the well, so it is available for
	// collision detection.
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}

	public void deleteRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				well[i][j + 1] = well[i][j];
			}
		}
	}

	// Clear completed rows from the field and award score according to
	// the number of simultaneously cleared rows.
	public void clearRows() {
		boolean gap;
		int numClears = 0;

		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (well[i][j] == Color.WHITE) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				numClears += 1;
			}
		}

		switch (numClears) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 300;
			break;
		case 3:
			score += 500;
			break;
		case 4:
			score += 800;
			break;
		}
	}

	// Draw the falling piece
	private void drawPiece(Graphics g) {
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26, (p.y + pieceOrigin.y) * 26, 25, 25);
		}
	}

	void initgr() {
		Dimension d = getSize();
		maxX = d.width - 1;
		maxY = d.height - 1;
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;
	}

	void drawbaselayer(Graphics g) {
		// playing area
		g.drawRect(iX(-250), iY(250), Math.round(250 / pixelSize), Math.round(500 / pixelSize));

		// next shape area
		g.drawRect(iX(50), iY(250), Math.round(170 / pixelSize), Math.round(100 / pixelSize));

		// quit button
		g.drawRect(iX(50), iY(-210), Math.round(100 / pixelSize), Math.round(40 / pixelSize));

		// strings
		g.setFont(new Font("Helvetica", Font.BOLD, (int) (20 / pixelSize)));
		g.drawString("Level:                        " + level, iX(50), iY(50));
		g.drawString("Lines:                        " + lines, iX(50), iY(0));
		g.drawString("Score:                       " + score, iX(50), iY(-50));
		g.drawString("QUIT", iX(75), iY(-235));
	}

	void mainlayer(Graphics g, int x, int y, Color c) {
		x = iX(-100 + 25 * x + 25);
		y = iY(-225 + 25 * y + 25);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void nextlayer(Graphics g, int x, int y, Color c) {
		x = iX(170 - 25 * 2 + 25 * x);
		y = iY(250 - 25 + 25 * y);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void currentlayer(Graphics g, int x, int y, Color c) {
		x = iX(-125 + 25 * x);
		y = iY(125 + 25 * y + 25);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void fillmain(Graphics g) {
		int x = -1;
		int y = -1;
		mainlayer(g, x, y, Color.YELLOW);
		x = 0;
		y = -1;
		mainlayer(g, x, y, Color.YELLOW);
		x = 0;
		y = 0;
		mainlayer(g, x, y, Color.YELLOW);
		x = 1;
		y = 0;
		mainlayer(g, x, y, Color.YELLOW);
		x = 1;
		y = -1;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = -1;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = 0;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = 1;
		mainlayer(g, x, y, Color.BLUE);
	}

	void fillnext(Graphics g) {
		int x = -1;
		int y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 0;
		y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 1;
		y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 1;
		y = 0;
		nextlayer(g, x, y, Color.RED);
	}

	void fillcurrent(Graphics g) {
		int x = -1;
		int y = -1;
		currentlayer(g, x, y, Color.GREEN);
		x = -1;
		y = 0;
		currentlayer(g, x, y, Color.GREEN);
		x = 0;
		y = -1;
		currentlayer(g, x, y, Color.GREEN);
		x = 0;
		y = 0;
		currentlayer(g, x, y, Color.GREEN);

	}

	void fillWell(Graphics g) {
		// Paint the well
		g.fillRect(0, 0, 26 * 12, 26 * 23);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(well[i][j]);
				g.fillRect(26 * i, 26 * j, 25, 25);
			}
		}
	}

	public void paint(Graphics g) {

		initgr();
		drawbaselayer(g);

		// fillmain(g);
		fillWell(g);

		fillnext(g);

		// Draw the currently falling piece
		// fillcurrent(g);
		drawPiece(g);

		if (hover) {
			g.setColor(Color.blue);
			g.drawRect(iX(-125 - 60), iY(25), Math.round(120 / pixelSize), Math.round(50 / pixelSize));
			g.drawString("PAUSE", iX(-160), iY(-5));
			repaint();
		}

	}

}

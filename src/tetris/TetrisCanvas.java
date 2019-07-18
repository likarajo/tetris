package tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

class TetrisCanvas extends Canvas {

	private static final long serialVersionUID = 1L;
	
	int originX = 0, originY = 0;
	float pixelSize, rWidth, rHeight, blockSize, margin;
	Font textFont;
	Component mainArea, nextShapeArea, scoreArea, setupButton, quitButton, pauseLabel;
	List<Component> currShapeBlocks = new ArrayList<>();
	boolean changeShape = false;

	TetrisCanvas() {
		mainArea = new Component();
		nextShapeArea = new Component();
		scoreArea = new Component();
		setupButton = new Component();
		quitButton = new Component();
		pauseLabel = new Component();
		pauseLabel.hidden = true;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON1) {
					// On left click
					if (quitButton.inside(fX(e.getX()), fY(e.getY())) && quitButton != null) {
						// Clicked on Quit button
						System.exit(0);
					} else if (setupButton.inside(fX(e.getX()), fY(e.getY())) && setupButton != null) {
						// Clicked on Setup button
						Tetris.getInstance().handleTimer(true); // suspend timer
						Tetris.getInstance().setup(); // display setup window
					} else if (Tetris.getInstance().isGamePlaying && !Tetris.getInstance().isGamePaused) {
						// Game is playing and Not Paused
						Tetris.getInstance().moveOrChange(Action.MoveLeft); // move shape left
						repaint();
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// On right click
					if (Tetris.getInstance().isGamePlaying && !Tetris.getInstance().isGamePaused) {
						// Game is playing and Not Paused
						Tetris.getInstance().moveOrChange(Action.MoveRight); // move shape right
						repaint();
					}
				}
			}
		});

		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				super.mouseWheelMoved(e);
				if (!Tetris.getInstance().isGamePlaying)
					// Game is Not playing
					return;
				if (Tetris.getInstance().isGamePaused)
					// Game is paused
					return;

				if (e.getWheelRotation() < 0) {
					// Mouse wheel UP
					if (Tetris.getInstance().moveOrChange(Action.RotateLeft))
						repaint();

				} else {
					// Mouse wheel DOWN
					if (Tetris.getInstance().moveOrChange(Action.RotateRight))
						repaint();
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				if (!Tetris.getInstance().isGamePlaying)
					// Game is Not playing
					return;

				// Display or hide Pause label
				boolean displayPause = mainArea.inside(fX(e.getX()), fY(e.getY()));
				if (displayPause == pauseLabel.hidden) {
					pauseLabel.hidden = !displayPause;
					Tetris.getInstance().handleTimer(displayPause);
					repaint();
				}

				// Keep or change Current shape
				boolean shapeSet = true;
				for (Component block : currShapeBlocks) {
					if (block.inside(fX(e.getX()), fY(e.getY()))) {
						shapeSet = false;
						if (!changeShape) {
							if (Tetris.getInstance().moveOrChange(Action.Change))
								repaint();
							changeShape = true;
							break;
						}
					}
				}
				if (shapeSet)
					changeShape = false;

			}
		});
	}

	void initgr() {
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		rWidth = (float) Tetris.BLOCKSIZE * (Tetris.MAINAREA_WIDTH + 5) + 3 * 10;
		rHeight = (float) Tetris.BLOCKSIZE * Tetris.MAINAREA_HEIGHT + 10 * 2;
		margin = fL(10);
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		blockSize = fL(Tetris.BLOCKSIZE);
		textFont = new Font("Helvetica", Font.BOLD, iL(16 / pixelSize));
	}

	int iX(float x) {
		return Math.round(originX + x / pixelSize);
	}

	int iY(float y) {
		return Math.round(originY + y / pixelSize);
	}

	int iL(float l) {
		return Math.round(l / pixelSize);
	}

	float fX(int x) {
		return (x - originX) * pixelSize;
	}

	float fY(int y) {
		return (y - originY) * pixelSize;
	}

	float fL(int L) {
		return (float) L;
	}

	void drawComponent(Graphics g, Component c) {
		if (!c.hidden) {
			if (c.fillColor != null) {
				g.setColor(c.fillColor);
				g.fillRect(iX(c.x), iY(c.y), iL(c.w), iL(c.h));
			}
			if (c.borderColor != null) {
				g.setColor(c.borderColor);
				g.drawRect(iX(c.x), iY(c.y), iL(c.w), iL(c.h));
			}
			if (c.text != null) {
				FontMetrics metrics = g.getFontMetrics(g.getFont());
				float x = c.x + (c.w - metrics.stringWidth(c.text)) / 2;
				float y = c.y + ((c.h - metrics.getHeight()) / 2) + metrics.getAscent();
				g.drawString(c.text, iX(x), iY(y));
			}
		}
		for (Component sc : c.subComponents)
			drawComponent(g, sc);
	}

	void drawMainArea(Graphics g) {

		mainArea.x = margin;
		mainArea.y = margin;
		mainArea.w = Tetris.MAINAREA_WIDTH * blockSize;
		mainArea.h = Tetris.MAINAREA_HEIGHT * blockSize;
		mainArea.subComponents.clear();

		Shapes currShape = Tetris.getInstance().getCurrTetrisShape();
		currShapeBlocks.clear();
		if (currShape != null) {
			int[][] currBlocks = currShape.getBlockPos();
			for (int i = 0; i < currBlocks.length; i++) {
				int x = currBlocks[i][0];
				int y = currBlocks[i][1];
				if (Tetris.getInstance().inMainArea(x, y)) {
					Component block = new Component(mainArea.relativeX((float) x * blockSize),
							mainArea.relativeY((float) y * blockSize), blockSize, blockSize);
					block.fillColor = currShape.color;
					currShapeBlocks.add(block);
					mainArea.addSubComponent(block);
				}
			}
		}

		Color[][] mainAreaColor = Tetris.getInstance().getMainAreaColor();
		for (int x = 0; x < mainAreaColor.length; x++) {
			for (int y = 0; y < mainAreaColor[x].length; y++) {
				if (mainAreaColor[x][y] != null) {
					Component block = new Component(mainArea.relativeX((float) x * blockSize),
							mainArea.relativeY((float) y * blockSize), blockSize, blockSize);
					block.fillColor = mainAreaColor[x][y];
					mainArea.addSubComponent(block);
				}
			}
		}

		pauseLabel.text = "PAUSE";
		pauseLabel.textColor = Color.blue;
		pauseLabel.borderColor = Color.blue;
		pauseLabel.setCenter(mainArea.centerX(), mainArea.centerY());
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		pauseLabel.w = metrics.stringWidth(pauseLabel.text) + margin * 2;
		pauseLabel.h = metrics.getHeight() + margin * 2;
		
		mainArea.addSubComponent(pauseLabel);

		if (Tetris.getInstance().isGameOver) {
			System.exit(0);
		}

		drawComponent(g, mainArea);
	}

	void drawNextShapeArea(Graphics g) {

		nextShapeArea.x = margin + mainArea.x + mainArea.w;
		nextShapeArea.y = mainArea.y;
		nextShapeArea.w = 5 * blockSize;
		nextShapeArea.h = 4 * blockSize;
		nextShapeArea.subComponents.clear();

		Component blocks = new Component(0, 0, 4 * blockSize, 2 * blockSize);
		blocks.borderColor = null;
		blocks.setCenter(nextShapeArea.centerX(), nextShapeArea.centerY());

		Shapes nextShape = Tetris.getInstance().getNextTetrisShape();
		if (nextShape != null) {
			int[][] nextShapePos = Shapes.getNextPos(new int[] { 0, 1 }, nextShape.getPos());
			Color color = nextShape.color;
			for (int i = 0; i < nextShapePos.length; i++) {
				int x = nextShapePos[i][0], y = nextShapePos[i][1] - 1;
				Component block = new Component(blocks.relativeX((float) x * blockSize),
						blocks.relativeY((float) y * blockSize), blockSize, blockSize);
				block.fillColor = color;
				blocks.addSubComponent(block);
			}
			nextShapeArea.addSubComponent(blocks);
		}

		drawComponent(g, nextShapeArea);
	}

	void drawScoreArea(Graphics g) {
		int fontHeight = g.getFontMetrics(g.getFont()).getHeight();
		int X = iX(nextShapeArea.x);
		int Y = iY(nextShapeArea.y + nextShapeArea.h);
		Y += fontHeight + margin;
		g.drawString(String.format("Level: %d", Tetris.LEVEL), X, Y);
		Y += fontHeight + margin;
		g.drawString(String.format("Lines: %d", Tetris.LINE), X, Y);
		Y += fontHeight + margin;
		g.drawString(String.format("Score: %d", Tetris.SCORE), X, Y);
	}

	void drawButtons(Graphics g) {

		quitButton.text = "QUIT";
		quitButton.textColor = Color.black;
		quitButton.borderColor = Color.black;
		quitButton.x = nextShapeArea.x;
		quitButton.y = rHeight - margin - quitButton.h;
		FontMetrics qmetrics = g.getFontMetrics(g.getFont());
		quitButton.w = qmetrics.stringWidth(quitButton.text) + margin * 2;
		quitButton.h = qmetrics.getHeight() + margin * 2;

		drawComponent(g, quitButton);

		setupButton.text = "SETUP";
		setupButton.textColor = Color.black;
		setupButton.borderColor = Color.black;
		setupButton.x = nextShapeArea.x;
		setupButton.y = quitButton.y - margin - setupButton.h;
		FontMetrics smetrics = g.getFontMetrics(g.getFont());
		setupButton.w = smetrics.stringWidth(setupButton.text) + margin * 2;
		setupButton.h = smetrics.getHeight() + margin * 2;

		drawComponent(g, setupButton);

	}

	public void paint(Graphics g) {
		initgr();
		g.setFont(textFont);
		drawMainArea(g);
		drawNextShapeArea(g);
		drawScoreArea(g);
		drawButtons(g);
	}
}

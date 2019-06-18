package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Engine extends JFrame {

	private int pixelSize = 20;
	private int newWidth, newHeight, actualHeight;
	private int playAreaWidth = 10, playAreaHeight = 20;
	public static int fallingSpeed = 100;
	public boolean isGG = false;
	public Timer timer = new Timer(fallingSpeed, null);

	private GameData gameData = new GameData(playAreaWidth, playAreaHeight);

	// components on JFrame
	private NextArea nextArea = new NextArea(gameData, pixelSize, playAreaWidth, playAreaHeight);
	private DataArea dataArea = new DataArea(gameData);
	private PlayArea playArea = new PlayArea(gameData, timer, pixelSize, playAreaWidth, playAreaHeight, nextArea,
			dataArea);

	private GameFunction gameFunction = new GameFunction(gameData, playArea, nextArea, timer, fallingSpeed, isGG);

	public Engine() {
		this.setTitle("Tetris Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(440, 480);
		this.setLayout(null);

		nextArea.setBorder(BorderFactory.createLineBorder(Color.black));
		nextArea.setLayout(null);
		nextArea.setVisible(true);

		dataArea.setLayout(null);
		dataArea.setVisible(true);

		playArea.setBorder(BorderFactory.createLineBorder(Color.black));
		playArea.setLayout(null);
		playArea.setVisible(true);

		// monitor change of size of JFrame
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {

				// get new width and height
				newWidth = getWidth();
				newHeight = getHeight();
				Insets spaces = Engine.this.getInsets();
				actualHeight = newHeight - spaces.top;

				// pixel size based on the shorter dimension
				if (newHeight < newWidth) {
					pixelSize = (int) (Math.ceil(newHeight * 1.0 / (playAreaHeight + 4)));
				} else {
					pixelSize = (int) (Math.ceil(newWidth * 1.0 / (playAreaWidth + 13)));
				}

				playArea.blockSize = pixelSize;
				nextArea.blockSize = pixelSize;

				// dimension of the areas based on pixel size
				playArea.setBounds(2 * pixelSize, (actualHeight - playAreaHeight * pixelSize) >> 1, playAreaWidth * pixelSize,
						playAreaHeight * pixelSize);
				nextArea.setBounds(playArea.getX() + playArea.getWidth() + 2 * pixelSize,
						(actualHeight - playAreaHeight * pixelSize) >> 1, 7 * pixelSize, 4 * pixelSize);
				dataArea.setBounds(playArea.getX() + playArea.getWidth() + 2 * pixelSize,
						nextArea.getY() + nextArea.getHeight() + 2 * pixelSize, 8 * pixelSize, 14 * pixelSize);
			}
		});

		add(playArea);
		add(nextArea);
		add(dataArea);

		// move shape left and right
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					gameFunction.move(-1, 0);
					repaint();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					gameFunction.move(1, 0);
					repaint();
				}
			}
		});

		// rotate shape CW and CCW
		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int rot = e.getWheelRotation();
				if (rot == -1) {
					gameFunction.rotateClockwise();
					repaint();
				}
				if (rot == 1) {
					gameFunction.rotateCounterClockwise();
					repaint();
				}
			}
		});

		// start drop
		timer.start();
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameFunction.falling();
				playArea.repaint();
				nextArea.repaint();
				dataArea.repaint();
			}
		});

		this.setVisible(true);
	}

	public void gamePause() {
		timer.stop();
		gameData.setCanRotate(false);
		playArea.setPause(true);
		playArea.repaint();
	}

}

@SuppressWarnings("serial")
class PlayArea extends JPanel {

	private GameData gameData;
	public Timer timer;
	public boolean isPause;
	public boolean isGG;
	public int blockSize;
	public Draw curDraw;
	public int mapW, mapH;

	public PlayArea(GameData gameData, Timer timer, int blockSize, int mapW, int mapH, NextArea nextArea,
			DataArea dataArea) {
		this.gameData = gameData;
		this.timer = timer;
		this.blockSize = blockSize;
		this.mapW = mapW;
		this.mapH = mapH;

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isGG) {
					timer.stop();
					isPause = true;
					setRotate();
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isGG) {
					timer.start();
					isPause = false;
					setRotate();
					repaint();
				}
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int width = this.getWidth();
		int height = this.getHeight();
		curDraw = gameData.getCurDraw();
		Point[][] points = curDraw.getPoints();

		if (isPause) {
			g.setColor(Color.BLUE);
			if (this.getWidth() < this.getHeight()) {
				g.setFont(new Font("pause", Font.BOLD, (int) (width * 0.15)));
				g.drawRect((int) (width - width * 0.6) >> 1, (int) (height - height * 0.2) >> 1, (int) (width * 0.6),
						(int) (height * 0.15));
				g.drawString("PAUSE", (int) (width - width * 0.5) >> 1, (int) (height - height * 0.01) >> 1);
			} else {
				g.setFont(new Font("pause", Font.BOLD, (int) (height * 0.15)));
				g.drawRect((int) (width - height * 0.6) >> 1, (int) (height - height * 0.33) >> 1, (int) (height * 0.6),
						(int) (height * 0.3));
				g.drawString("PAUSE", (int) (width - height * 0.5) >> 1, 7 * height / 13);
			}
		}

		for (Point p : points[curDraw.getPosNum()]) {
			g.setColor(curDraw.getColor());
			g.fillRect((curDraw.initPos + p.x) * blockSize, blockSize * p.y, blockSize, blockSize);
			g.setColor(Color.BLACK);
			g.drawRect((curDraw.initPos + p.x) * blockSize, blockSize * p.y, blockSize, blockSize);
		}

		boolean[][] map = gameData.playMap;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				if (map[x][y]) {
					g.setColor(gameData.getColorMap()[x][y]);
					g.fillRect(blockSize * x, blockSize * y, blockSize, blockSize);
					g.setColor(Color.BLACK);
					g.drawRect(blockSize * x, blockSize * y, blockSize, blockSize);
				}
			}
		}
	}

	public void setMapW(int mapW) {
		this.mapW = mapW;
	}

	public void setMapH(int mapH) {
		this.mapH = mapH;
	}

	public GameData getGameData() {
		return gameData;
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	public void setRotate() {
		if (isPause) {
			this.getGameData().setCanRotate(false);
		} else {
			this.getGameData().setCanRotate(true);
		}
	}

	public void setPause(boolean pause) {
		isPause = pause;
	}

}

@SuppressWarnings("serial")
class DataArea extends JPanel {

	public GameData gameData;
	private int width;
	private int height;
	private JLabel level, lines, score;

	public DataArea(GameData gameData) {
		this.gameData = gameData;

		level = new JLabel("Level:  " + gameData.getCurLevel());
		lines = new JLabel("Lines:  " + gameData.getCurLines());
		score = new JLabel("Score:  " + gameData.getCurScore());
		JButton quitButton = new JButton("Quit");

		// monitor change of size of data_area
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				width = getWidth();
				height = getHeight();

				level.setBounds(1, (int) (height * 0.067), width - 2, (int) (height * 0.134));
				level.setFont(new Font("level", Font.BOLD, (int) (width * 0.12)));
				add(level);

				lines.setBounds(1, (int) (height * 0.267), width - 2, (int) (height * 0.134));
				lines.setFont(new Font("lines", Font.BOLD, (int) (width * 0.12)));
				add(lines);

				score.setBounds(1, (int) (height * 0.467), width - 2, (int) (height * 0.134));
				score.setFont(new Font("score", Font.BOLD, (int) (width * 0.12)));
				add(score);

				quitButton.setBorder(BorderFactory.createLineBorder(Color.black));
				quitButton.setBounds((int) (width * 0.1), (int) (height * 0.8), (int) (width * 0.6),
						(int) (height * 0.134));
				quitButton.setFont(new Font("quit", Font.BOLD, (int) (width * 0.12)));
				quitButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						System.exit(0);
					}
				});
				quitButton.setVisible(true);
				add(quitButton);

			}
		});

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		level.setText("Level:  " + gameData.getCurLevel());
		lines.setText("Lines:  " + gameData.getCurLines());
		score.setText("Score:  " + gameData.getCurScore());
	}

}

class GameData {

	public boolean[][] playMap;
	public Color[][] colorMap;
	public Shape shapes;
	private int random = 7;
	private int curTypeNum = (int) (Math.random() * random), nextTypeNum;
	private Draw curDraw, nextDraw;
	public int curLevel = 1, curLines = 0, curScore = 0;
	private boolean canRotate = true;
	public int W, H;

	public GameData(int w, int h) {
		this.W = w;
		this.H = h;
		playMap = new boolean[W][H];
		colorMap = new Color[W][H];
		shapes = new Shape(W, H);
		nextTypeNum = (int) (Math.random() * random);
		while (nextTypeNum == curTypeNum) {
			nextTypeNum = (int) (Math.random() * random);
		}
		initDraw(curTypeNum);
		nextDraw = shapes.getDraw(nextTypeNum);
	}

	public void initDraw(int type) {
		Draw s = shapes.getDraw(type);
		Point[][] p = s.getPoints();
		Point[][] newP = new Point[p.length][p[0].length];
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[i].length; j++) {
				newP[i][j] = new Point(p[i][j].x, p[i][j].y);
			}
		}
		curDraw = new Draw(s.offX, s.typeNum, s.getColor(), newP, W, H);
	}

	public Color[][] getColorMap() {
		return colorMap;
	}

	public void setCurTypeNum(int curType) {
		this.curTypeNum = curType;
	}

	public Draw getCurDraw() {
		return curDraw;
	}

	public int getNextDrawNum() {
		return nextTypeNum;
	}

	public void setNextTypeNum(int x) {
		this.nextTypeNum = x;
	}

	public Draw getNextDraw() {
		return nextDraw;
	}

	public void setNextDraw(int x) {
		this.nextDraw = shapes.getDraw(x);
	}

	public int getCurLevel() {
		return curLevel;
	}

	public int getCurLines() {
		return curLines;
	}

	public int getCurScore() {
		return curScore;
	}

	public void setCurScore(int score) {
		this.curScore = score;
	}

	public void setCanRotate(boolean x) {
		canRotate = x;
	}

	public boolean getCanRotate() {
		return canRotate;
	}

	public void setCurDraw(Draw curDraw) {
		this.curDraw = curDraw;
	}

	public void setShapes(Shape shapes) {
		this.shapes = shapes;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public void setCurLines(int curLines) {
		this.curLines = curLines;
	}

	public void setCurLevel(int curLevel) {
		this.curLevel = curLevel;
	}
}

class GameFunction {

	private GameData gameData;
	private boolean[][] playMap;
	private Color[][] colorMap;
	private Draw shape;
	private boolean isGG;

	public GameFunction(GameData gameData, PlayArea playArea, NextArea nextArea, Timer timer, int fallSpeed,
			boolean isGG) {
		this.gameData = gameData;
		this.playMap = gameData.playMap;
		this.colorMap = gameData.colorMap;
		this.isGG = isGG;
	}

	// move shape
	public void move(int moveX, int moveY) {
		if (isGG) {
			return;
		}
		playMap = gameData.playMap;
		shape = gameData.getCurDraw();
		shape.move(moveX, moveY, playMap);
	}

	public void rotateClockwise() {
		if (isGG) {
			return;
		}
		playMap = gameData.playMap;
		if (gameData.getCanRotate()) {
			shape = gameData.getCurDraw();
			if (shape.getType() == 0) {
				return;
			}
			shape.rotateClockwise(playMap);
		}
	}

	public void rotateCounterClockwise() {
		if (isGG) {
			return;
		}
		playMap = gameData.playMap;
		if (gameData.getCanRotate()) {
			shape = gameData.getCurDraw();
			if (shape.getType() == 0) {
				return;
			}
			shape.rotateCounterClockwise(playMap);
		}
	}

	public void falling() {
		this.shape = gameData.getCurDraw();
		boolean[][] map = gameData.playMap;
		Color[][] colorMap = gameData.colorMap;
		// check whether the shape can move
		if (shape.move(0, 1, map)) {
			return;
		}

		// if it can not move, draw it in the playMap.
		Point[] tetrPonints = shape.getPoints()[shape.getPosNum()];
		for (int i = 0; i < tetrPonints.length; i++) {
			map[tetrPonints[i].x + shape.initPos][tetrPonints[i].y] = true;
			colorMap[tetrPonints[i].x + shape.initPos][tetrPonints[i].y] = shape.getColor();
		}

		// initial another shape
		gameData.initDraw(gameData.getNextDrawNum());
		gameData.setCurTypeNum(gameData.getNextDrawNum());
		gameData.setNextTypeNum((int) (Math.random() * gameData.getRandom()));
		gameData.setNextDraw(gameData.getNextDrawNum());
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}


	public boolean[][] getPlayMap() {
		return playMap;
	}

	public void setPlayMap(boolean[][] playMap) {
		this.playMap = playMap;
	}

	public Color[][] getColorMap() {
		return colorMap;
	}

	public void setColorMap(Color[][] colorMap) {
		this.colorMap = colorMap;
	}

}

@SuppressWarnings("serial")
class NextArea extends JPanel {

	public int blockSize;
	public GameData gameData;
	private Draw shape;
	private Shape nextShape;

	public NextArea(GameData gameData, int blockSize, int mapW, int mapH) {

		this.gameData = gameData;
		this.blockSize = blockSize;
		nextShape = new Shape(mapW, mapH);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		shape = nextShape.getDraw(gameData.getNextDrawNum());
		Point[] points = shape.getPoints()[shape.getPosNum()];
		int offY = 2;
		for (Point p : points) {
			g.setColor(shape.getColor());
			g.fillRect(((7 - shape.offX) / 2 + p.x) * blockSize, ((4 - offY) / 2 + p.y) * blockSize, blockSize,
					blockSize);
			g.setColor(Color.BLACK);
			g.drawRect(((7 - shape.offX) / 2 + p.x) * blockSize, ((4 - offY) / 2 + p.y) * blockSize, blockSize,
					blockSize);

		}
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public void setNextShape(Shape nextshape) {
		this.nextShape = nextshape;
	}
}

class Draw {
	private Color color;
	private Point[][] points;
	public int typeNum;
	public int posNum;
	public int offX;
	public int initPos;
	private static int MIN_X = 0;
	public static int MAX_X;
	private static int MIN_Y = 0;
	public static int MAX_Y;

	public Draw(int offX, int typeNum, Color color, Point[][] points, int mapW, int mapH) {

		this.offX = offX;
		this.color = color;
		this.points = points;
		this.typeNum = typeNum;
		this.posNum = 0;
		this.initPos = (mapW - offX) / 2;
		MAX_X = mapW - 1;
		MAX_Y = mapH - 1;

	}

	public int getType() {
		return typeNum;
	}

	public void setTypeNum(int typeNum) {
		this.typeNum = typeNum;
	}

	public int getPosNum() {
		return posNum;
	}

	public Color getColor() {
		return color;
	}

	public Point[][] getPoints() {
		return points;
	}

	// move the blocks
	public boolean move(int moveX, int moveY, boolean[][] map) {
		for (Point p : points[posNum]) {
			int newX = p.x + moveX;
			int newY = p.y + moveY;
			if (this.isOutOfBoundary(newX, newY, map)) {
				return false;
			}
		}

		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[x].length; y++) {
				points[x][y].x += moveX;
				points[x][y].y += moveY;
			}
		}
		return true;
	}

	public void rotateClockwise(boolean[][] map) {
		int newPosNum = posNum + 1;
		for (Point p : getPoints()[newPosNum % 4]) {
			if (isOutOfBoundary(p.x, p.y, map)) {
				return;
			}
		}
		posNum = newPosNum % 4;
	}

	public void rotateCounterClockwise(boolean[][] map) {
		int newPosNum = posNum - 1;
		if (newPosNum < 0) {
			newPosNum = 3;
		}
		for (Point p : getPoints()[newPosNum % 4]) {
			if (isOutOfBoundary(p.x, p.y, map)) {
				return;
			}
		}
		posNum = newPosNum % 4;
	}

	// check whether the shape touched the boundary
	private boolean isOutOfBoundary(int x, int y, boolean map[][]) {
		return x + initPos < MIN_X || x + initPos > MAX_X || y < MIN_Y || y > MAX_Y || map[x + this.initPos][y];
	}

	public void setIntiPos(int initPos) {
		this.initPos = initPos;
	}
}

@SuppressWarnings("serial")
class Shape extends JPanel {

	public ArrayList<Draw> shapes;

	public Shape(int mapW, int mapH) {

		shapes = new ArrayList<>();

		// Square shape
		shapes.add(0,
				new Draw(2, 0, Color.GREEN,
						new Point[][] { { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
								{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
								{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
								{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) } },
						mapW, mapH));

		// I shape
		shapes.add(1,
				new Draw(4, 1, Color.CYAN,
						new Point[][] { { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) },
								{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
								{ new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) },
								{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },
						mapW, mapH));

		// L shape
		shapes.add(2,
				new Draw(3, 2, Color.RED,
						new Point[][] { { new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
								{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
								{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
								{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2) } },
						mapW, mapH));

		// J shape
		shapes.add(3,
				new Draw(3, 3, Color.BLUE,
						new Point[][] { { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
								{ new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2) },
								{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
								{ new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2) } },
						mapW, mapH));

		// S shape
		shapes.add(4,
				new Draw(3, 4, Color.YELLOW,
						new Point[][] { { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
								{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
								{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
								{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) } },
						mapW, mapH));

		// Z shape
		shapes.add(5,
				new Draw(3, 5, Color.MAGENTA,
						new Point[][] { { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
								{ new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
								{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
								{ new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },
						mapW, mapH));

		// T shape
		shapes.add(6,
				new Draw(3, 6, Color.ORANGE,
						new Point[][] { { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
								{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
								{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
								{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },
						mapW, mapH));
	}

	public Draw getDraw(int typeNum) {
		return shapes.get(typeNum);
	}

}
package tetris;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class Tetris {

	static int MAINAREA_WIDTH = 10;
	static int MAINAREA_HEIGHT = 20;
	static int BLOCKSIZE = 30;
	static int LEVEL = 1;
	static int LINE = 0;
	static int SCORE = 0;
	static int M = 1; // M – scoring factor (range: 1-10).
	static int N = 20; // N – number of rows required for each Level of difficulty (range: 20-50).
	static float S = 0.1f; // S – speed factor (range: 0.1-1.0).

	/**
	 * Main function
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Tetris.getInstance().setup();
	}

	private static final Tetris instance = new Tetris();

	TetrisCanvas canvas;
	TetrisFrame frame;
	JDialog window;

	Timer timer;
	Color[][] mainAreaColor;
	Shapes currTetrisShape;
	Shapes nextTetrisShape;
	boolean isGamePlaying;
	boolean isGamePaused;
	boolean isGameOver;

	private Tetris() {
		mainAreaColor = new Color[MAINAREA_WIDTH][MAINAREA_HEIGHT];
		canvas = new TetrisCanvas();
		frame = new TetrisFrame(canvas);
		isGamePlaying = false;
		isGameOver = false;
	}

	public static synchronized Tetris getInstance() {
		return instance;
	}

	public void handleTimer(boolean isGamePaused) {
		if (isGamePaused != this.isGamePaused) {
			this.isGamePaused = isGamePaused;
			if (isGamePaused) {
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
			} else {
				timer = new Timer();
				// FS = FS x (1 + Level x S)
				float interval = (float) 500 / (1f + S * (float) LEVEL);
				timer.scheduleAtFixedRate(new GameTimer(), (int) interval, (int) interval);
			}
		}
	}

	public Color[][] getMainAreaColor() {
		return mainAreaColor;
	}

	public Shapes getCurrTetrisShape() {
		return currTetrisShape;
	}

	public Shapes getNextTetrisShape() {
		return nextTetrisShape;
	}

	void startGame() {
		LEVEL = 1;
		SCORE = 0;
		LINE = 0;
		mainAreaColor = new Color[MAINAREA_WIDTH][MAINAREA_HEIGHT];
		currTetrisShape = new Shapes();
		nextTetrisShape = new Shapes();
		isGamePlaying = true;
		isGamePaused = true;
		handleTimer(false);
		isGameOver = false;
		frame.setSize(BLOCKSIZE * (MAINAREA_WIDTH + 5) + 3 * 10, BLOCKSIZE * MAINAREA_HEIGHT + 10 * 2);
		canvas.repaint();
	}

	void endGame() {
		currTetrisShape = null;
		isGamePlaying = false;
		handleTimer(true);
		canvas.repaint();
	}

	public boolean inMainArea(int x, int y) {
		return x >= 0 && x < MAINAREA_WIDTH && y >= 0 && y < MAINAREA_HEIGHT;
	}

	public boolean isMovable(int[][] shapeMatrix) {
		for (int i = 0; i < shapeMatrix.length; i++) {
			int x = shapeMatrix[i][0];
			int y = shapeMatrix[i][1];
			if (!(x >= 0 && x < MAINAREA_WIDTH && y < MAINAREA_HEIGHT)) {
				return false;
			} else if (inMainArea(x, y) && mainAreaColor[x][y] != null) {
				return false;
			}
		}
		return true;
	}

	public static void rotateShape(boolean shapeMatrix[][], boolean clockwise) {
		int N = shapeMatrix.length;
		for (int x = 0; x < N / 2; x++) {
			for (int y = x; y < N - x - 1; y++) {
				if (clockwise) {
					boolean temp = shapeMatrix[x][y];
					shapeMatrix[x][y] = shapeMatrix[N - 1 - y][x];
					shapeMatrix[N - 1 - y][x] = shapeMatrix[N - 1 - x][N - 1 - y];
					shapeMatrix[N - 1 - x][N - 1 - y] = shapeMatrix[y][N - 1 - x];
					shapeMatrix[y][N - 1 - x] = temp;
				} else {
					boolean temp = shapeMatrix[x][y];
					shapeMatrix[x][y] = shapeMatrix[y][N - 1 - x];
					shapeMatrix[y][N - 1 - x] = shapeMatrix[N - 1 - x][N - 1 - y];
					shapeMatrix[N - 1 - x][N - 1 - y] = shapeMatrix[N - 1 - y][x];
					shapeMatrix[N - 1 - y][x] = temp;
				}
			}
		}
	}

	public boolean moveOrChange(Action action) {
		if (currTetrisShape == null)
			return false;
		boolean[][] newPos = currTetrisShape.getPos();
		int[] newPt = currTetrisShape.getPt();
		Shapes changedShape = null;
		switch (action) {
		case MoveDown:
			newPt[1] += 1;
			break;
		case MoveLeft:
			newPt[0] -= 1;
			break;
		case MoveRight:
			newPt[0] += 1;
			break;
		case RotateLeft:
			if (currTetrisShape.type != Shape.Shape_Sq && currTetrisShape.type != Shape.Shape_N7)
				rotateShape(newPos, true);
			break;
		case RotateRight:
			if (currTetrisShape.type != Shape.Shape_Sq && currTetrisShape.type != Shape.Shape_N7)
				rotateShape(newPos, false);
			break;
		case Change:
			changedShape = new Shapes();
			while (changedShape.type == currTetrisShape.type || changedShape.type == nextTetrisShape.type)
				changedShape = new Shapes();
			changedShape.pt = currTetrisShape.pt;
			newPos = changedShape.getPos();
			break;
		default:
			break;
		}

		int[][] newShapePos = Shapes.getNextPos(newPt, newPos);

		if (isMovable(newShapePos)) {
			if (action == Action.Change) {
				currTetrisShape = changedShape;
				SCORE -= LEVEL * M;
			} else {
				currTetrisShape.setPt(newPt);
				currTetrisShape.setPos(newPos);
			}
			return true;
		} else {
			if (action == Action.MoveDown) {
				boolean endOfGame = false;
				int[][] blocksPos = currTetrisShape.getBlockPos();
				PriorityQueue<Integer> rowsToRemove = new PriorityQueue<>(MAINAREA_HEIGHT);
				for (int i = 0; i < blocksPos.length; i++) {
					int x = blocksPos[i][0];
					int y = blocksPos[i][1];
					if (!inMainArea(x, y))
						endOfGame = true;
					else {
						mainAreaColor[x][y] = currTetrisShape.color;
						boolean hasSpace = false;
						for (int j = 0; j < MAINAREA_WIDTH; j++) {
							if (mainAreaColor[j][y] == null) {
								hasSpace = true;
								break;
							}
						}
						if (!hasSpace)
							rowsToRemove.add(y);
					}
				}
				if (!endOfGame) {
					currTetrisShape = nextTetrisShape;
					nextTetrisShape = new Shapes();
					if (rowsToRemove.size() > 0) {
						removeRows(rowsToRemove);
					}
					return true;
				} else {
					isGameOver = true;
					endGame();
				}
			}
			return false;
		}
	}

	private void removeRows(PriorityQueue<Integer> rows) {
		for (Integer row : rows) {
			for (int r = row - 1; r >= 0; r--) {
				for (int c = 0; c < MAINAREA_WIDTH; c++)
					mainAreaColor[c][r + 1] = mainAreaColor[c][r];
			}
			for (int c = 0; c < MAINAREA_WIDTH; c++)
				mainAreaColor[c][0] = null;
		}
		SCORE += rows.size() * (LEVEL * M);
		LINE += rows.size();
		if (LINE >= N) {
			LEVEL++;
			LINE -= N;
			handleTimer(true);
			handleTimer(false);
		}
	}

	private void displayWindow() {
		if (SwingUtilities.isEventDispatchThread()) {
			window.revalidate();
			window.repaint();
			window.pack();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						displayWindow();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void setup() {
		if (SwingUtilities.isEventDispatchThread()) {
			window = new JDialog(frame, "Game Setup", true);

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			window.add(panel);

			{
				JPanel newShapesPanel = new JPanel();
				newShapesPanel.setLayout(new GridBagLayout());

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.insets = new Insets(5, 5, 5, 5);

				{
					{
						gbc.gridx = 0;
						gbc.gridy = 0;
						Shape type = Shape.Shape_N1;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 1;
						gbc.gridy = 0;
						Shape type = Shape.Shape_N2;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 2;
						gbc.gridy = 0;
						Shape type = Shape.Shape_N3;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 3;
						gbc.gridy = 0;
						Shape type = Shape.Shape_N4;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 0;
						gbc.gridy = 1;
						Shape type = Shape.Shape_N5;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 1;
						gbc.gridy = 1;
						Shape type = Shape.Shape_N6;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 2;
						gbc.gridy = 1;
						Shape type = Shape.Shape_N7;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}

					{
						gbc.gridx = 3;
						gbc.gridy = 1;
						Shape type = Shape.Shape_N8;
						JPanel element = new JPanel();
						element.setLayout(new FlowLayout());
						element.add(new ShapePanel(type));
						JCheckBox cb = new JCheckBox();
						cb.setSelected(!Shapes.unusedShapes.contains(type));
						cb.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!Shapes.unusedShapes.contains(type))
									Shapes.unusedShapes.add(type);
								else
									Shapes.unusedShapes.remove(type);
							}
						});
						element.add(cb);
						newShapesPanel.add(element, gbc);
					}
				}
				panel.add(newShapesPanel);

			}

			{
				JPanel parametersPanel = new JPanel();
				parametersPanel.setLayout(new GridBagLayout());

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridy = 0;
				gbc.insets = new Insets(10, 10, 10, 10);

				{
					JPanel factorPanel = new JPanel();
					factorPanel.setLayout(new BoxLayout(factorPanel, BoxLayout.Y_AXIS));

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.LEADING));
						row.add(new JLabel("M:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, M);
						slider.setMajorTickSpacing(1);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								M = slider.getValue();
							}
						});
						row.add(slider);
						factorPanel.add(row);
					}

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.LEADING));
						row.add(new JLabel("N:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, 20, 50, N);
						slider.setMajorTickSpacing(5);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								N = slider.getValue();
							}
						});
						row.add(slider);
						factorPanel.add(row);
					}

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.LEADING));
						row.add(new JLabel("S:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, (int) (0.1f * 10f), (int) (1.0f * 10f),
								(int) (S * 10f));
						slider.setMajorTickSpacing(1);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								S = (float) slider.getValue() / 10f;
							}
						});
						row.add(slider);
						factorPanel.add(row);
					}
					gbc.gridx = 0;
					parametersPanel.add(factorPanel, gbc);
				}

				{
					JPanel mainAreaPanel = new JPanel();
					mainAreaPanel.setLayout(new BoxLayout(mainAreaPanel, BoxLayout.Y_AXIS));

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.TRAILING));
						row.add(new JLabel("W:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 20, MAINAREA_WIDTH);
						slider.setMajorTickSpacing(5);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								MAINAREA_WIDTH = slider.getValue();
							}
						});
						row.add(slider);
						mainAreaPanel.add(row);
					}

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.TRAILING));
						row.add(new JLabel("H:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, 20, 30, MAINAREA_HEIGHT);
						slider.setMajorTickSpacing(5);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								MAINAREA_HEIGHT = slider.getValue();
							}
						});
						row.add(slider);
						mainAreaPanel.add(row);
					}

					{
						JPanel row = new JPanel();
						row.setLayout(new FlowLayout(FlowLayout.TRAILING));
						row.add(new JLabel("Block Size:"));
						JSlider slider = new JSlider(JSlider.HORIZONTAL, 30, 40, BLOCKSIZE);
						slider.setMajorTickSpacing(5);
						slider.setMinorTickSpacing(1);
						slider.setPaintTicks(true);
						slider.setPaintLabels(true);
						slider.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent e) {
								BLOCKSIZE = slider.getValue();
								displayWindow();
							}
						});
						row.add(slider);
						mainAreaPanel.add(row);
					}
					gbc.gridx = 1;
					parametersPanel.add(mainAreaPanel, gbc);
				}
				panel.add(parametersPanel);
			}

			{
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

				Button startButton = new Button("Start");
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startGame();
						window.setVisible(false);
					}
				});
				buttonPanel.add(startButton);

				Button cancelButton = new Button("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (isGamePaused && !isGameOver && isGamePlaying) {
							handleTimer(false);
						}
						window.setVisible(false);
					}
				});
				buttonPanel.add(cancelButton);
				panel.add(buttonPanel);
			}

			window.pack();
			window.setVisible(true);

		} else {

			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						setup();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
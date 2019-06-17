package ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import api.Conversions;
import entities.RenderingDemo;
import entities.TetrisGame;
import ui.components.ActiveTetromino;
import ui.components.GameBorder;
import ui.components.GhostTetromino;
import ui.components.NextTetromino;
import ui.components.PauseIndicator;
import ui.components.PlayArea;
import ui.components.QuitButton;
import ui.components.ScoreIndicator;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisGameUI extends JFrame {

	private static final long serialVersionUID = -4294448890074857524L;
	
	public static int GAME_WIDTH;
	public static int GAME_HEIGHT;
	public static int FRAME_WIDTH;
	public static int FRAME_HEIGHT;
	
	private JPanel contentPane;
	private PlayArea playArea;
	private GhostTetromino ghostTetromino;
	private ActiveTetromino activeTetromino;
	private NextTetromino nextTetromino;
	private ScoreIndicator scoreIndicator;
	private PauseIndicator pauseIndicator;
	private QuitButton quitButton;
	private GameBorder gameBorder;
	
	private TetrisGame game;
	private Timer gameUpdateTimer;
	private long gameUpdateInterval;
	
	//launch application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TetrisGameUI ui = new TetrisGameUI();
					ui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//default constructor
	public TetrisGameUI() { this(10, 20, 200, 5, 1); }

	//constructor with game setting args
	public TetrisGameUI(int playAreaWidth, int playAreaHeight, 
			long updateInterval, int linesPerLevel, int scoreMultiplier) {
		//start the game
		game = new TetrisGame(playAreaWidth, playAreaHeight, 
				updateInterval, linesPerLevel, scoreMultiplier);

		//create game UI components
		playArea = new PlayArea(game);
		ghostTetromino = new GhostTetromino(game);
		activeTetromino = new ActiveTetromino(game);
		nextTetromino = new NextTetromino(game);
		scoreIndicator = new ScoreIndicator(game);
		pauseIndicator = new PauseIndicator(game);
		quitButton = new QuitButton();
		
		//setup frame and content pane
		GAME_WIDTH = NextTetromino.OFFSET_X + NextTetromino.WIDTH + 16;
		GAME_HEIGHT = PlayArea.OFFSET_Y + PlayArea.HEIGHT + 16;
		FRAME_WIDTH = 10;
		FRAME_HEIGHT = 32;
		
		gameBorder = new GameBorder(GAME_WIDTH, GAME_HEIGHT);
		
		setTitle("Tetris!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, GAME_WIDTH + FRAME_WIDTH, GAME_HEIGHT + FRAME_HEIGHT);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//set the logical and device drawing dimensions
		Conversions.setLogicalDimensions(GAME_WIDTH, GAME_HEIGHT);
		Conversions.setDeviceDimensions(contentPane.getSize());
		
		//add each component to the UI
		//these are rendered in LIFO order
		contentPane.add(quitButton);
		contentPane.add(pauseIndicator);
		contentPane.add(scoreIndicator);
		contentPane.add(nextTetromino);
		contentPane.add(activeTetromino);
		contentPane.add(ghostTetromino);
		contentPane.add(playArea);
		contentPane.add(gameBorder);
		
		//add UI mouse events to the content pane
		addMouseEvents();
		
		//create and start a timer to periodically update the game
		startGameUpdateTimer();
	}
	
	
	//adds mouse listeners and mouse pressed events to the content pane
	private void addMouseEvents() {
		
		//this method should be removed in favor of each component
		//handling its own mouse events. At the moment this cannot 
		//done as component bound computation is not implemented
		
		//for all frame-based actions check both if the frame contains 
		//the mouse and if it does not contain it, cannot use if/else 
		//since an interrupt may/will occur between the if and else
		
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				float x = Conversions.toLogicalX(event.getX());
				float y = Conversions.toLogicalY(event.getY());
				
				//close application when mouse pressed in quit button
				if (QuitButton.FRAME.contains(x, y)) {
					System.exit(0);
				}
				
				//move when pressed outside of quitbutton
				if (!QuitButton.FRAME.contains(x, y)) {
					if (SwingUtilities.isLeftMouseButton(event)) {
						game.moveActiveLeft();
						repaint();
					}
					if (SwingUtilities.isRightMouseButton(event)) {
						game.moveActiveRight();
						repaint();
					}
				}
			}
		});
		
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				//if wheel rotation < 0, then scrolling is up
				if (event.getWheelRotation() < 0) {
					game.moveActiveClockwise();
					repaint();
				}
				else {
					game.moveActiveCounterClockwise();
					repaint();
				}
			}
		});
		
		contentPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent event) {
				float x = Conversions.toLogicalX(event.getX());
				float y = Conversions.toLogicalY(event.getY());
				
				//pause game when play area is hovered
				if (PlayArea.FRAME.contains(x, y) && !pauseIndicator.isHovered()) {
					pauseIndicator.setHovered(true);
					repaint();
				}
				if (!PlayArea.FRAME.contains(x, y) && pauseIndicator.isHovered()) {
					pauseIndicator.setHovered(false);
					repaint();
				}
				
				//swap active tetromino with next when hovered
				if (TetrominoPainter.contains(game.getActive(), 
						x, y, PlayArea.OFFSET_X, PlayArea.OFFSET_Y) && 
						!activeTetromino.isHovered()) {
					activeTetromino.setHovered(true);
					repaint();
				}
				if (!TetrominoPainter.contains(game.getActive(), 
						x, y, PlayArea.OFFSET_X, PlayArea.OFFSET_Y) && 
						activeTetromino.isHovered()) {
					activeTetromino.setHovered(false);
					repaint();
				}
				
				//animate quit button on hover
				if (QuitButton.FRAME.contains(x, y) && !quitButton.isHovered()) {
					quitButton.setHovered(true);
					repaint();
				}
				if (!QuitButton.FRAME.contains(x, y) && quitButton.isHovered()) {
					quitButton.setHovered(false);
					repaint();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	//starts or restarts game timer
	private void startGameUpdateTimer() {
		gameUpdateInterval = game.getUpdateInterval();
		
		gameUpdateTimer = new Timer();
		gameUpdateTimer.schedule(new TimerTask() {
	        public void run() {
	        	if (game.isOver()) {
		            JOptionPane.showMessageDialog(null, "Game Over!");
		            
		            this.cancel();
					System.exit(0);
	        	}
	        	
	            game.update();
	            repaint();
	            
	            if (gameUpdateInterval != game.getUpdateInterval()) {
	            	gameUpdateInterval = game.getUpdateInterval();
		            this.cancel();
		            
		            startGameUpdateTimer();
	            }
	        }
	    }, 0, gameUpdateInterval);
	}
	
	//called on each resize
	@Override
 	public void validate() {
		super.validate();

		//update the device drawing dimensions for the game
		Conversions.setDeviceDimensions(contentPane.getSize());
		
		//update bounds of child components
		for(Component component : contentPane.getComponents())
			component.setBounds(0, 0, 
					contentPane.getSize().width, 
					contentPane.getSize().height);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
	}
}
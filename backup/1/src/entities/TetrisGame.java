package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import entities.blocks.Block;
import entities.blocks.Tetromino;
import entities.blocks.tetrominos.I_Mino;
import entities.blocks.tetrominos.J_Mino;
import entities.blocks.tetrominos.L_Mino;
import entities.blocks.tetrominos.O_Mino;
import entities.blocks.tetrominos.S_Mino;
import entities.blocks.tetrominos.T_Mino;
import entities.blocks.tetrominos.Z_Mino;

public class TetrisGame {

	protected int playAreaWidth;
	protected int playAreaHeight;
	protected int tetrominoWidth;
	
    //game data
	protected Block[][] playArea;
	protected List<Tetromino> bag;
	
	//tetromino game data
	protected Point start;
	protected Tetromino active;
	protected Tetromino next;
	protected Tetromino ghost;
	
	//metagame data
	protected long updateInterval;
	protected int linesPerLevel;
	protected int scoreMultiplier;
	
	protected int level;
	protected int lines;
	protected int score;
	protected boolean paused;
	protected boolean over;
	
	//default constructor
	public TetrisGame() { }

	//constructor with game setting args
	public TetrisGame(int playAreaWidth, int playAreaHeight, 
			long updateInterval, int linesPerLevel, int scoreMultiplier) {
		this.playAreaHeight = playAreaHeight;
		this.playAreaWidth = playAreaWidth;
		tetrominoWidth = 2;
		
		playArea = new Block[playAreaHeight][playAreaWidth];
		bag = new ArrayList<Tetromino>();
		
		start = new Point(playAreaWidth / 2 - tetrominoWidth, 0);
		active = nextTetrominoInBag(start);
		next = nextTetrominoInBag(start);
		ghost = generateGhost();
		
		this.updateInterval = updateInterval;
		this.linesPerLevel = linesPerLevel;
		this.scoreMultiplier = scoreMultiplier;
		
		level = 1;
		lines = 0;
		score = 0;
		paused = false;
	}
	
	//steps the game one time state forward
	public void update() {
		//temporaily save current game state
		TetrisGame state = new TetrisGame();
		state.setLines(lines);
		state.setLevel(level);
		state.setScore(score);
		
		//move the active tetromino down, note: this method places
		//the tetromino if it cannot be moved down
		moveActiveDown();
		
		//update game metadata
		if (this.lines != state.getLines()) {
			int linesCleared = this.lines - state.getLines();
			score += level * scoreMultiplier * linesCleared;
		}
		if (lines >= linesPerLevel * level) {
			level++;
		}
		if (this.level != state.getLevel()) {
			updateInterval *= (state.getLevel() / (double)level);
		}
		if (this.score != state.getScore()) {
			//TODO: Implement events that trigger on score change
		}
	}
	
	//swaps active tetromino with next tetromino
	public void swapActive() {
		next.getPosition().x = active.getPosition().x; 
		next.getPosition().y = active.getPosition().y; 
		
		active = next;
		next = nextTetrominoInBag(start);
		ghost = generateGhost();
		
		//apply score penalty for swapping
		score -= level * scoreMultiplier;
	}
	
	//returns next tetromino in bag
	private Tetromino nextTetrominoInBag(Point position) {
		if (0 == bag.size()) {
			//bag is empty, re-fill and re-randomize bag
			bag.add(new I_Mino((Point) position.clone()));
			bag.add(new J_Mino((Point) position.clone()));
			bag.add(new L_Mino((Point) position.clone()));
			bag.add(new O_Mino((Point) position.clone()));
			bag.add(new S_Mino((Point) position.clone()));
			bag.add(new T_Mino((Point) position.clone()));
			bag.add(new Z_Mino((Point) position.clone()));

			Collections.shuffle(bag, new Random(System.nanoTime()));
		}
		
		//remove and return next piece in bag
		return bag.remove(0);
	}

	//true if given row is filled
	private boolean rowIsFilled(Block[] blocks) {
		for (Block block : blocks) {
			if (null == block) { return false; }
		}
		
		return true;
	}
	
	//removes row and shifts blocks above down by one
	private void removeLine(int i) {
		for (; i > 0; i--) {
			for (int j = 0; j < playArea[i].length; j++) {
				//move both the array reference and positional values
				playArea[i][j] = playArea[i - 1][j];
				if (null != playArea[i][j]) {
					playArea[i][j].moveDown();
				}
			}
		}
	}
	
	//handles filled rows & returns number of rows filled
	private void handleFilledLines() {
		for (int i = 0; i < playArea.length; i++) {
			if (rowIsFilled(playArea[i])) {
				removeLine(i);
				lines++;
			}
		}
	}
	
	//converts the tetromino into blocks
	private void placeTetromino(Tetromino tetromino) {
		Block[] blocks = tetromino.getBlocks();
		
		for (Block block : blocks) {
			block.getPosition().x += tetromino.getPosition().x;
			block.getPosition().y += tetromino.getPosition().y;
			int x = block.getPosition().x;
			int y = block.getPosition().y;
			
			if (null == playArea[y][x]) {
				playArea[y][x] = block;
			}
			else {
				playArea[y][x] = block;
				over = true;
			}
		}
		
		handleFilledLines();
	}
	
	//true if given tetromino is inside the play area
	private boolean inPlayArea(Tetromino tetromino) {
		Block[] blocks = tetromino.getBlocks();
		
		for (Block block : blocks) {
			int x = tetromino.getPosition().x + block.getPosition().x;
			int y = tetromino.getPosition().y + block.getPosition().y;
			
			//check if this location is in the play area
			if (x < 0 || x > playAreaWidth - 1 ||
				y < 0 || y > playAreaHeight - 1) {
				return false;
			}
		}
		
		return true;
	}
	
	//true if given tetromino is colliding with a block
	private boolean isColliding(Tetromino tetromino) {
		Block[] blocks = tetromino.getBlocks();
		
		for (Block block : blocks) {
			int x = tetromino.getPosition().x + block.getPosition().x;
			int y = tetromino.getPosition().y + block.getPosition().y;
			
			//check if a block already exists at this location in the play area
			if (null != playArea[y][x]) {
				return true;
			}
		}
		
		return false;
	}
	
	//generate ghost where active block will fall 
	private Tetromino generateGhost() {
		//test on a copy where the active tetromino will fall
		Tetromino activeCopy = active.Copy();
		
		//move copy down until no longer possible
		while (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			activeCopy.moveDown();
		}
		
		//move copy back up one into a valid position
		activeCopy.moveUp();
		
		return activeCopy;
	}
	
	//block manipulation methods
	public void moveActiveUp() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved up
		Tetromino activeCopy = active.Copy();
		activeCopy.moveUp();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.moveUp();
			ghost = generateGhost();
		}
	}
	public void moveActiveRight() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved right
		Tetromino activeCopy = active.Copy();
		activeCopy.moveRight();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.moveRight();
			ghost = generateGhost();
		}
	}
	public void moveActiveDown() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved down
		Tetromino activeCopy = active.Copy();
		activeCopy.moveDown();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.moveDown();
			ghost = generateGhost();
		}
		else {
			//active tetromino cannot be moved down, place it instead
			placeTetromino(active);
			active = next;
			next = nextTetrominoInBag(start);
			ghost = generateGhost();
		}
	}
	public void moveActiveLeft() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved left
		Tetromino activeCopy = active.Copy();
		activeCopy.moveLeft();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.moveLeft();
			ghost = generateGhost();
		}
	}
	public void moveActiveClockwise() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved clockwise
		Tetromino activeCopy = active.Copy();
		activeCopy.rotateClockWise();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.rotateClockWise();
			ghost = generateGhost();
		}
	}
	public void moveActiveCounterClockwise() {
		//do nothing if paused
		if (paused) { return; }
		
		//test on a copy if the active tetromino can be moved counter clockwise
		Tetromino activeCopy = active.Copy();
		activeCopy.rotateCounterClockWise();
		
		if (inPlayArea(activeCopy) && !isColliding(activeCopy)) {
			active.rotateCounterClockWise();
			ghost = generateGhost();
		}
	}

	//getters and setters
	public Block[][] getPlayArea() {
		return playArea;
	}
	public List<Tetromino> getBag() {
		return bag;
	}
	public Tetromino getActive() {
		return active;
	}
	public Tetromino getNext() {
		return next;
	}
	public Tetromino getGhost() {
		return ghost;
	}
	public void setGhost(Tetromino ghost) {
		this.ghost = ghost;
	}
	public long getUpdateInterval() {
		return updateInterval;
	}
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLines() {
		return lines;
	}
	public void setLines(int lines) {
		this.lines = lines;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isPaused() {
		return paused;
	}
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	public int getPlayAreaWidth() {
		return playAreaWidth;
	}
	public void setPlayAreaWidth(int playAreaWidth) {
		this.playAreaWidth = playAreaWidth;
	}
	public int getPlayAreaHeight() {
		return playAreaHeight;
	}
	public void setPlayAreaHeight(int playAreaHeight) {
		this.playAreaHeight = playAreaHeight;
	}
	public boolean isOver() {
		return over;
	}
	public void setOver(boolean over) {
		this.over = over;
	}
}

package entities.blocks;

import java.awt.Color;
import java.awt.Point;

public abstract class Tetromino {
	
	public enum Orientation {
		UP,
		RIGHT,
		DOWN,
		LEFT
	}
	
	protected Block[] blocks;
	protected Point position;
	protected Color color;
	protected Orientation orientation;
	
	//constructor with position, color, & orientation args
	public Tetromino(Point position, Color color, Orientation orientation) {
		this.position = position;
		this.color = color;
		this.orientation = orientation;
		
		//initialize blocks
		blocks = new Block[4];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
			blocks[i].setColor(color);
		}
		initializeBlocks();
	}

	//constructor with position & color args
	public Tetromino(Point position, Color color) {
		this.position = position;
		this.color = color;
		this.orientation = Orientation.UP;

		//initialize blocks
		blocks = new Block[4];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
			blocks[i].setColor(color);
		}
		initializeBlocks();
	}
	
	//method that initializes blocks
	protected abstract void initializeBlocks();
	
	//returns a copy of the current tetromino for forward time integration
	public abstract Tetromino Copy();
	
	//block manipulation methods
	public void moveUp() { position.y--; }  
	public void moveRight() { position.x++; }
	public void moveDown() { position.y++; }
	public void moveLeft() { position.x--; }
	public void rotateClockWise() 
	{
		switch(orientation)
		{
			case UP:
				setOrientation(Orientation.RIGHT);
				break;
			case RIGHT:
				setOrientation(Orientation.DOWN);
				break;
			case DOWN:
				setOrientation(Orientation.LEFT);
				break;
			case LEFT:
				setOrientation(Orientation.UP);
				break;
		}
	}
	public void rotateCounterClockWise() 
	{
		switch(orientation)
		{
			case UP:
				setOrientation(Orientation.LEFT);
				break;
			case RIGHT:
				setOrientation(Orientation.UP);
				break;
			case DOWN:
				setOrientation(Orientation.RIGHT);
				break;
			case LEFT:
				setOrientation(Orientation.DOWN);
				break;
		}
	}

	//getters and setters
	public Block[] getBlocks() {
		return blocks;
	}
	public Point getPosition() { 
		return position; 
	}
	public void setPosition(Point position) { 
		this.position = position; 
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Orientation getOrientation() {
		return orientation;
	}
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		initializeBlocks();
	}
}
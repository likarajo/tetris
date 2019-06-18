package entities.blocks;

import java.awt.Color;
import java.awt.Point;

public class Block {
	
	private Point position;
	private Color color;
	
	public Block()
	{
		this.position = new Point();
		this.color = new Color(128, 128, 128);
	}
	
	public Block(Point position, Color color)
	{
		this.position = position;
		this.color = color;
	}

	//block manipulation methods
	public void moveUp() { position.y--; }  
	public void moveRight() { position.x++; }
	public void moveDown() { position.y++; }
	public void moveLeft() { position.x--; }
	
	//getters and setters
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
}
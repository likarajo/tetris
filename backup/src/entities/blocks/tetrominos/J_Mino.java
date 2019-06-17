package entities.blocks.tetrominos;

import java.awt.Color;
import java.awt.Point;

import entities.blocks.Tetromino;

public class J_Mino extends Tetromino {
	
	public static final Color COLOR = new Color(0, 0, 255);

	//constructor with position & orientation args
	public J_Mino(Point position, Orientation orientation) {
		super(position, COLOR, orientation);
	}

	//constructor with position arg
	public J_Mino(Point position) {
		super(position, COLOR);
	}
	
	@Override
	protected void initializeBlocks() {
		switch(orientation)
		{
			case UP:
				blocks[0].getPosition().x = 0;
				blocks[1].getPosition().x = 1;
				blocks[2].getPosition().x = 2;
				blocks[3].getPosition().x = 0;
				
				blocks[0].getPosition().y = 1;
				blocks[1].getPosition().y = 1;
				blocks[2].getPosition().y = 1;
				blocks[3].getPosition().y = 0;
				break;
			case RIGHT:
				blocks[0].getPosition().x = 1;
				blocks[1].getPosition().x = 1;
				blocks[2].getPosition().x = 1;
				blocks[3].getPosition().x = 2;
				
				blocks[0].getPosition().y = 2;
				blocks[1].getPosition().y = 1;
				blocks[2].getPosition().y = 0;
				blocks[3].getPosition().y = 0;
				break;
			case DOWN:
				blocks[0].getPosition().x = 0;
				blocks[1].getPosition().x = 1;
				blocks[2].getPosition().x = 2;
				blocks[3].getPosition().x = 2;
				
				blocks[0].getPosition().y = 1;
				blocks[1].getPosition().y = 1;
				blocks[2].getPosition().y = 1;
				blocks[3].getPosition().y = 2;
				break;
			case LEFT:
				blocks[0].getPosition().x = 1;
				blocks[1].getPosition().x = 1;
				blocks[2].getPosition().x = 1;
				blocks[3].getPosition().x = 0;
				
				blocks[0].getPosition().y = 2;
				blocks[1].getPosition().y = 1;
				blocks[2].getPosition().y = 0;
				blocks[3].getPosition().y = 2;
				break;
		}
	}

	@Override
	public Tetromino Copy() {
		return new J_Mino((Point) position.clone(), orientation);
	}

}
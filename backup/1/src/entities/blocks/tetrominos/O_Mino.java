package entities.blocks.tetrominos;

import java.awt.Color;
import java.awt.Point;

import entities.blocks.Tetromino;

public class O_Mino extends Tetromino {

	public static final Color COLOR = new Color(247, 211, 8);

	//constructor with position & orientation args
	public O_Mino(Point position, Orientation orientation) {
		super(position, COLOR, orientation);
	}

	//constructor with position arg
	public O_Mino(Point position) {
		super(position, COLOR);
	}
	
	@Override
	protected void initializeBlocks() {
		blocks[0].getPosition().x = 1;
		blocks[1].getPosition().x = 1;
		blocks[2].getPosition().x = 2;
		blocks[3].getPosition().x = 2;
		
		blocks[0].getPosition().y = 1;
		blocks[1].getPosition().y = 0;
		blocks[2].getPosition().y = 0;
		blocks[3].getPosition().y = 1;
	}

	@Override
	public Tetromino Copy() {
		return new O_Mino((Point) position.clone(), orientation);
	}

}
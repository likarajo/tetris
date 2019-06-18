package entities;

import java.awt.Point;

import entities.blocks.Block;
import entities.blocks.tetrominos.I_Mino;
import entities.blocks.tetrominos.J_Mino;
import entities.blocks.tetrominos.L_Mino;
import entities.blocks.tetrominos.O_Mino;
import entities.blocks.tetrominos.S_Mino;
import entities.blocks.tetrominos.T_Mino;
import entities.blocks.tetrominos.Z_Mino;

public class RenderingDemo extends TetrisGame {
	public RenderingDemo() {
		//initialize play area
		playArea  = new Block[getPlayAreaWidth()][getPlayAreaHeight()];
		
		playArea[7][18] = new Block(new Point(7, 18), S_Mino.COLOR);
		playArea[8][18] = new Block(new Point(8, 18), S_Mino.COLOR);
		playArea[7][19] = new Block(new Point(7, 19), S_Mino.COLOR);
		playArea[6][19] = new Block(new Point(6, 19), S_Mino.COLOR);
		
		playArea[9][17] = new Block(new Point(9, 17), J_Mino.COLOR);
		playArea[9][18] = new Block(new Point(9, 18), J_Mino.COLOR);
		playArea[8][19] = new Block(new Point(8, 19), J_Mino.COLOR);
		playArea[9][19] = new Block(new Point(9, 19), J_Mino.COLOR);
		
		//note: we initialize block by block since once tetrominos
		//land, they are no longer tetrominos, they are just blocks
		
		//set active and next tetrominos
		active = new O_Mino(start);
		next = new L_Mino(start);
		
		//move active tetromino down one
		active.moveDown();
	}
}

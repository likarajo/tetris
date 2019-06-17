package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import ui.BlockPainter;
import ui.TetrominoPainter;
import api.Conversions;
import entities.TetrisGame;
import entities.blocks.Block;
import entities.blocks.Tetromino;
import entities.blocks.tetrominos.I_Mino;
import entities.blocks.tetrominos.O_Mino;

public class NextTetromino extends Component {
	
	private static final long serialVersionUID = 1L;

	public static int WIDTH;
	public static int HEIGHT;
	public static int OFFSET_X;
	public static int OFFSET_Y;
	public static Rectangle FRAME;
	
	private TetrisGame game;
	
	//constructor with TetrisGame argument
	public NextTetromino(TetrisGame game) {
		this.game = game;
		
		WIDTH = 128;
		HEIGHT = 80;
		OFFSET_X = PlayArea.OFFSET_X + PlayArea.WIDTH + 16;
		OFFSET_Y = 16;
		FRAME = new Rectangle(
				OFFSET_X - 2, 
				OFFSET_Y - 2, 
				WIDTH + 4 - 1,
				HEIGHT + 4 - 1);
	}
	
	@Override
	public void paint(Graphics graphics) {
		Conversions.drawRect(graphics, FRAME, Color.BLACK);
	
		//compute the offset needed to center the tetromino
		Point offset = new Point();
		if (game.getNext() instanceof I_Mino || game.getNext() instanceof O_Mino) {
			//center as if four blocks wide and two tall
			offset.x = OFFSET_X + (WIDTH - 4 * BlockPainter.PIXEL_SIZE) / 2;
			offset.y = OFFSET_Y + (HEIGHT - 2 * BlockPainter.PIXEL_SIZE) / 2;
		}
		else {
			//center as if three blocks wide and two tall
			offset.x = OFFSET_X + (WIDTH - 3 * BlockPainter.PIXEL_SIZE) / 2;
			offset.y = OFFSET_Y + (HEIGHT - 2 * BlockPainter.PIXEL_SIZE) / 2;
		}
		
		//manually paint each block to ignore the tetromino's position
		for(Block block : game.getNext().getBlocks()) {
			BlockPainter.paint(graphics, block, 
					offset.x, 
					offset.y);
		}
	}
}
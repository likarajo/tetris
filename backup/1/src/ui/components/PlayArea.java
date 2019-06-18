package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import ui.BlockPainter;
import api.Conversions;
import entities.TetrisGame;
import entities.blocks.Block;

public class PlayArea extends Component {
	
	private static final long serialVersionUID = 6479917092059257798L;

	public static int WIDTH;
	public static int HEIGHT;
	public static int OFFSET_X;
	public static int OFFSET_Y;
	public static Rectangle FRAME;
	
	private TetrisGame game;
	private Rectangle[][] blockGrid;
	
	//constructor with TetrisGame argument
	public PlayArea(TetrisGame game) {
		this.game = game;
		
		WIDTH = game.getPlayAreaWidth() * BlockPainter.PIXEL_SIZE;
		HEIGHT = game.getPlayAreaHeight() * BlockPainter.PIXEL_SIZE;
		OFFSET_X = 16;
		OFFSET_Y = 16;
		FRAME = new Rectangle(
				OFFSET_X - 2, 
				OFFSET_Y - 2, 
				WIDTH + 4 - 1,
				HEIGHT + 4 - 1);
		
		blockGrid = new Rectangle
				[game.getPlayAreaWidth()]
				[game.getPlayAreaHeight()];
		
		for (int i = 0; i < blockGrid.length; i++) {
			for (int j = 0; j < blockGrid[i].length; j++) {
				blockGrid[i][j] = new Rectangle(
						OFFSET_X + i * BlockPainter.PIXEL_SIZE,
						OFFSET_Y + j * BlockPainter.PIXEL_SIZE,
						BlockPainter.PIXEL_SIZE - 1,
						BlockPainter.PIXEL_SIZE - 1);
			}
		}
	}
	
	@Override
	public void paint(Graphics graphics) {
		//draw frame
		Conversions.drawRect(graphics, FRAME, Color.BLACK);
		
		//draw block grid
		graphics.setColor(Color.WHITE);
		for (int i = 0; i < blockGrid.length; i++) {
			for (int j = 0; j < blockGrid[i].length; j++) {
				Conversions.drawRect(graphics, blockGrid[i][j]);
			}
		}
		
		//draw each block
		for (int i = 0; i < game.getPlayArea().length; i++) {
			for (int j = 0; j < game.getPlayArea()[i].length; j++) {
				BlockPainter.paint(graphics, game.getPlayArea()[i][j], 
						OFFSET_X, OFFSET_Y);
			}
		}
	}
}
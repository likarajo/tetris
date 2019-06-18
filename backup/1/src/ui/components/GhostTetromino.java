package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import ui.TetrominoPainter;
import entities.TetrisGame;
import entities.blocks.Tetromino;

public class GhostTetromino extends Component {
	
	private static final long serialVersionUID = -3429538043903008536L;
	private TetrisGame game;
	
	//constructor with TetrisGame argument
	public GhostTetromino(TetrisGame game) {
		this.game = game;
	}
	
	@Override
	public void paint(Graphics graphics) {
		TetrominoPainter.paintGhost(graphics, game.getGhost(), 
				PlayArea.OFFSET_X, 
				PlayArea.OFFSET_Y);
	}
}

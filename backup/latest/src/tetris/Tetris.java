package tetris;

import java.awt.*;
import java.awt.event.*;

public class Tetris extends Frame {

	private static final long serialVersionUID = 1L; // Since serializable

	public static void main(String[] args) {
		new Tetris();
	}

	Tetris() {
		super("Tetris");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(600, 600);
		TetrisCanvas game = new TetrisCanvas();
		add("Center", game);
		game.start();
		setVisible(true);
	}
}

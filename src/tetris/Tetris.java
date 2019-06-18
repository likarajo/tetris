package tetris;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Tetris extends Frame {

	private static final long serialVersionUID = 1L;

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
		Layer layer = new Layer();
		add("Center", layer);
		layer.start();
		setVisible(true);
	}
}

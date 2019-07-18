package tetris;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;

	Game() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(600, 600);
		//setIgnoreRepaint(true);
		TetrisCanvas tetris = new TetrisCanvas();
		add("Center", tetris);
		tetris.start();
		setVisible(true);
	}

}

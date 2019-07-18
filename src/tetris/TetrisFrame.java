package tetris;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class TetrisFrame extends Frame {

	private static final long serialVersionUID = 1L;

	TetrisFrame(TetrisCanvas canvas) {
		super("Tetris");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(Tetris.BLOCKSIZE * (Tetris.MAINAREA_WIDTH + 5) + 3 * 10,
				Tetris.BLOCKSIZE * Tetris.MAINAREA_HEIGHT + 10 * 2);
		add("Center", canvas);
		setVisible(true);
	}
}

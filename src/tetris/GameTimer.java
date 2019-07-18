package tetris;

import java.util.TimerTask;

/**
 * Class for Game Timer
 */
class GameTimer extends TimerTask {
	GameTimer() {
	}

	@Override
	public void run() {
		if (Tetris.getInstance().moveOrChange(Action.MoveDown))
			Tetris.getInstance().canvas.repaint();
	}
}

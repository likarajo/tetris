package tetris;

import javax.swing.Timer;

public class GameThread {
	
	public Timer timer;
	
	GameThread(Timer timer) {
		this.timer = timer;
	}

	void start() {
		timer.start();
	}

	void stop() {
		timer.stop();
	}

	public Timer getTimer() {
		return timer;
	}

}
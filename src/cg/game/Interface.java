package cg.game;

/** @author Rajarshi Chattopadhyay
* Squares.java: This program draws concentric squares rotated by 45 degrees to one another.
*/

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Interface extends Frame {
	public static void main(String[] args) {
		new Interface();
	}

	Interface() {
		super("Tetris: Intrerface for Tetris game");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(600, 600);
		add("Center", new InterfaceCanvas());
		setVisible(true);
	}
}

@SuppressWarnings("serial")
class InterfaceCanvas extends Canvas {
	int maxX, maxY, centerX, centerY;
	int min_d;
	int sqSize;
	float pixelSize, rWidth = 600, rHeight = 600;
	int level=1;
	int lines=0;
	int score=0;

	int iX(float x) {
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y) {
		return Math.round(centerY - y / pixelSize);
	}

	void initgr() {
		Dimension d = getSize();
		maxX = d.width - 1;
		maxY = d.height - 1;
		pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;
		min_d = Math.min(maxX, maxY);
		sqSize = min_d / 10;

	}

	public void paint(Graphics g) {

		initgr();
		
		g.drawRect(iX(-250),iY(250),Math.round(250/pixelSize),Math.round(500/pixelSize));
		g.drawRect(iX(50),iY(250),Math.round(170/pixelSize),Math.round(100/pixelSize));
		g.drawRect(iX(50),iY(-210),Math.round(100/pixelSize),Math.round(40/pixelSize));
		
		g.setFont(new Font("Helvetica",Font.BOLD,(int)(20/pixelSize)));
		g.drawString("Level:                        "+level, iX(50), iY(50));
		g.drawString("Lines:                        "+lines, iX(50), iY(0));
		g.drawString("Score:                       "+score, iX(50), iY(-50));
		g.drawString("QUIT", iX(75), iY(-235));

	}
}

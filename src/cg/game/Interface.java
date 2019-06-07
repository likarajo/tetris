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
	int maxX, maxY;
	int centerX, centerY;
	int cx, cy;
	float pixelSize, rWidth = 600, rHeight = 600;
	int level = 1;
	int lines = 0;
	int score = 0;
	int[][] mainlayer = new int[10][20];
	Color[][] layerColor = new Color[10][20];
	Shape current;
	Shape next;

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
		pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;
	}

	void drawbaselayer(Graphics g) {
		// playing area
		g.drawRect(iX(-250), iY(250), Math.round(250 / pixelSize), Math.round(500 / pixelSize));

		// next shape area
		g.drawRect(iX(50), iY(250), Math.round(170 / pixelSize), Math.round(100 / pixelSize));

		// quit button
		g.drawRect(iX(50), iY(-210), Math.round(100 / pixelSize), Math.round(40 / pixelSize));

		// strings
		g.setFont(new Font("Helvetica", Font.BOLD, (int) (20 / pixelSize)));
		g.drawString("Level:                        " + level, iX(50), iY(50));
		g.drawString("Lines:                        " + lines, iX(50), iY(0));
		g.drawString("Score:                       " + score, iX(50), iY(-50));
		g.drawString("QUIT", iX(75), iY(-235));
	}

	void fillmain(Graphics g) {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				if (mainlayer[x][y] == 1) {
					x = iX(-250 + 25 * (x + 1) - 25);
					y = iY(-250 + 25 * (y + 1));
					g.setColor(layerColor[x][y]);
					g.fillRect(x + 1, y + 1, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
					g.setColor(Color.black);
					g.drawRect(x + 1, y + 1, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
				}

			}
		}
	}

	public void fillnext(Graphics g) {
		for (int i = 0; i < 4; ++i) {
			int x = next.x(i);
			int y = next.y(i);
			x = iX(160 - 25 * 2 + 25 * x);
			y = iY(240 - 25 + 25 * y);
			g.setColor(Color.RED);
			g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
			g.setColor(Color.BLACK);
			g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		}

	}

	public void fillcurrent(Graphics g) {
		for (int i = 0; i < 4; ++i) {
			int x = cx + current.x(i);
			int y = cy + current.y(i);
			x = iX(-250 + 25 * x - 25);
			y = iY(-250 + 25 * y);
			g.setColor(Color.GREEN);
			g.fillRect(x + 1, y + 1, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
			g.setColor(Color.BLACK);
			g.drawRect(x + 1, y + 1, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		
		}

	}

	public void paint(Graphics g) {

		initgr();
		baselayer(g);

	}
}

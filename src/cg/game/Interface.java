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
		super("Tetris Game");
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
class InterfaceCanvas extends Canvas // implements MouseListener, MouseMotionListener
{
	int maxX, maxY;
	int centerX, centerY;
	int cx, cy;
	float pixelSize, rWidth = 600, rHeight = 600;
	int level = 1;
	int lines = 0;
	int score = 0;
	boolean hover = false;

	InterfaceCanvas() {
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;

				if (mouseX > -250 && mouseX < 0 && mouseY > -250 && mouseY < 250) {
					hover = true;
					repaint();
				} else {
					hover = false;
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// System.exit(1);
				float mouseX = (e.getX() - centerX) * pixelSize;
				float mouseY = (centerY - e.getY()) * pixelSize;

				if (mouseX > 50 && mouseX < 150 && mouseY > -250 && mouseY < -210) {
					System.exit(1);

				}
			}
		});
	}

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

	void mainlayer(Graphics g, int x, int y, Color c) {
		x = iX(-100 + 25 * x + 25);
		y = iY(-225 + 25 * y + 25);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void nextlayer(Graphics g, int x, int y, Color c) {
		x = iX(170 - 25 * 2 + 25 * x);
		y = iY(250 - 25 + 25 * y);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void currentlayer(Graphics g, int x, int y, Color c) {
		x = iX(-125 + 25 * x);
		y = iY(125 + 25 * y + 25);
		g.setColor(c);
		g.fillRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, Math.round(25 / pixelSize), Math.round(25 / pixelSize));
	}

	void fillmain(Graphics g) {
		int x = -1;
		int y = -1;
		mainlayer(g, x, y, Color.YELLOW);
		x = 0;
		y = -1;
		mainlayer(g, x, y, Color.YELLOW);
		x = 0;
		y = 0;
		mainlayer(g, x, y, Color.YELLOW);
		x = 1;
		y = 0;
		mainlayer(g, x, y, Color.YELLOW);
		x = 1;
		y = -1;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = -1;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = 0;
		mainlayer(g, x, y, Color.BLUE);
		x = 2;
		y = 1;
		mainlayer(g, x, y, Color.BLUE);
	}

	void fillnext(Graphics g) {
		int x = -1;
		int y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 0;
		y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 1;
		y = -1;
		nextlayer(g, x, y, Color.RED);
		x = 1;
		y = 0;
		nextlayer(g, x, y, Color.RED);
	}

	void fillcurrent(Graphics g) {
		int x = -1;
		int y = -1;
		currentlayer(g, x, y, Color.GREEN);
		x = -1;
		y = 0;
		currentlayer(g, x, y, Color.GREEN);
		x = 0;
		y = -1;
		currentlayer(g, x, y, Color.GREEN);
		x = 0;
		y = 0;
		currentlayer(g, x, y, Color.GREEN);

	}

	public void paint(Graphics g) {

		initgr();
		drawbaselayer(g);
		fillnext(g);
		fillcurrent(g);
		fillmain(g);

		if (hover) {
			g.setColor(Color.blue);
			g.drawRect(iX(-125 - 60), iY(25), Math.round(120 / pixelSize), Math.round(50 / pixelSize));
			g.drawString("PAUSE", iX(-160), iY(-5));
			repaint();
		}

	}

}

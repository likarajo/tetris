package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import api.Conversions;

public class GameBorder extends Component {
	
	private static final long serialVersionUID = 463170031465934857L;
	
	private Rectangle border;
	
	//constructor with width and height argument
	public GameBorder(int width, int height) {
		border = new Rectangle(0, 0, width - 1, height - 1);
	}
	
	@Override
	public void paint(Graphics graphics) {
		//the game border is a discrete construct
		Conversions.drawRect(graphics, border, false);
	}
}

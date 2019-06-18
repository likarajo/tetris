package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import entities.TetrisGame;
import api.Conversions;

public class PauseIndicator extends Component {
	
	private static final long serialVersionUID = -7790433383646677788L;

	public static Rectangle PLAY_AREA_OVERLAY;
	
	private TetrisGame game;
	private Font pausedFont;
	private Color pausedFontColor;
	private Color playAreaOverlayColor;
	private boolean hovered;
	
	public PauseIndicator(TetrisGame game) { 
		this.game = game;
		
		PLAY_AREA_OVERLAY = new Rectangle (
				PlayArea.FRAME.x + 1,
				PlayArea.FRAME.y + 1,
				PlayArea.FRAME.width - 1,
				PlayArea.FRAME.height - 1);
		
		pausedFont = new Font("Arial", Font.BOLD, 16);
		pausedFontColor = new Color(0, 112, 192);
		playAreaOverlayColor = new Color(255, 255, 255, 128);
	}
	
	@Override
	public void paint(Graphics graphics) {
		if (game.isPaused()) {
			Conversions.fillRect(graphics, PLAY_AREA_OVERLAY, playAreaOverlayColor);

			graphics.setColor(pausedFontColor);
			graphics.setFont(Conversions.toDevice(pausedFont));
			
			//note: the offset below is just a value I felt looked nice
			
			Conversions.drawString(graphics, "PAUSE", 
					PlayArea.OFFSET_X + PlayArea.FRAME.width / 2 - 24, 
					PlayArea.OFFSET_Y + PlayArea.FRAME.height / 2 - 8);
		}
	}
	
	//getters and setters
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
		
		//change font style if hovered
		if (hovered) {
			game.setPaused(true);
		}
		else {
			game.setPaused(false);
		}
	}
	public boolean isHovered() { return hovered; }
}
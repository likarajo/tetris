package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import api.Conversions;
import entities.blocks.Block;

public class BlockPainter {
	public static final int PIXEL_SIZE = 24;
	
	public static void paint(Graphics graphics, Block block, 
			int offsetX, int offsetY) {
		//check if block exists
		if (null != block) {
			Rectangle rect = new Rectangle(
					offsetX + block.getPosition().x * PIXEL_SIZE,
					offsetY + block.getPosition().y * PIXEL_SIZE,
					BlockPainter.PIXEL_SIZE - 1,
					BlockPainter.PIXEL_SIZE - 1);
			
			graphics.setColor(block.getColor());
			Conversions.fillRect(graphics, rect);
			
			graphics.setColor(Color.BLACK);
			Conversions.drawRect(graphics, rect);
		}
	}
}

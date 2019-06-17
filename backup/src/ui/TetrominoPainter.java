package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import ui.components.PlayArea;
import api.Conversions;
import entities.blocks.Block;
import entities.blocks.Tetromino;

public class TetrominoPainter {
	
	public static void paint(Graphics graphics, Tetromino tetromino, 
			int offsetX, int offsetY) {
		//check if Tetromino exists
		if (null != tetromino) {
			for(Block block : tetromino.getBlocks()) {
				
				Point blockOffset = new Point(
						tetromino.getPosition().x + block.getPosition().x,
						tetromino.getPosition().y + block.getPosition().y);
				
				Rectangle rect = new Rectangle(
						offsetX + blockOffset.x * BlockPainter.PIXEL_SIZE,
						offsetY + blockOffset.y * BlockPainter.PIXEL_SIZE,
						BlockPainter.PIXEL_SIZE - 1,
						BlockPainter.PIXEL_SIZE - 1);
				
				graphics.setColor(block.getColor());
				Conversions.fillRect(graphics, rect);
				
				graphics.setColor(Color.BLACK);
				Conversions.drawRect(graphics, rect);
			}
		}
	}
	
	public static void paintGhost(Graphics graphics, Tetromino tetromino, 
			int offsetX, int offsetY) {
		//check if Tetromino exists
		if (null != tetromino) {
			for(Block block : tetromino.getBlocks()) {
				
				Point blockOffset = new Point(
						tetromino.getPosition().x + block.getPosition().x,
						tetromino.getPosition().y + block.getPosition().y);
				
				Rectangle rect = new Rectangle(
						offsetX + blockOffset.x * BlockPainter.PIXEL_SIZE + 2,
						offsetY + blockOffset.y * BlockPainter.PIXEL_SIZE + 2,
						BlockPainter.PIXEL_SIZE - 5,
						BlockPainter.PIXEL_SIZE - 5);
				
				graphics.setColor(block.getColor());
				Conversions.drawRect(graphics, rect);
			}
		}
	}
	
	public static boolean contains(Tetromino tetromino, float x, float y,  
			int offsetX, int offsetY) {
		for(Block block : tetromino.getBlocks()) {
			Point blockOffset = new Point(
					tetromino.getPosition().x + block.getPosition().x,
					tetromino.getPosition().y + block.getPosition().y);
			
			Rectangle rect = new Rectangle(
					offsetX + blockOffset.x * BlockPainter.PIXEL_SIZE,
					offsetY + blockOffset.y * BlockPainter.PIXEL_SIZE,
					BlockPainter.PIXEL_SIZE,
					BlockPainter.PIXEL_SIZE);
			
			if (rect.contains(x, y)) { return true; }
		}
		
		return false;
	}
}

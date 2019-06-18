package api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Conversions {
	public static float logicalWidth = 0.f;
	public static float logicalHeight = 0.f;
	public static float logicalCenterX = 0.f;
	public static float logicalCenterY = 0.f;
	public static float pixelSize = 0.f;
	public static int deviceCenterX = 0;
	public static int deviceCenterY = 0;
	
	public static void setLogicalDimensions(float logicalWidth, 
			float logicalHeight) {
		Conversions.logicalWidth = logicalWidth;
		Conversions.logicalHeight = logicalHeight;
		Conversions.logicalCenterX = logicalWidth / 2.f;
		Conversions.logicalCenterY = logicalHeight / 2.f;
	}
	
	public static void setDeviceDimensions(Dimension deviceSize) {
		Conversions.pixelSize = Math.max(
				logicalWidth / deviceSize.width, 
				logicalHeight / deviceSize.height);
		
		Conversions.deviceCenterX = deviceSize.width >> 1;
		Conversions.deviceCenterY = deviceSize.height >> 1;
	}
	
	public static int toDeviceX(float x) {
		return Math.round(deviceCenterX + (x - logicalCenterX) / pixelSize);
	}
	
	public static int toDeviceY(float y) {
		return Math.round(deviceCenterY + (y - logicalCenterY) / pixelSize);
	}
	
	public static float toLogicalX(float x) {
		return (x - deviceCenterX) * pixelSize + logicalCenterX;
	}
	
	public static float toLogicalY(float y) {
		return (y - deviceCenterY) * pixelSize + logicalCenterY;
	}
	
	public static Point toDevice(PointF point) {
		return new Point(toDeviceX(point.x), toDeviceY(point.y));
	}
	
	public static PointF toLogical(Point point) {
		return new PointF(toLogicalX(point.x), toLogicalY(point.y));
	}
	
	//note: consider extracting methods below into a new class
	
	//draws a rectangle or that rectangle padded by 0.5f
	public static void drawRect(Graphics graphics, Rectangle rect, boolean padded) {
		if (padded) {
			//note: we pad by 0.5f to remove the one logical pixel distance 
			//caused by using discrete points in the logical coordinate system
			graphics.drawRect(
					toDeviceX(rect.x - 0.5f),
					toDeviceY(rect.y - 0.5f),
					Math.round((rect.width + 1.0f) / pixelSize),
					Math.round((rect.height + 1.0f) / pixelSize));
		}
		else {
			graphics.drawRect(
					toDeviceX(rect.x),
					toDeviceY(rect.y),
					Math.round(rect.width / pixelSize),
					Math.round(rect.height / pixelSize));
		}
	}
	
	//sets color, then draws a rectangle or that rectangle padded by 0.5f
	public static void drawRect(Graphics graphics, 
			Rectangle rect, Color color, boolean padded) {
		graphics.setColor(color);
		drawRect(graphics, rect, padded);
	}
		
	//sets color, then draws a rectangle padded by 0.5f
	public static void drawRect(Graphics graphics, 
			Rectangle rect, Color color) {
		graphics.setColor(color);
		drawRect(graphics, rect, true);
	}
	
	//draws a rectangle padded by 0.5f
	public static void drawRect(Graphics graphics, 
			Rectangle rect) {
		drawRect(graphics, rect, true);
	}

	//fills a rectangle or that rectangle padded by 0.5f
	public static void fillRect(Graphics graphics, Rectangle rect, boolean padded) {
		if (padded) {
			//note: we pad by 0.5f to remove the one logical pixel distance 
			//caused by using discrete points in the logical coordinate system
			graphics.fillRect(
					toDeviceX(rect.x - 0.5f),
					toDeviceY(rect.y - 0.5f),
					Math.round((rect.width + 1.0f) / pixelSize),
					Math.round((rect.height + 1.0f) / pixelSize));
		}
		else {
			graphics.fillRect(
					toDeviceX(rect.x),
					toDeviceY(rect.y),
					Math.round(rect.width / pixelSize),
					Math.round(rect.height / pixelSize));
		}
	}
	
	//sets color, then fills a rectangle or that rectangle padded by 0.5f
	public static void fillRect(Graphics graphics, 
			Rectangle rect, Color color, boolean padded) {
		graphics.setColor(color);
		fillRect(graphics, rect, padded);
	}
	
	//sets color, then fills a rectangle padded by 0.5f
	public static void fillRect(Graphics graphics, 
			Rectangle rect, Color color) {
		graphics.setColor(color);
		fillRect(graphics, rect, true);
	}
	
	//fills a rectangle padded by 0.5f
	public static void fillRect(Graphics graphics, 
			Rectangle rect) {
		//draw continuously as default
		fillRect(graphics, rect, true);
	}
	
	//returns font in device coordinates
	public static Font toDevice(Font font) {
		return new Font(
				font.getName(), 
				font.getStyle(), 
				Math.round(font.getSize() / pixelSize));
	}
	
	//draws given string
	public static void drawString(Graphics graphics,
			String str, int offsetX, int offsetY) {
		graphics.drawString(str, toDeviceX(offsetX), toDeviceY(offsetY));
	}
}
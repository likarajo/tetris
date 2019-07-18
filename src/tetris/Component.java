package tetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class Component {
	float x, y, w, h;
	Color borderColor = Color.black;
	Color fillColor = null;
	Color textColor = Color.black;
	String text;
	boolean hidden = false;
	List<Component> subComponents = new ArrayList<>();

	public Component() {
	}

	public Component(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void addSubComponent(Component component) {
		subComponents.add(component);
	}

	public boolean inside(float px, float py) {
		return px >= x && px <= x + w && py >= y && py <= y + h;
	}

	public float relativeX(float x) {
		return x + this.x;
	}

	public float relativeY(float y) {
		return y + this.y;
	}

	public float centerX() {
		return x + w / 2;
	}

	public float centerY() {
		return y + h / 2;
	}

	public void setCenter(float cx, float cy) {
		x = cx - w / 2;
		y = cy - h / 2;
	}
}

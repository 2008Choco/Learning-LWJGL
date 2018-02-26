package me.choco.learning.engine.rendering.text;

public class Glyph {
	
	private final char character;
	private final int width, height;
	private final int x, y;
	
	public Glyph(char character, int width, int height, int x, int y) {
		this.character = character;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
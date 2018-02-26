package me.choco.learning.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

public final class GameConfig {
	
	private boolean dirty;
	
	private String title = "";
	private int width = 1080, height = 720;
	private int maxFPS = Integer.MAX_VALUE, maxUPS = 20;
	private boolean vsync = false;
	
	private GameConfig() {}
	
	public static GameConfig newConfig() {
		return new GameConfig();
	}
	
	public GameConfig setTitle(String title) {
		this.title = title;
		this.dirty = true;
		return this;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public GameConfig setWidth(int width) {
		this.width = width;
		this.dirty = true;
		return this;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public GameConfig setHeight(int height) {
		this.height = height;
		this.dirty = true;
		return this;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public GameConfig setMaxFPS(int maxFPS) {
		this.maxFPS = maxFPS;
		this.dirty = true;
		return this;
	}
	
	public int getMaxFPS() {
		return this.maxFPS;
	}
	
	public GameConfig setMaxUPS(int maxUPS) {
		this.maxUPS = maxUPS;
		this.dirty = true;
		return this;
	}
	
	public int getMaxUPS() {
		return this.maxUPS;
	}
	
	public GameConfig setVsync(boolean vsync) {
		this.vsync = vsync;
		this.dirty = true;
		return this;
	}
	
	public boolean isVsync() {
		return this.vsync;
	}
	
	protected Window constructNewWindow() {
		Window window = new Window(title, width, height);
		window.setAsActiveContext();
		window.show();
		return window;
	}
	
	protected void updateGameState(GameBase base) {
		if (!dirty) return;
		
		base.window.setAsActiveContext();
		base.window.setTitle(title);
		base.window.setWidth(width);
		base.window.setHeight(height);
		glfwSwapInterval(vsync ? 1 : 0);
		
		this.dirty = false;
	}
	
}
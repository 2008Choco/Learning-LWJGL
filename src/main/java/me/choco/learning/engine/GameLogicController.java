package me.choco.learning.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.joml.Vector3f;

import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.camera.CameraMouseInput;
import me.choco.learning.engine.rendering.Renderer;

public abstract class GameLogicController {
	
	protected boolean vsync = false;
	protected int maxFPS, maxUPS;
	protected int currentFPS, currentUPS;
	
	protected boolean running = true;
	protected Window window;
	protected Renderer renderer;
	
	protected Camera camera = new Camera();
	protected final Vector3f cameraDelta = new Vector3f();
	protected final CameraMouseInput mouseInput = new CameraMouseInput();
	
	private final String title;
	
	public GameLogicController(String title) {
		this.title = title;
		this.init();
	}
	
	public final void run() {
		long startTime = System.currentTimeMillis();
		long lastTimeUPS = System.nanoTime(), lastTimeFPS = System.nanoTime();
		double deltaFPS = 0.0, deltaUPS = 0.0;
		double nsFPS = 1_000_000_000.0 / maxFPS, nsUPS = 1_000_000_000.0 / maxUPS;
		
		while (running) {
			// Game updates
			long now = System.nanoTime();
			deltaUPS += (now - lastTimeUPS) / nsUPS;
			lastTimeUPS = now;
			
			if (deltaUPS >= 1.0) {
				this.update();
				this.handleInput();
				this.currentUPS++;
				deltaUPS--;
			}
			
			// Frame render updates
			now = System.nanoTime();
			deltaFPS += (now - lastTimeFPS) / nsFPS;
			lastTimeFPS = now;
			
			if (deltaFPS >= 1.0) {
				this.render();
				this.currentFPS++;
				deltaFPS--;
			}
			
			// FPS and UPS display
			if (System.currentTimeMillis() - startTime > 1000) {
				startTime += 1000;
				this.window.setTitle(title + " - (FPS: " + this.currentFPS + " | UPS: " + this.currentUPS + ")");
				this.currentUPS = 0;
				this.currentFPS = 0;
			}
			
			if (glfwWindowShouldClose(window.getId())) {
				this.running = false;
			}
		}

		this.cleanup();
	}
	
	public void setVSync(boolean vsync) {
		glfwSwapInterval((this.vsync = vsync) ? 1 : 0);
	}
	
	public boolean isVsync() {
		return vsync;
	}
	
	public void setMaxFPS(int maxFPS) {
		this.maxFPS = maxFPS;
	}
	
	public int getMaxFPS() {
		return maxFPS;
	}
	
	public void setMaxUPS(int maxUPS) {
		this.maxUPS = maxUPS;
	}
	
	public int getMaxUPS() {
		return maxUPS;
	}
	
	public int getCurrentFPS() {
		return currentFPS;
	}
	
	public int getCurrentUPS() {
		return currentUPS;
	}
	
	public void setWindow(Window window) {
		this.window = window;
	}
	
	public Window getWindow() {
		return window;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render();
	
	public abstract void handleInput();
	
	public abstract void cleanup();
	
}
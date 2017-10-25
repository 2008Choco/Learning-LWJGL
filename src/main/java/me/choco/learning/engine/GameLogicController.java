package me.choco.learning.engine;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.joml.Vector3f;

import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.camera.CameraMouseInput;
import me.choco.learning.engine.rendering.Renderer;

/**
 * Represents the base class for all game logic handlers. Any game implementation
 * should extend this class and initialize its variables through the use of the
 * protected methods. Failure to initialize a field in the {@link #init()} method
 * may result in a runtime exception
 */
public abstract class GameLogicController {
	
	protected boolean vsync = false;
	protected int maxFPS, maxUPS;
	protected int currentFPS, currentUPS;
	
	private boolean running = true;
	protected Window window;
	protected Renderer renderer;
	
	protected Camera camera = new Camera();
	protected final Vector3f cameraDelta = new Vector3f();
	protected final CameraMouseInput mouseInput = new CameraMouseInput();
	
	private final String title;
	
	/**
	 * Construct a new GameLogicController and provide it with a game title
	 * 
	 * @param title the title of the game. Will be displayed on the window
	 */
	public GameLogicController(String title) {
		this.title = title;
		this.init();
		this.run();
	}
	
	private final void run() {
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
	
	/**
	 * Initialize this game with a specific Renderer implementation and
	 * invoke its {@link Renderer#init()} method
	 * 
	 * @param renderer the renderer to set and initialize
	 */
	protected void initRenderer(Renderer renderer) {
		this.renderer = renderer;
		this.renderer.init();
	}
	
	/**
	 * Set the renderer implementation to use without invoking its init method
	 * 
	 * @param renderer the renderer to set
	 */
	protected void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	/**
	 * Get the renderer used for this game logic controller
	 * 
	 * @return the game renderer
	 */
	public Renderer getRenderer() {
		return renderer;
	}
	
	/**
	 * Set the window context to be drawn to for this game logic controller
	 * 
	 * @param window the new window draw context
	 */
	protected void setWindowContext(Window window) {
		this.window = window;
		this.window.setAsActiveContext();
		this.window.show();
	}
	
	/**
	 * Get the active window draw context
	 * 
	 * @return the active game window
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * Set the world camera to be used for this logic controller
	 * 
	 * @param camera the camera to set
	 */
	protected void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * Get the active game engine camera
	 * 
	 * @return the active game camera
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Set whether vsync should be used or not
	 * 
	 * @param vsync true if vsync should be used. false otherwise
	 */
	protected void setVSync(boolean vsync) {
		glfwSwapInterval((this.vsync = vsync) ? 1 : 0);
	}
	
	/**
	 * Check whether vsync is being used in this current context or not
	 * 
	 * @return true if vsync is enabled, false otherwise
	 */
	public boolean isVsync() {
		return vsync;
	}
	
	/**
	 * Set the maximum FPS cap to be used when rendering this game
	 * 
	 * @param maxFPS the new maximum FPS
	 */
	protected void setMaxFPS(int maxFPS) {
		this.maxFPS = maxFPS;
	}
	
	/**
	 * Get the maximum amount of FPS for this game
	 * 
	 * @return the maximum FPS
	 */
	public int getMaxFPS() {
		return maxFPS;
	}
	
	/**
	 * Set the maximum UPS cap (updates) to be used when updating this game
	 * 
	 * @param maxUPS the new maximum UPS
	 */
	protected void setMaxUPS(int maxUPS) {
		this.maxUPS = maxUPS;
	}
	
	/**
	 * Get the maximum amount of UPS for this game
	 * 
	 * @return the maximum UPS
	 */
	public int getMaxUPS() {
		return maxUPS;
	}
	
	/**
	 * Get the current FPS as of invoking this method
	 * 
	 * @return the current FPS
	 */
	public int getCurrentFPS() {
		return currentFPS;
	}
	
	/**
	 * Get the current UPS as of invoking this method
	 * 
	 * @return the current UPS
	 */
	public int getCurrentUPS() {
		return currentUPS;
	}
	
	/**
	 * Initialize the game. This method is invoked immediately after the constructor has
	 * been called. Here is where logic controller fields / values should be declared
	 * and initialized, as well as the initialization of OpenGL, GLFW, etc.
	 */
	public abstract void init();
	
	/**
	 * Update the game and all its static / dynamic entities. Called according to
	 * {@link #getMaxUPS()}
	 */
	public abstract void update();
	
	/**
	 * Render the game to the screen. Called according to {@link #getMaxFPS()}
	 */
	public abstract void render();
	
	/**
	 * Handle the game input. Called according to {@link #getMaxUPS()}
	 */
	public abstract void handleInput();
	
	/**
	 * Cleanup any outstanding data that should be cleared before the engine halts.
	 * Called once at the end of the game loop
	 */
	public abstract void cleanup();
	
}
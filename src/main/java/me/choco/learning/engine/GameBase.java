package me.choco.learning.engine;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.camera.CameraMouseInput;
import me.choco.learning.engine.model.ObjectModel;
import me.choco.learning.engine.rendering.Renderer;

/**
 * Represents the base class for all game logic handlers. Any game implementation
 * should extend this class and initialize its variables through the use of the
 * protected methods. Failure to initialize a field in the {@link #init()} method
 * may result in a runtime exception
 */
public abstract class GameBase {
	
	protected int currentFPS, currentUPS;
	private boolean running = true;
	
	protected Window window;
	protected Renderer renderer;
	private final List<ObjectModel> renderQueue = new ArrayList<>(); // Clean this up... I hate this
	
	protected Camera camera = new Camera();
	protected final Vector3f cameraDelta = new Vector3f();
	protected final CameraMouseInput mouseInput = new CameraMouseInput();
	
	private final GameConfig config;
	
	/**
	 * Construct a new GameLogicController and provide it with a game title
	 * 
	 * @param config the game's configuration
	 */
	public GameBase(GameConfig config) {
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		
		// Declare and initialize configuration
		this.config = config;
		this.window = config.constructNewWindow();
		
		// Initialize OpenGL contexts
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		glPolygonMode(GL_FRONT_AND_BACK, GL_TRIANGLES);
		
		// Initialize game-specific features
		this.mouseInput.init(window);
		
		this.init();
		this.run();
	}
	
	private final void run() {
		long startTime = System.currentTimeMillis();
		long lastTimeUPS = System.nanoTime(), lastTimeFPS = System.nanoTime();
		double deltaFPS = 0.0, deltaUPS = 0.0;
		double nsFPS = 1_000_000_000.0 / config.getMaxFPS(), nsUPS = 1_000_000_000.0 / config.getMaxUPS();
		
		while (running) {
			// Game updates
			long now = System.nanoTime();
			deltaUPS += (now - lastTimeUPS) / nsUPS;
			lastTimeUPS = now;
			
			if (deltaUPS >= 1.0) {
				this.update();
				this.handleInput();
				this.config.updateGameState(this);
				
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
				this.window.setTitle(config.getTitle() + " - (FPS: " + this.currentFPS + " | UPS: " + this.currentUPS + ")");
				this.currentUPS = 0;
				this.currentFPS = 0;
			}
			
			if (glfwWindowShouldClose(window.getId())) {
				this.running = false;
			}
		}

		this.cleanup();
		glfwTerminate();
	}
	
	/**
	 * Render the game to the screen. Called according to {@link #getMaxFPS()}
	 */
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		this.renderer.render(renderQueue.toArray(new ObjectModel[renderQueue.size()])); // TODO: Ew
		
		glfwSwapBuffers(window.getId());
	}
	
	/**
	 * Add the specified model to the render queue to be rendered
	 * 
	 * @param model the model to render
	 */
	public void addToRenderQueue(ObjectModel model) {
		this.renderQueue.add(model);
	}
	
	/**
	 * Get this game's configuration
	 * 
	 * @return the configuration
	 */
	public GameConfig getConfig() {
		return this.config;
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
	 * Handle the game input. Called according to {@link #getMaxUPS()}
	 */
	public abstract void handleInput();
	
	/**
	 * Cleanup any outstanding data that should be cleared before the engine halts.
	 * Called once at the end of the game loop
	 */
	public abstract void cleanup();
	
}
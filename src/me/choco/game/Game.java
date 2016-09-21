package me.choco.game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import me.choco.game.entities.Camera;
import me.choco.game.entities.Entity;
import me.choco.game.entities.generic.Light;
import me.choco.game.models.RawModel;
import me.choco.game.models.TexturedModel;
import me.choco.game.models.textures.ModelTexture;
import me.choco.game.utils.Loader;
import me.choco.game.utils.modelling.OBJLoader;
import me.choco.game.utils.output.Screenshot;
import me.choco.game.utils.rendering.MasterRenderer;

public class Game {
	
	/* JAVADOCS: https://javadoc.lwjgl.org/ */
	
	private long window;
	public static final int WIDTH = 720, HEIGHT = 480;
	
	private final boolean[] keys = new boolean[1024];

	private MasterRenderer renderer;
	private final Loader loader = new Loader();
	private final Camera camera = new Camera();
	
	public Game() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		
		try {
			init();
			run();

			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		} finally {
			glfwTerminate();
		}
	}

	private void init() {
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(WIDTH, HEIGHT, "Learning LWJGL", NULL, NULL);
		if (window == NULL) throw new UnsupportedOperationException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS){
				this.keys[key] = true;
				
				// Screenshot key
				if (key == GLFW_KEY_F2){
					new Screenshot().saveToDesktop();
					System.out.println("Screenshot saved to desktop");
				}
				
				if (key == GLFW_KEY_F3){
					if (this.renderer != null){
						this.renderer.setWireframeMode(!this.renderer.isWireframeMode());
					}
				}
			}
			else if (action == GLFW_RELEASE)
				this.keys[key] = false;
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
			window,
			(vidmode.width() - WIDTH) / 2,
			(vidmode.height() - HEIGHT) / 2
		);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
	}

	private void run() {
		GL.createCapabilities();
		
		Light light = new Light(new Vector3f(-20000,20000,-20000), new Vector3f(1, 1, 1));
		
		RawModel rawModel = OBJLoader.loadModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TexturedModel model = new TexturedModel(rawModel, texture);
		
		Entity entity = new Entity(model, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		
		// Terrain
//		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass"));
//		Terrain terrain = new Terrain(0, -1, loader, grassTexture);
//		Terrain terrain2 = new Terrain(1, -1, loader, grassTexture);
		
		renderer = new MasterRenderer();
		while (!glfwWindowShouldClose(window)) {
			if (this.isKeyPressed(GLFW_KEY_R))
				entity.increaseRotation(0, 1, 0);
			
			// Handle key values
			float speed = camera.getSpeed();
			if (this.isKeyPressed(GLFW_KEY_W))
				camera.getPosition().z -= speed;
			if (this.isKeyPressed(GLFW_KEY_D))
				camera.getPosition().x += speed;
			if (this.isKeyPressed(GLFW_KEY_A))
				camera.getPosition().x -= speed;
			if (this.isKeyPressed(GLFW_KEY_S))
				camera.getPosition().z += speed;
			if (this.isKeyPressed(GLFW_KEY_SPACE))
				camera.getPosition().y += speed;
			if (this.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
				camera.getPosition().y -= speed;
			
			if (this.isKeyPressed(GLFW_KEY_ESCAPE))
				break;
			
			/* Start Render */
//			renderer.processTerrain(terrain);
//			renderer.processTerrain(terrain2);
			
			renderer.processEntity(entity);
			renderer.render(light, camera);
			/* End Render */
			
			glfwSwapBuffers(window);

			glfwPollEvents();
		}
		
		renderer.cleanShader();
		loader.clearBufferData();
	}
	
	// KEY UTILITIES
	public boolean isKeyPressed(int key){
		return keys[key];
	}
	
	public boolean areKeysPressed(int... keys){
		boolean allPressed = true;
		for (int key : keys)
			if (isKeyReleased(key)) allPressed = false;
		return allPressed;
	}
	
	public boolean isKeyReleased(int key){
		return !keys[key];
	}
	
	public boolean areKeysReleased(int... keys){
		boolean allReleased = true;
		for (int key : keys)
			if (isKeyPressed(key)) allReleased = false;
		return allReleased;
	}

	public static void main(String[] args) { new Game(); }
}
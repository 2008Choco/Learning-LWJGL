package me.choco.learning;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import me.choco.learning.engine.GameLogicController;
import me.choco.learning.engine.Window;
import me.choco.learning.engine.model.ModelLoader;
import me.choco.learning.engine.model.ObjectModel;
import me.choco.learning.engine.texture.Material;
import me.choco.learning.engine.texture.Texture;

public class LearningLWJGLGame extends GameLogicController {
	
	private static final boolean VSYNC = false;
	private static final int MAX_FPS = Integer.MAX_VALUE, MAX_UPS = 20;

	private static final float CAMERA_MOVEMENT_SPEED = 0.05f;
	private static final float MOUSE_SENSITIVITY = 0.35f;
	
	private ObjectModel model;
	
	public LearningLWJGLGame() {
		super("Learning LWJGL");
	}
	
	@Override
	public void init() {
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		
		this.setWindowContext(new Window("Learning LWJGL", 1080, 640));
		
		// Initialize OpenGL
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		glPolygonMode(GL_FRONT_AND_BACK, GL_TRIANGLES);
		
		// Declare necessary variables for logic controller
		this.setVSync(VSYNC);
		this.setMaxFPS(MAX_FPS);
		this.setMaxUPS(MAX_UPS);
		this.initRenderer(new LearningRenderer(window, camera));
		this.mouseInput.init(window);
        
		// Initialize the object model... temp
	    this.model = new ObjectModel(ModelLoader.loadOBJModel("/models/bunny.obj", new Material(new Texture("/textures/cube_texture.png"))));
	    this.model.setPosition(0, 0, -2);
	}
	
	@Override
	public void update() {
		// Update camera position
		this.camera.move(
			cameraDelta.x * CAMERA_MOVEMENT_SPEED, 
			cameraDelta.y * CAMERA_MOVEMENT_SPEED, 
			cameraDelta.z * CAMERA_MOVEMENT_SPEED
		);
		if (mouseInput.isRightButtonPressed()) {
			Vector2f display = mouseInput.getDisplayVec();
			this.camera.rotate(
				display.x * MOUSE_SENSITIVITY, 
				display.y * MOUSE_SENSITIVITY, 
				0
			);
		}
	}
	
	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		this.renderer.render(model);
		
		glfwSwapBuffers(window.getId());
		glfwPollEvents();
	}
	
	@Override
	public void handleInput() {
		this.mouseInput.update(window);
		this.cameraDelta.set(0, 0, 0);
		
		if (window.isKeyPressed(GLFW_KEY_W)) {
			this.cameraDelta.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			this.cameraDelta.z = 1;
		}
		
		if (window.isKeyPressed(GLFW_KEY_A)) {
			this.cameraDelta.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			this.cameraDelta.x = 1;
		}
		
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			this.cameraDelta.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			this.cameraDelta.y = 1;
		}
	}
	
	@Override
	public void cleanup() {
		this.renderer.cleanup();
		this.model.cleanup();
		this.window.destroy();
		
		glfwTerminate();
	}
	
}
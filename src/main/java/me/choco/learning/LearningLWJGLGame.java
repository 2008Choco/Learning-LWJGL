package me.choco.learning;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import me.choco.learning.engine.GameBase;
import me.choco.learning.engine.GameConfig;
import me.choco.learning.engine.model.ModelLoader;
import me.choco.learning.engine.model.ObjectModel;
import me.choco.learning.engine.texture.Material;
import me.choco.learning.engine.texture.Texture;

public class LearningLWJGLGame extends GameBase {
	
	private static final boolean VSYNC = false;
	private static final int MAX_FPS = Integer.MAX_VALUE, MAX_UPS = 20;

	private static final float CAMERA_MOVEMENT_SPEED = 0.05f;
	private static final float MOUSE_SENSITIVITY = 0.35f;
	
	private ObjectModel model;
	
	public LearningLWJGLGame() {
		super(GameConfig.newConfig()
				.setTitle("The Labyrinth")
				.setWidth(1080)
				.setHeight(640)
				.setVsync(VSYNC)
				.setMaxFPS(MAX_FPS)
				.setMaxUPS(MAX_UPS));
	}
	
	@Override
	public void init() {
		this.initRenderer(new LearningRenderer(window, camera));
        
		// Initialize the object model... temp
	    this.model = new ObjectModel(ModelLoader.loadOBJModel("/models/bunny.obj", new Material(new Texture("/textures/cube_texture.png"))));
	    this.model.setPosition(0, 0, -2);
	    this.addToRenderQueue(model);
	}
	
	@Override
	public void update() {
		glfwPollEvents();
		
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
		
		if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			this.model.getRotation().y -= 5;
		} else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			this.model.getRotation().y += 5;
		}
	}
	
	@Override
	public void cleanup() {
		this.renderer.cleanup();
		this.model.cleanup();
		this.window.destroy();
	}
	
}
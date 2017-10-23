package me.choco.learning.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;

/**
 * Represents an iconified window
 * 
 * @author Parker Hawke - 2008Choco
 */
public class Window {
	
	private final long id;
	
	private String title;
	private int width, height;
	
	/**
	 * Construct and display a new window
	 * 
	 * @param title the title of the window
	 * @param width the window width
	 * @param height the window height
	 */
	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		glfwDefaultWindowHints();
		this.id = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if (id == NULL)
			throw new RuntimeException("Could not create GLFW window");
		
		glfwSetFramebufferSizeCallback(id, (window, newWidth, newHeight) -> {
			this.width = newWidth;
			this.height = newHeight;
	        glViewport(0, 0, newWidth, newHeight);
		});
		
		// Center window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		glfwSetWindowPos(id,
			(vidmode.width() - width) / 2,
			(vidmode.height() - height) / 2
		);
		
		glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(id, true);
		});
	}
	
	/**
	 * Get the ID associated with this window as assigned by LWJGL
	 * 
	 * @return the window ID
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Set the title of this window
	 * 
	 * @param title the new title to set
	 */
	public void setTitle(String title) {
		glfwSetWindowTitle(id, title);
		this.title = title;
	}
	
	/**
	 * Get the title of this window
	 * 
	 * @return the window title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set the width of this window in pixels
	 * 
	 * @param width the new width to set
	 */
	public void setWidth(int width) {
		glfwSetWindowSize(id, width, height);
		glViewport(0, 0, width, height);
		this.width = width;
	}
	
	/**
	 * Get the width of this window in pixels
	 * 
	 * @return the window width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Set the height of this window in pixels
	 * 
	 * @param height the new height to set
	 */
	public void setHeight(int height) {
		glfwSetWindowSize(id, width, height);
		glViewport(0, 0, width, height);
		this.height = height;
	}
	
	/**
	 * Get the height of this window in pixels
	 * 
	 * @return the window height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Set the state of a window hint for this window
	 * 
	 * @param hint the hint to apply
	 * @param value the value of the hint
	 */
	public void setWindowHint(int hint, int value) {
		glfwWindowHint(hint, value);
	}
	
	/**
	 * Check whether a key is pressed or not
	 * 
	 * @param key the key to check
	 * @return true if the key is pressed. false otherwise
	 */
	public boolean isKeyPressed(int key) {
		return glfwGetKey(id, key) == GLFW_PRESS;
	}
	
	/**
	 * Show this window and display it on the screen
	 */
	public void show() {
		glfwShowWindow(id);
	}
	
	/**
	 * Hide this window from view
	 */
	public void hide() {
		glfwHideWindow(id);
	}
	
	/**
	 * Set this window as the active context
	 */
	public void setAsActiveContext() {
		glfwMakeContextCurrent(id);
	}
	
}
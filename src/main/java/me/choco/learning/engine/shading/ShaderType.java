package me.choco.learning.engine.shading;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * Represents a list of all possible types of shaders capable of being
 * loaded into a {@link ShaderProgram}
 * 
 * @author Parker Hawke - 2008Choco
 */
public enum ShaderType {
	
	/**
	 * A fragment shader used to process fragments in the rendering pipeline
	 */
	FRAGMENT(GL_FRAGMENT_SHADER),
	
	/**
	 * A vertex shader used to process vertex positions in the rendering pipeline
	 */
	VERTEX(GL_VERTEX_SHADER);
	
	
	private final int id;
	
	private ShaderType(int id) {
		this.id = id;
	}
	
	/**
	 * Get the OpenGL ID for this shader type (GL20)
	 * 
	 * @return the shader type id
	 */
	public int getId() {
		return id;
	}
	
}
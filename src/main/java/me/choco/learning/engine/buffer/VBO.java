package me.choco.learning.engine.buffer;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Represents a vertex buffer object which holds data in form of float buffers
 * 
 * @author Parker Hawke - 2008Choco
 */
public class VBO {
	
	private final int id, target;
	
	/**
	 * Construct a new float buffer VBO and generate its unique id
	 * 
	 * @param target the buffer type to create
	 * @param buffer the data to buffer into the VBO
	 */
	public VBO(int target, FloatBuffer buffer) {
		this.id = glGenBuffers();
		this.target = target;
		
		this.bind();
		glBufferData(target, buffer, GL_STATIC_DRAW);
	}
	
	/**
	 * Construct a new int buffer VBO and generate its unique id
	 * 
	 * @param target the buffer type to create
	 * @param buffer the data to buffer into the VBO
	 */
	public VBO(int target, IntBuffer buffer) {
		this.id = glGenBuffers();
		this.target = target;
		
		this.bind();
		glBufferData(target, buffer, GL_STATIC_DRAW);
	}
	
	/**
	 * Get this VBO's unique id
	 * 
	 * @return the VBO id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Bind this VBO as the active buffer
	 */
	public void bind() {
		glBindBuffer(target, id);
	}
	
	/**
	 * Unbind this VBO
	 */
	public void unbind() {
		glBindBuffer(target, 0);
	}
	
	/**
	 * Delete this buffer. Upon calling this method, this object is no longer usable
	 */
	public void delete() {
		this.unbind();
		glDeleteBuffers(id);
	}
	
}
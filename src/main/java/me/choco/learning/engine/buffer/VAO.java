package me.choco.learning.engine.buffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

/**
 * Represents a vertex array object that holds data to be stored on the GPU
 * 
 * @author Parker Hawke - 2008Choco
 */
public class VAO {
	
	private int attributePointers;
	private final int id;
	
	/**
	 * Construct and generate a new VAO
	 */
	public VAO() {
		this.id = glGenVertexArrays();
	}

	/**
	 * Create a new attribute pointer on the vertex array of type float
	 * 
	 * @param index the index to create the attribute pointer
	 * @param size the size of the data (i.e. 3 for 3D vectors)
	 */
	public void createAttribPointer(int index, int size) {
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		this.attributePointers++;
	}
	
	/**
	 * Enable an attribute array at the given index
	 * 
	 * @param index the index of the attribute to enable
	 */
	public void enableAttribArray(int index) {
		glEnableVertexAttribArray(index);
	}
	
	/**
	 * Enable all available attribute arrays in this VAO
	 */
	public void enableAllAttribArrays() {
		for (int i = 0; i < attributePointers; i++) {
			glEnableVertexAttribArray(i);
		}
	}
	
	/**
	 * Disable an attribute array at the given index
	 * 
	 * @param index the index of the attribute to disable
	 */
	public void disableAttribArray(int index) {
		glDisableVertexAttribArray(index);
	}
	
	/**
	 * Disable all available attribute arrays in this VAO
	 */
	public void disableAllAttribArrays() {
		for (int i = 0; i < attributePointers; i++) {
			glDisableVertexAttribArray(i);
		}
	}
	
	/**
	 * Bind this VAO as the active vertex array
	 */
	public void bind() {
		glBindVertexArray(id);
	}
	
	/**
	 * Unbind this VAO
	 */
	public void unbind() {
		glBindVertexArray(0);
	}
	
	/**
	 * Delete this vertex array. Upon calling this method, this object is no 
	 * longer usable
	 */
	public void delete() {
		this.disableAllAttribArrays();
		glDeleteVertexArrays(id);
	}
	
}
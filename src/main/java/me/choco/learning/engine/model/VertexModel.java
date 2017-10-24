package me.choco.learning.engine.model;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import me.choco.learning.engine.buffer.VAO;
import me.choco.learning.engine.buffer.VBO;
import me.choco.learning.engine.texture.Material;

/**
 * Represents the internal features of an {@link ObjectModel} including its
 * vertices, indices, texture coordinates and normals
 * 
 * @author Parker Hawke - 2008Choco
 */
public class VertexModel {
	
	private final VAO vao;
	private final VBO vertexBuffer, indicesBuffer, textureCoordsBuffer, normalsBuffer;
	
	private Material material = new Material(); // Default empty material
	
	private final int vertexCount;
	
	/**
	 * Construct a new vertex model with provided array buffer values
	 * 
	 * @param vertices the model vertices
	 * @param indices the indices of the vertices
	 * @param textureCoords the coordinates in which textures should be rendered
	 * @param normals the model face normals
	 */
	public VertexModel(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
		this.vertexCount = indices.length;
		
		// Construct new VAO and VBO objects
		this.vao = new VAO();
		this.vao.bind();

		FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
		vertexBuffer.put(vertices).flip();
		this.vertexBuffer = new VBO(GL_ARRAY_BUFFER, vertexBuffer);
		this.vao.createAttribPointer(0, 3);
		
		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();
		this.indicesBuffer = new VBO(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);
		
		FloatBuffer textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
		textureCoordsBuffer.put(textureCoords).flip();
		this.textureCoordsBuffer = new VBO(GL_ARRAY_BUFFER, textureCoordsBuffer);
		this.vao.createAttribPointer(1, 2);
		
		FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
		normalsBuffer.put(normals).flip();
		this.normalsBuffer = new VBO(GL_ARRAY_BUFFER, normalsBuffer);
		this.vao.createAttribPointer(2, 3);
		
		// Unbind buffers & clear buffer data
		this.normalsBuffer.unbind();
		this.vao.unbind();
		
		MemoryUtil.memFree(vertexBuffer);
		MemoryUtil.memFree(indicesBuffer);
		MemoryUtil.memFree(textureCoordsBuffer);
		MemoryUtil.memFree(normalsBuffer);
	}
	
	/**
	 * Get the VAO that handles this vertex model
	 * 
	 * @return the model's VAO
	 */
	public VAO getVAO() {
		return vao;
	}
	
	/**
	 * Set the material that should be rendered on this vertex model
	 * 
	 * @param material the material to be set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	/**
	 * Get this model's material
	 * 
	 * @return the model material
	 */
	public Material getMaterial() {
		return material;
	}
	
	/**
	 * Check whether the model has a texture or not
	 * 
	 * @return true if the model has a texture, false otherwise
	 */
	public boolean hasTexture() {
		return material.isTextured();
	}
	
	/**
	 * Render this vertex model to the framebuffer
	 */
	public void render() {
		if (material.isTextured()) {
			glActiveTexture(GL_TEXTURE0);
			this.material.getTexture().bind();
		}
		
		this.vao.bind();
		this.vao.enableAllAttribArrays();
		
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
		
		this.vao.disableAllAttribArrays();
		this.vao.unbind();
	}
	
	/**
	 * Cleanup any outstanding data
	 */
	public void cleanup() {
		this.vertexBuffer.delete();
		this.indicesBuffer.delete();
		this.textureCoordsBuffer.delete();
		this.normalsBuffer.delete();
		
		this.vao.delete();
		
		if (material.isTextured()) {
			material.getTexture().delete();
		}
	}
	
}
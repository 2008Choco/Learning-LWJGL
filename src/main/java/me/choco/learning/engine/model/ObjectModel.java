package me.choco.learning.engine.model;

import org.joml.Vector3f;

/**
 * Represents a model in the world
 * 
 * @author Parker Hawke - 2008Choco
 */
public class ObjectModel {
	
	private final VertexModel vertexModel;
	
	private Vector3f position, rotation;
	private float scale;
	
	/**
	 * Construct a new object model with an underlying vertex model
	 * 
	 * @param vertexModel the vertex model
	 */
	public ObjectModel(VertexModel vertexModel) {
		this.vertexModel = vertexModel;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = 1;
	}
	
	/**
	 * Get the underlying vertex model
	 * 
	 * @return the vertex model
	 */
	public VertexModel getVertexModel() {
		return vertexModel;
	}
	
	/**
	 * Set the position of this object model in the world
	 * 
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 * @param z the new z coordinate
	 */
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	/**
	 * Get the position of the model in the world
	 * 
	 * @return the model position
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Set the rotation of this object model in the world
	 * 
	 * @param x the new x angle
	 * @param y the new y angle
	 * @param z the new z angle
	 */
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
	
	/**
	 * Get the rotation of the model in the world
	 * 
	 * @return the model rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}
	
	/**
	 * Set the rendering scale of this model
	 * 
	 * @param scale the new rendering scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Get the rendering scale of this model
	 * 
	 * @return the rendering scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Cleanup any data for this model
	 */
	public void cleanup() {
		this.vertexModel.cleanup();
	}
	
}
package me.choco.learning.engine.camera;

import org.joml.Vector3f;

/**
 * Represents an in-world free roaming camera
 * 
 * @author Parker Hawke - 2008Choco
 */
public class Camera {
	
	private final Vector3f position, rotation;
	
	/**
	 * Construct a new camera at a given position and rotation
	 * 
	 * @param position the initial position of the camera
	 * @param rotation the initial rotation of the camera
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	/**
	 * Construct a new camera at an initial position and rotation of 0
	 */
	public Camera() {
		this(new Vector3f(), new Vector3f());
	}
	
	/**
	 * Set the position of the camera
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
	 * Move the camera according to x, y and z offsets
	 * 
	 * @param offsetX the x offset to move
	 * @param offsetY the y offset to move
	 * @param offsetZ the z offset to move
	 */
	public void move(float offsetX, float offsetY, float offsetZ) {
		if (offsetZ != 0) {
			this.position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1 * offsetZ;
			this.position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if (offsetX != 0) {
			this.position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1 * offsetX;
			this.position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		
		this.position.y += offsetY;
	}
	
	/**
	 * Get the current camera position
	 * 
	 * @return the camera position
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Set the rotation of the camera
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
	 * Rotate the camera according to x, y and z angle offsets
	 * 
	 * @param offsetX the x offset to rotate
	 * @param offsetY the y offset to rotate
	 * @param offsetZ the z offset to rotate
	 */
	public void rotate(float offsetX, float offsetY, float offsetZ) {
		this.rotation.x += offsetX;
		this.rotation.y += offsetY;
		this.rotation.z += offsetZ;
	}
	
	/**
	 * Get the current camera rotation
	 * 
	 * @return the camera rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}
	
}
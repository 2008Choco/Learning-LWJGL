package me.choco.game.entities;

import org.joml.Vector3f;

import me.choco.game.models.TexturedModel;

public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private float rotationX, rotationY, rotationZ, scale;
	public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
	}
	
	public void increasePosition(float x, float y, float z){
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}
	
	public void increaseRotation(float x, float y, float z){
		this.rotationX += x;
		this.rotationY += y;
		this.rotationZ += z;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getRotationX() {
		return rotationX;
	}
	
	public float getRotationY() {
		return rotationY;
	}
	
	public float getRotationZ() {
		return rotationZ;
	}
	
	public float getScale() {
		return scale;
	}
}
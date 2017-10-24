package me.choco.learning.engine.light;

import org.joml.Vector3f;

public class PointLight {
	
	private Vector3f position, colour;
	
	public PointLight(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
}
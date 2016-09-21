package me.choco.game.entities.generic;

import org.joml.Vector3f;

public class Light {
	
	private Vector3f position, colour;
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, float r, float g, float b) {
		this.position = position;
		this.colour = new Vector3f(r / 255, g / 255, g / 255);
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
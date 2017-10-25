package me.choco.learning.engine.light;

import org.joml.Vector3f;

/**
 * Represents a stationary point of light capable of emitting diffused light
 */
public class PointLight {
	
	private Vector3f position, colour;
	
	/**
	 * Construct a new point light at a given position with a colour
	 * 
	 * @param position the position of the light
	 * @param colour the light colour (components from 0.0 to 1.0)
	 */
	public PointLight(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	/**
	 * Set the new position of this point light
	 * 
	 * @param position the new position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/**
	 * Get the position of this point light
	 * 
	 * @return the point light position
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Set the colour of this point light. Component values should range
	 * from 0.0 to 1.0, where 0.0 is black and 1.0 is white
	 * 
	 * @param colour the new colour to set
	 */
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	/**
	 * Get the colour of this point light
	 * 
	 * @return the light's colour
	 */
	public Vector3f getColour() {
		return colour;
	}
	
}
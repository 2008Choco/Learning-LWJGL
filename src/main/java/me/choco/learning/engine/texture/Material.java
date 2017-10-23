package me.choco.learning.engine.texture;

import org.joml.Vector4f;

public class Material {
	
	private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	
	private Texture texture;
	private Vector4f ambientColour, diffuseColour, specularColour;
	
	private float reflectance;
	
	public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
		this.ambientColour = ambientColour;
		this.diffuseColour = diffuseColour;
		this.specularColour = specularColour;
		this.texture = texture;
		this.reflectance = reflectance;
	}
	
	public Material(Texture texture, float reflectance) {
		this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
	}
	
	public Material(Vector4f colour, float reflectance) {
		this(colour, colour, colour, null, reflectance);
	}
	
	public Material(Texture texture) {
		this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 0);
	}
	
	public Material() {
		this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, null, 0);
	}
	
	public void setAmbientColour(Vector4f ambientColour) {
		this.ambientColour = ambientColour;
	}
	
	public Vector4f getAmbientColour() {
		return ambientColour;
	}
	
	public void setDiffuseColour(Vector4f diffuseColour) {
		this.diffuseColour = diffuseColour;
	}
	
	public Vector4f getDiffuseColour() {
		return diffuseColour;
	}
	
	public void setSpecularColour(Vector4f specularColour) {
		this.specularColour = specularColour;
	}
	
	public Vector4f getSpecularColour() {
		return specularColour;
	}
	
	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
	
	public float getReflectance() {
		return reflectance;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean isTextured() {
		return this.texture != null;
	}
	
}
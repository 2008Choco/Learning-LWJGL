package me.choco.game.models.textures;

public class ModelTexture {
	
	private float shineDamper = 0, reflectivity = 0;
	
	private int textureID;
	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}
}
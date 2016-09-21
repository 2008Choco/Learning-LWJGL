package me.choco.game.models;

import me.choco.game.models.textures.ModelTexture;

public class TexturedModel {
	
	private final RawModel model;
	private final ModelTexture texture;
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public RawModel getModel() {
		return model;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
}
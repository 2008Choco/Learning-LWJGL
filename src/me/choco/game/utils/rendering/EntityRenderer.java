package me.choco.game.utils.rendering;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.choco.game.entities.Entity;
import me.choco.game.models.RawModel;
import me.choco.game.models.TexturedModel;
import me.choco.game.models.textures.ModelTexture;
import me.choco.game.shaders.StaticShader;
import me.choco.game.utils.Maths;

public class EntityRenderer {
	
	private MasterRenderer masterRenderer;
	private StaticShader shader;
	
	public EntityRenderer(MasterRenderer masterRenderer, StaticShader shader, Matrix4f projectionMatrix) {
		this.masterRenderer = masterRenderer;
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities){
		for (TexturedModel model : entities.keySet()){
			this.prepareTexturedModel(model);
			
			List<Entity> toRender = entities.get(model);
			for (Entity entity : toRender){
				this.prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			this.unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0); // Positions
		GL20.glEnableVertexAttribArray(1); // Texture Coordinates
		GL20.glEnableVertexAttribArray(2); // Normals

		if (!this.masterRenderer.isWireframeMode()){
			ModelTexture texture = model.getTexture();
			shader.loadShineValues(texture.getShineDamper(), texture.getReflectivity());
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		}
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
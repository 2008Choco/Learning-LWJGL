package me.choco.game.utils.rendering;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.choco.game.models.RawModel;
import me.choco.game.models.textures.ModelTexture;
import me.choco.game.shaders.TerrainShader;
import me.choco.game.utils.Maths;
import me.choco.game.world.Terrain;

public class TerrainRenderer {
	
	private MasterRenderer masterRenderer;
	private TerrainShader shader;
	
	public TerrainRenderer(MasterRenderer masterRenderer, TerrainShader shader, Matrix4f projectionMatrix) {
		this.masterRenderer = masterRenderer;
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Terrain> terrains){
		for (Terrain terrain : terrains){
			this.prepareTerrain(terrain);
			
			this.loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			this.unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain){
		RawModel rawModel = terrain.getModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0); // Positions
		GL20.glEnableVertexAttribArray(1); // Texture Coordinates
		GL20.glEnableVertexAttribArray(2); // Normals
		
		if (!this.masterRenderer.isWireframeMode()){
			ModelTexture texture = terrain.getTexture();
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
	
	private void loadModelMatrix(Terrain terrain){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
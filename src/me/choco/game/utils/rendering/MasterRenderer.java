package me.choco.game.utils.rendering;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import me.choco.game.Game;
import me.choco.game.entities.Camera;
import me.choco.game.entities.Entity;
import me.choco.game.entities.generic.Light;
import me.choco.game.models.TexturedModel;
import me.choco.game.shaders.StaticShader;
import me.choco.game.shaders.TerrainShader;
import me.choco.game.world.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000f;

	private boolean wireframeMode = false;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader staticShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	public MasterRenderer() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		this.createProjectionMatrix();
		
		this.entityRenderer = new EntityRenderer(this, staticShader, projectionMatrix);
		this.terrainRenderer = new TerrainRenderer(this, terrainShader, projectionMatrix);
	}
	
	public void render(Light light, Camera camera){
		this.prepare();
		
		staticShader.start();
		staticShader.loadLight(light);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
	}
	
	public void processEntity(Entity entity){
		TexturedModel model = entity.getModel();
		List<Entity> entities = this.entities.get(model);
		
		if (entities == null) entities = new ArrayList<>();
		entities.add(entity);
		
		this.entities.put(model, entities);
	}
	
	public void processTerrain(Terrain terrain){
		this.terrains.add(terrain);
	}
	
	public void cleanShader(){
		this.staticShader.clearShaderData();
		this.terrainShader.clearShaderData();
	}
	
	private void createProjectionMatrix(){
		float aspectRatio = (float) Game.WIDTH / Game.HEIGHT;
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(xScale);
		projectionMatrix.m11(yScale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
		projectionMatrix.m33(0);
	}
	
	public void setWireframeMode(boolean wireframeMode) {
		this.wireframeMode = wireframeMode;
		
		if (wireframeMode)
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		else GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, wireframeMode ? GL11.GL_LINE : GL11.GL_FILL);
	}
	
	public boolean isWireframeMode() {
		return wireframeMode;
	}
}
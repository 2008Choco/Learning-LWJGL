package me.choco.learning;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import me.choco.learning.engine.Window;
import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.light.PointLight;
import me.choco.learning.engine.model.ObjectModel;
import me.choco.learning.engine.model.VertexModel;
import me.choco.learning.engine.rendering.Renderer;
import me.choco.learning.engine.rendering.TransformationMatrices;
import me.choco.learning.engine.shading.ShaderProgram;
import me.choco.learning.engine.shading.ShaderType;

/**
 * Handles the rendering processes of the LWJGL program
 * 
 * @author Parker Hawke - 2008Choco
 */
public class LearningRenderer implements Renderer {
	
	private static final float FOV = (float) Math.toRadians(60);
	private static final float Z_NEAR = 0.01f, Z_FAR = 1000.0f;
	
	private Window window;
	private Camera camera;
	private ShaderProgram shaderProgram;
	private TransformationMatrices transformation = new TransformationMatrices();
	
	private final PointLight light = new PointLight(new Vector3f(0, -2, -3), new Vector3f(1, 1, 1));
	
	public LearningRenderer(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
		this.shaderProgram = new ShaderProgram();
		
		this.init(); // TODO: Have this called internally, not in the constructor
	}
	
	@Override
	public void init() {
		this.shaderProgram.loadShader(ShaderType.FRAGMENT, "/shaders/fragment.fs");
		this.shaderProgram.loadShader(ShaderType.VERTEX, "/shaders/vertex.vs");
		this.shaderProgram.link();
		
		// Vertex shader uniforms
		this.shaderProgram.createUniformVariable("projectionMatrix");
		this.shaderProgram.createUniformVariable("modelViewMatrix");
		this.shaderProgram.createUniformVariable("transformationMatrix");
		this.shaderProgram.createUniformVariable("lightPosition");
		
		// Fragment shader uniforms
		this.shaderProgram.createUniformVariable("textureSampler");
		this.shaderProgram.createUniformVariable("lightColour");
	}
	
	@Override
	public void render(ObjectModel... models) {
		this.shaderProgram.bind();
		
		this.shaderProgram.setUniformValue("projectionMatrix", transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR));
		this.shaderProgram.setUniformValue("lightPosition", light.getPosition());
		this.shaderProgram.setUniformValue("textureSampler", 0);
		this.shaderProgram.setUniformValue("lightColour", light.getColour());
		
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		
		// Render models
		for (ObjectModel model : models) {
			VertexModel vertexModel = model.getVertexModel();
			Matrix4f transformationMatrix = transformation.getTransformationMatrix(model);
			
			this.shaderProgram.setUniformValue("modelViewMatrix", viewMatrix.mul(transformationMatrix));
			this.shaderProgram.setUniformValue("transformationMatrix", transformationMatrix);
			vertexModel.render();
		}
		
		this.shaderProgram.unbind();
	}
	
	@Override
	public void cleanup() {
		if (shaderProgram != null) {
			this.shaderProgram.cleanup();
		}
	}
	
}
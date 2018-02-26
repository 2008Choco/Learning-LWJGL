package me.choco.learning;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.nio.FloatBuffer;
import java.util.List;

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
	
	private final PointLight light = new PointLight(new Vector3f(0, -2, -3), new Vector3f(1, 1, 1));
	
	public LearningRenderer(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
		this.shaderProgram = new ShaderProgram();
	}
	
	@Override
	public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
		this.shaderProgram.loadShader(ShaderType.FRAGMENT, "/shaders/fragment.fs");
		this.shaderProgram.loadShader(ShaderType.VERTEX, "/shaders/vertex.vs");
		this.shaderProgram.link();
		
		// Vertex shader uniforms
		this.shaderProgram.createUniformVariable("projectionMatrix");
		this.shaderProgram.createUniformVariable("transformationMatrix");
		this.shaderProgram.createUniformVariable("viewMatrix");
		this.shaderProgram.createUniformVariable("lightPosition");
		
		// Fragment shader uniforms
		this.shaderProgram.createUniformVariable("textureSampler");
		this.shaderProgram.createUniformVariable("lightColour");
	}
	
	@Override
	public void render(List<ObjectModel> models) {
		this.shaderProgram.bind();
		
		this.shaderProgram.setUniformValue("projectionMatrix", TransformationMatrices.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR));
		this.shaderProgram.setUniformValue("lightPosition", light.getPosition());
		this.shaderProgram.setUniformValue("textureSampler", 0);
		this.shaderProgram.setUniformValue("lightColour", light.getColour());
		
		Matrix4f viewMatrix = TransformationMatrices.getViewMatrix(camera);
		
		// Render models
		for (ObjectModel model : models) {
			VertexModel vertexModel = model.getVertexModel();
			Matrix4f transformationMatrix = TransformationMatrices.getTransformationMatrix(model);
			
			this.shaderProgram.setUniformValue("transformationMatrix", transformationMatrix);
			this.shaderProgram.setUniformValue("viewMatrix", viewMatrix);
			vertexModel.render();
		}
		
		this.shaderProgram.unbind();
	}
	
	@Override
	public void render(FloatBuffer buffer, int bufferSize) {
		glActiveTexture(GL_TEXTURE0);
		glDrawElements(GL_TRIANGLES, bufferSize, GL_UNSIGNED_INT, 0);
	}
	
	@Override
	public void cleanup() {
		if (shaderProgram != null) {
			this.shaderProgram.cleanup();
		}
	}
	
}
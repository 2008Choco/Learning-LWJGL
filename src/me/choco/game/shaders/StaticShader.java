package me.choco.game.shaders;

import org.joml.Matrix4f;

import me.choco.game.entities.Camera;
import me.choco.game.entities.generic.Light;
import me.choco.game.utils.Maths;

public class StaticShader extends AbstractShader {
	
	private static final String VERTEX_SHADER = "./shaders/StaticVertexShader.txt";
	private static final String FRAGMENT_SHADER = "./shaders/StaticFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	
	public StaticShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		this.bindAttribute(0, "position");
		this.bindAttribute(1, "textureCoords");
		this.bindAttribute(2, "normal");
	}
	
	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = this.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = this.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = this.getUniformLocation("viewMatrix");
		this.location_lightPosition = this.getUniformLocation("lightPosition");
		this.location_lightColour = this.getUniformLocation("lightColour");
		this.location_shineDamper = this.getUniformLocation("shineDamper");
		this.location_reflectivity = this.getUniformLocation("reflectivity");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		this.loadUniformValue(this.location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		this.loadUniformValue(this.location_projectionMatrix, matrix);
	}
	
	public void loadShineValues(float damper, float reflectivity){
		this.loadUniformValue(location_shineDamper, damper);
		this.loadUniformValue(location_reflectivity, reflectivity);
	}
	
	public void loadViewMatrix(Camera camera){
		this.loadUniformValue(this.location_viewMatrix, Maths.createViewMatrix(camera));
	}
	
	public void loadLight(Light light){
		this.loadUniformValue(this.location_lightPosition, light.getPosition());
		this.loadUniformValue(this.location_lightColour, light.getColour());
	}
}
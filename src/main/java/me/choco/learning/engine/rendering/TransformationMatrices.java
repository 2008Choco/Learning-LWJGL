package me.choco.learning.engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.model.ObjectModel;

/**
 * A class that holds information on various types of matrices including
 * projection, view and model view matrices. Applies respective transformation
 * based on the provided values in order to constantly update them when
 * required
 * 
 * @author Parker Hawke - 2008Choco
 */
public class TransformationMatrices {
	
	private final Matrix4f projectionMatrix = new Matrix4f(), viewMatrix = new Matrix4f(), modelViewMatrix = new Matrix4f();
	
	/**
	 * Modify and get the projection matrix based on given values
	 * 
	 * @param fov the FOV of the projection matrix
	 * @param width the width of the framebuffer
	 * @param height the height of the framebuffer
	 * @param zNear the nearest view distance on the projection matrix
	 * @param zFar the further view distance on the projection matrix
	 * 
	 * @return the resulting projection matrix
	 */
	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		this.projectionMatrix.identity()
			.perspective(fov, width / height, zNear, zFar);
		return projectionMatrix;
	}
	
	/**
	 * Get the model view matrix for a given object model which reflects
	 * its position, rotation and scale in the world
	 * 
	 * @param model the model to reflect changes for
	 * @param viewMatrix the view matrix to multiply with
	 * 
	 * @return the resulting model view matrix
	 */
	public Matrix4f getModelViewMatrix(ObjectModel model, Matrix4f viewMatrix) {
		Matrix4f transformationMatrix = getTransformationMatrix(model);
		Matrix4f viewCurrent = new Matrix4f(viewMatrix);
		return viewCurrent.mul(transformationMatrix);
	}
	
	public Matrix4f getTransformationMatrix(ObjectModel model) {
		Vector3f rotation = model.getRotation();
		
		return this.modelViewMatrix.identity()
			.translate(model.getPosition())
			.rotateX((float) Math.toRadians(-rotation.x))
			.rotateY((float) Math.toRadians(-rotation.y))
			.rotateZ((float) Math.toRadians(-rotation.z))
			.scale(model.getScale());
	}
	
	/**
	 * Get the view matrix for the given camera
	 * 
	 * @param camera the camera perspective
	 * @return the resulting view matrix
	 */
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f position = camera.getPosition(), rotation = camera.getRotation();
		
		this.viewMatrix.identity()
			.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
			.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
			.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
}
package me.choco.learning.engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import me.choco.learning.engine.camera.Camera;
import me.choco.learning.engine.model.ObjectModel;

/**
 * A utility class intended to simplify the calculation of various core OpenGL
 * matrices including projection, transformation and view matrices without having
 * to remember linear algebra concepts
 * 
 * @author Parker Hawke - 2008Choco
 */
public final class TransformationMatrices {
	
	private static final Matrix4f MATRIX_PROJECTION = new Matrix4f(), MATRIX_VIEW = new Matrix4f(), MATRIX_MODEL_VIEW = new Matrix4f();

	private TransformationMatrices() {}
	
	/**
	 * Modify and get the projection matrix based on given values. A projection matrix
	 * projects 2D space onto a 3D environment with various environmental parameters
	 * including field of view (FOV), screen width and height as well as the nearest
	 * and farthest z position to render
	 * 
	 * @param fov the FOV of the projection matrix
	 * @param width the width of the frame buffer
	 * @param height the height of the frame buffer
	 * @param zNear the nearest view distance on the projection matrix
	 * @param zFar the further view distance on the projection matrix
	 * 
	 * @return the resulting projection matrix
	 */
	public static Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		return MATRIX_PROJECTION.identity().perspective(fov, width / height, zNear, zFar);
	}
	
	/**
	 * Get the transformation matrix of a given model. A transformation matrix reflects
	 * an objects position, rotation and scale relative to its original values. i.e. if a
	 * model is positioned at (1, 1, 1), is rotated 90 degrees and has a scale of 0.5, this
	 * matrix will reflect those values from it's original position of (0, 0, 0), rotation
	 * of 0 and scale of 1
	 * 
	 * @param model the model whose transformation matrix to calculate
	 * @return the calculated transformation matrix
	 */
	public static Matrix4f getTransformationMatrix(ObjectModel model) {
		Vector3f rotation = model.getRotation();
		
		return MATRIX_MODEL_VIEW.identity()
			.translate(model.getPosition())
			.rotateX((float) Math.toRadians(-rotation.x))
			.rotateY((float) Math.toRadians(-rotation.y))
			.rotateZ((float) Math.toRadians(-rotation.z))
			.scale(model.getScale());
	}
	
	/**
	 * Get the view matrix for the given camera. A view matrix reflects the camera's
	 * perspective in the world including its position and rotation
	 * 
	 * @param camera the camera perspective whose view matrix to calculate
	 * @return the calculated view matrix
	 */
	public static Matrix4f getViewMatrix(Camera camera) {
		Vector3f position = camera.getPosition(), rotation = camera.getRotation();
		
		return MATRIX_VIEW.identity()
			.rotateX((float) Math.toRadians(rotation.x))
			.rotateY((float) Math.toRadians(rotation.y))
			.translate(-position.x, -position.y, -position.z);
	}
	
}
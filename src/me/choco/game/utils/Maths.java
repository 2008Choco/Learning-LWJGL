package me.choco.game.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import me.choco.game.entities.Camera;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rotationX, float rotationY, float rotationZ, float scale){
		Matrix4f tMatrix = new Matrix4f();
		tMatrix.identity();
		tMatrix.translate(translation);
		tMatrix.rotateXYZ((float) Math.toRadians(rotationX), (float) Math.toRadians(rotationY), (float) Math.toRadians(rotationZ));
		tMatrix.scale(scale);
		
		return tMatrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f vMatrix = new Matrix4f();
		vMatrix.identity();
		vMatrix.rotateX((float) Math.toRadians(camera.getPitch()));
		vMatrix.rotateY((float) Math.toRadians(camera.getYaw()));
		vMatrix.translate(new Vector3f(camera.getPosition()).mul(-1));
		
		return vMatrix;
	}
}
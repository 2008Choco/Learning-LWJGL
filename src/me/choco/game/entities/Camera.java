package me.choco.game.entities;

import org.joml.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw, roll;
	private float speed = 0.25f;
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getSpeed() {
		return speed;
	}
}
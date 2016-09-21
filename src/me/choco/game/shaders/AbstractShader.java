package me.choco.game.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class AbstractShader {
	
	private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	private int program;
	private final int vertexShaderID, fragmentShaderID;
	public AbstractShader(String vertexShaderFile, String fragmentShaderFile) {
		this.program = GL20.glCreateProgram();
		this.vertexShaderID = this.loadShader(vertexShaderFile, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = this.loadShader(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);

		GL20.glAttachShader(this.program, this.vertexShaderID);
		GL20.glAttachShader(this.program, this.fragmentShaderID);
		this.bindAttributes();
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		
		this.getAllUniformLocations();
	}
	
	public void start(){
		GL20.glUseProgram(this.program);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void clearShaderData(){
		this.stop();
		
		GL20.glDetachShader(this.program, this.vertexShaderID);
		GL20.glDetachShader(this.program, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.program);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(this.program, attribute, variableName);
	}

	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String variableName){
		return GL20.glGetUniformLocation(this.program, variableName);
	}
	
	public void loadUniformValue(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	public void loadUniformValue(int location, Vector3f value){
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}
	
	public void loadUniformValue(int location, boolean value){
		GL20.glUniform1f(location, value ? 1 : 0);
	}
	
	public void loadUniformValue(int location, Matrix4f matrix){
		matrix.get(matrixBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	private int loadShader(String fileName, int type){
		StringBuilder shaderCode = new StringBuilder();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
			String line;
			while ((line = reader.readLine()) != null)
				shaderCode.append(line).append("\n");
		}catch(IOException e){ e.printStackTrace(); }
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID,  GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.err.println(GL20.glGetShaderInfoLog(shaderID));
			System.exit(1);
		}
		
		return shaderID;
	}
}
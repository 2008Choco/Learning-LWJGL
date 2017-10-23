package me.choco.learning.engine.shading;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

/**
 * Represents a shader program containing various types of shaders. Handles
 * the linking, binding and unbinding of shader programs in the OpenGL
 * rendering pipeline
 * 
 * @author Parker Hawke - 2008Choco
 */
public class ShaderProgram {
	
	private final Map<String, Integer> uniformVariables = new HashMap<>();
	
	private final int programId;
	private int vertexShaderId, fragmentShaderId;
	
	/**
	 * Construct a new shader program and initialize it with a unique id
	 */
	public ShaderProgram() {
		this.programId = glCreateProgram();
		
		if (programId == 0) {
			throw new UnsupportedOperationException("Could not create a new shader program");
		}
	}
	
	/**
	 * Load a shader file based on its type
	 * 
	 * @param type the type of shader to load
	 * @param fileName the name of the file containing the shader source code
	 * 
	 * @return the resulting shader id
	 */
	public int loadShader(ShaderType type, String fileName) {
		if (type == null)
			throw new IllegalArgumentException("Must specify a non-null shader type");
		if (fileName == null)
			throw new IllegalArgumentException("Cannot load shader from a non-existent file");
		
		int shaderId = -1;
		
		// Create ID for the respective shader type
		if (type == ShaderType.FRAGMENT) {
			this.fragmentShaderId = glCreateShader(type.getId());
			shaderId = fragmentShaderId;
		}
		else if (type == ShaderType.VERTEX) {
			this.vertexShaderId = glCreateShader(type.getId());
			shaderId = vertexShaderId;
		}
		
		if (shaderId == 0) {
			throw new UnsupportedOperationException("Could not create a new shader. Shader type: " + type);
		}
		
		// Load the shader source from file
		StringBuilder shaderSource = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getResourceAsStream(fileName)))) {
			reader.lines().forEach(l -> shaderSource.append(l).append("\n"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set and compile the source code for the given shader
		glShaderSource(shaderId, shaderSource);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new UnsupportedOperationException("Could not compile shader " + type + ". Stacktrace: " + glGetShaderInfoLog(shaderId));
		}
		
		// Attach the shader id to the program
		glAttachShader(programId, shaderId);
		return shaderId;
	}
	
	/**
	 * Link the program to the graphics processing unit. Any shader ids will
	 * be lost and its source code will be forgotten after a link is successful.
	 * Should be invoked after all shaders have been loaded using
	 * {@link #loadShader(ShaderType, File)}
	 */
	public void link() {
		// Attempt to link the program to the GPU
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new UnsupportedOperationException("Could not link the shader program. Stacktrace: " + glGetProgramInfoLog(programId));
		}
		
		// If any shaders are attached, detach them from the shader program
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}
		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		
		// Validate the program before assuming all is well
		// This can be completely removed once everything is production ready
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Error while validating shader program. Stacktrace: " + glGetProgramInfoLog(programId));
		}
	}
	
	/**
	 * Create a uniform variable found in the shader program. Note that a variable
	 * must be created before setting its value
	 * 
	 * @param name the name of the uniform variable
	 */
	public void createUniformVariable(String name) {
		int location = glGetUniformLocation(programId, name);
		if (location < 0)
			throw new NoSuchFieldError("Could not find the uniform variable with name \"" + name + "\"");
		
		this.uniformVariables.put(name, location);
	}
	
	/**
	 * Set the value of a uniform variable of type "mat4"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			glUniformMatrix4fv(uniformVariables.get(name), false, value.get(buffer));
		}
	}
	
	/**
	 * Set the value of a uniform variable of type "vec4"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, Vector4f value) {
		glUniform4f(uniformVariables.get(name), value.x, value.y, value.z, value.w);
	}
	
	/**
	 * Set the value of a uniform variable of type "vec3"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, Vector3f value) {
		glUniform3f(uniformVariables.get(name), value.x, value.y, value.z);
	}
	
	/**
	 * Set the value of a uniform variable of type "bool"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, boolean value) {
		this.setUniformValue(name, value ? 1 : 0);
	}
	
	/**
	 * Set the value of a uniform variable of type "int"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, int value) {
		glUniform1i(uniformVariables.get(name), value);
	}
	
	/**
	 * Set the value of a uniform variable of type "float"
	 * 
	 * @param name the name of the uniform variable
	 * @param value the value to set
	 */
	public void setUniformValue(String name, float value) {
		glUniform1f(uniformVariables.get(name), value);
	}
	
	/**
	 * Bind and use this shader program
	 */
	public void bind() {
		glUseProgram(programId);
	}
	
	/**
	 * Unbind this shader program
	 */
	public void unbind() {
		glUseProgram(0);
	}
	
	/**
	 * Clean up any outstanding information in this shader program, and delete
	 * the program from the GPU
	 */
	public void cleanup() {
		this.unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}
	
}
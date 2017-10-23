package me.choco.learning.engine.rendering;

import me.choco.learning.engine.model.ObjectModel;

public interface Renderer {
	
	/* OpenGL Rendering Pipeline
	 * 
	 * 1. Vertex and Index Lists
	 * 2. Vertex Processing       <- Vertex Shader
	 * 3. Geometry Processing     <- Geometry Shader
	 * 4. Rasterization
	 * 5. Fragment Processing     <- Fragment Shader
	 * 6. Framebuffer
	 * 
	 * Vertex and Index Lists:   A list of vertices and indexes in the form of Vertex Buffers
	 * Vertex Processing:        Calculates the projected position of each vertex onto the screen
	 * Geometry Processing:      Connects the vertices created by the vertex shader to create triangles
	 * Rasterization:            Takes the triangles from geometry processing and clips & transforms them into fragments
	 * Fragment Processing:      Fragments from rasterization are used to generate pixels and written to the framebuffer
	 * Framebuffer:              Final result of the pipeline that holds all values of each pixel drawn to the screen
	 */
	
	/**
	 * Initialize the renderer with any necessary information. This method
	 * should be invoked once upon the initialization of this class
	 */
	public void init();
	
	/**
	 * Render to the LWJGL framebuffer
	 * 
	 * @param models all models to render
	 */
	public void render(ObjectModel... models);
	
	/**
	 * Clean up any outstanding information in the renderer including the
	 * active shader program
	 */
	public void cleanup();
	
}
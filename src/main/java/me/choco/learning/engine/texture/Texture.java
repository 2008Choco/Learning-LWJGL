package me.choco.learning.engine.texture;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import me.choco.learning.engine.model.VertexModel;

/**
 * Represents a renderable texture on a {@link VertexModel}
 * 
 * @author Parker Hawke - 2008Choco
 */
public class Texture {
	
	private int id = -1;
	private int width, height;
	
	/**
	 * Construct a new texture and read its png data from the provided file name
	 * 
	 * @param fileName the name of the texture file (including its path)
	 */
	public Texture(String fileName) {
		try {
			PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
			buffer.flip();
			
			this.id = glGenTextures();
			this.width = decoder.getWidth();
			this.height = decoder.getHeight();
			
			glBindTexture(GL_TEXTURE_2D, id);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glGenerateMipmap(GL_TEXTURE_2D);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct a new texture based off of raw buffer data
	 * 
	 * @param buffer the buffer data to create a texture from
	 * @param width the expected width of the texture
	 * @param height the expected height of the texture
	 */
	public Texture(ByteBuffer buffer, int width, int height) {
		this.id = glGenTextures();
		this.width = width;
		this.height = height;
		
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	/**
	 * Get the texture ID for this texture
	 * 
	 * @return the texture ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the width of this texture
	 * 
	 * @return this texture width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get the height of this texture
	 * 
	 * @return the texture height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Bind this texture as the active texture
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * Unbind this texture
	 */
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Delete this texture from memory
	 */
	public void delete() {
		this.unbind();
		glDeleteTextures(id);
	}
	
}
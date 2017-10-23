package me.choco.learning.engine.texture;

import static org.lwjgl.opengl.GL11.*;
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
			glBindTexture(GL_TEXTURE_2D, id);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glGenerateMipmap(GL_TEXTURE_2D);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
package me.choco.game.models.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture {
	
	private int textureID;
	private final int width, height;
	public Texture(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("./resources/" + fileName + ".png"));
		} catch (IOException e) { e.printStackTrace(); }
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		int[] rawPixels = new int[width * height];
		image.getRGB(0, 0, width, height, rawPixels, 0, width);
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(rawPixels.length * 4);
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				int pixel = rawPixels[(x * width) + y];
				
				pixels.put((byte) ((pixel >> 16) & 0xFF));
				pixels.put((byte) ((pixel >> 8) & 0xFF));
				pixels.put((byte) (pixel & 0xFF));
				pixels.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		pixels.flip();
		this.textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureID);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
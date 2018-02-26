package me.choco.learning.engine.rendering.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;

import me.choco.learning.engine.rendering.Renderer;
import me.choco.learning.engine.texture.Texture;

public class FontTexture {
	
	private final Map<Character, Glyph> glyphs = new HashMap<>();
	
	private final Texture texture;
	private int fontHeight;
	
	private final Font font;
	private final boolean antiAliasing;
	
	public FontTexture(Font font, boolean antiAliasing) {
		this.font = font;
		this.antiAliasing = antiAliasing;
		this.texture = createTextureAtlas();
	}

	/**
	 * Gets the width of the specified text
	 *
	 * @param text the text whose width to calculate
	 * @return the width of the text
	 */
	public int getWidth(CharSequence text) {
		int width = 0, lineWidth = 0;
		
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			
			if (character == '\n') { // New line
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				continue;
			}
			
			// Ignore carriage returns
			if (character == '\r') continue;
			
			Glyph glyph = glyphs.get(character);
			if (glyph == null) continue;
			
			lineWidth += glyph.getWidth();
		}
		
		width = Math.max(width, lineWidth);
		return width;
	}

	/**
	 * Gets the height of the specified text
	 *
	 * @param text the text whose height to calculate
	 * @return the height of the text
	 */
	public int getHeight(CharSequence text) {
		int height = 0, lineHeight = 0;

		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);

			if (character == '\n') { // New line
				height += lineHeight;
				lineHeight = 0;
				continue;
			}

			// Ignore carriage returns
			if (character == '\r') continue;

			Glyph glyph = glyphs.get(character);
			if (glyph == null) continue;
			
			lineHeight = Math.max(lineHeight, glyph.getHeight());
		}

		height += lineHeight;
		return height;
	}

	public void drawText(Renderer renderer, CharSequence text, float x, float y, Color color) {
		int textHeight = getHeight(text);
		float drawX = x, drawY = y;
		
		if (textHeight > fontHeight) {
			drawY += textHeight - fontHeight;
		}
		
		this.texture.bind();
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			
			if (character == '\n') { // Draw to new line
				drawY -= fontHeight;
				drawX = x;
				continue;
			}
			
			// Ignore carriage return
			if (character == '\r') continue;
			
			Glyph glyph = glyphs.get(character);
			
			FloatBuffer buffer = toFloatBuffer(drawX, drawY, glyph.getX(), glyph.getY(), glyph.getWidth(), glyph.getHeight(), color);
			renderer.render(buffer, buffer.capacity());
			MemoryUtil.memFree(buffer);
			
			drawX += glyph.getWidth();
		}
	}
	
	private FloatBuffer toFloatBuffer(float drawX, float drawY, float x, float y, float width, float height, Color color) {
		float x2 = drawX + x, y2 = drawY + y;
		float s1 = (x / texture.getWidth()), s2 = (y / texture.getHeight());
		float t1 = ((x + width) / texture.getWidth()), t2 = ((y + height) / texture.getHeight());
		
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();
		
		FloatBuffer buffer = MemoryUtil.memAllocFloat(6);
		
		buffer.put(drawX).put(drawY).put(r).put(g).put(b).put(a).put(s1).put(t1);
		buffer.put(drawX).put(y2).put(r).put(g).put(b).put(a).put(s1).put(t2);
		buffer.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);

		buffer.put(drawX).put(drawY).put(r).put(g).put(b).put(a).put(s1).put(t1);
		buffer.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);
		buffer.put(x2).put(drawY).put(r).put(g).put(b).put(a).put(s2).put(t1);
		
		return buffer;
	}
	
	private Texture createTextureAtlas() {
		int textureWidth = 0;
		
		// Calculate texture width and height
		// NOTE: Char codes 0 - 31 are control codes and can be ignored
		for (int i = 32; i < 256; i++) {
			if (i == 127) continue; // Delete character. Can be ignored
			
			char character = (char) i;
			BufferedImage charImage = createCharImage(character);
			if (charImage == null) continue; // Ignore if invalid
			
			textureWidth += charImage.getWidth();
			this.fontHeight = Math.max(fontHeight, charImage.getHeight());
		}
		
		// Generate texture atlas image
		BufferedImage image = new BufferedImage(textureWidth, fontHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		int offset = 0;
		for (int i = 0; i < 256; i++) {
			if (i == 127) continue;
			
			char character = (char) i;
			BufferedImage charImage = createCharImage(character);
			if (charImage == null) continue; // Ignore if invalid
			
			int charWidth = charImage.getWidth(), charHeight = charImage.getHeight();
			Glyph glyph = new Glyph(character, charWidth, charHeight, offset, image.getHeight() - charHeight);
			graphics.drawImage(charImage, offset, 0, null);
			offset += charWidth;
			
			this.glyphs.put(character, glyph);
		}
		
		// Flip the image horizontally. Orients origin to bottom left
        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);
        
        int width = image.getWidth(), height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
		
        // Populate byte buffer with texture data
        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int y = 0; y < height; y++) {
        	for (int x = 0; x < width; x++) {
        		int pixel = pixels[(y * width) + x];
        		buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
        		buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
        		buffer.put((byte) (pixel & 0xFF)); // Blue
        		buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
        	}
        }
        
        // Generate final texture
        buffer.flip();
        Texture textureAtlas = new Texture(buffer, width, height);
        MemoryUtil.memFree(buffer);
        
		return textureAtlas;
	}
	
	private BufferedImage createCharImage(char character) {
		// Get temp FontMetrics object
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		if (antiAliasing) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics();
		graphics.dispose();
		
		int charWidth = metrics.charWidth(character);
		int charHeight = metrics.getHeight();
		
		// Character not represented in provided font
		if (charWidth == 0) return null;
		
		// Generate character image
		image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
		
		if (antiAliasing) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		graphics.setFont(font);
		graphics.setPaint(Color.WHITE);
		graphics.drawString(String.valueOf(character), 0, metrics.getAscent());
		graphics.dispose();
		
		return image;
	}
	
}
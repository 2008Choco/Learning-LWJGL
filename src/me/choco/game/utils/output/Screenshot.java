package me.choco.game.utils.output;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import me.choco.game.Game;

public class Screenshot {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	
	private BufferedImage screenshot;
	public Screenshot() {
		int width = Game.WIDTH, height = Game.HEIGHT;
		
		GL11.glReadBuffer(GL11.GL_FRONT);
		ByteBuffer data = BufferUtils.createByteBuffer(width * height * 4);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		
		this.renderScreenshot(data, width, height);
	}
	
	public BufferedImage getScreenshot(){
		return screenshot;
	}
	
	public void saveToDesktop(){
		String fileName = DATE_FORMAT.format(new Date()).toString() + ".png";
		
		File file = new File(System.getProperty("user.home") + "/Desktop/" + fileName);
		try {
			ImageIO.write(screenshot, "PNG", file);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private void renderScreenshot(ByteBuffer data, int width, int height){
		this.screenshot = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				int index = (x + (width * y)) * 4;
				
				int r = data.get(index++) & 0xFF;
				int g = data.get(index++) & 0xFF;
				int b = data.get(index) & 0xFF;
				
				this.screenshot.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
	}
}
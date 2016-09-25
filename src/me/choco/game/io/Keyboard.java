package me.choco.game.io;

public class Keyboard {
	
	public static final boolean[] keys = new boolean[1024];
	
	public static boolean isKeyPressed(int key){
		return keys[key];
	}
	
	public static boolean areKeysPressed(int... keys){
		boolean allPressed = true;
		for (int key : keys)
			if (isKeyReleased(key)) allPressed = false;
		return allPressed;
	}
	
	public static boolean isKeyReleased(int key){
		return !keys[key];
	}
	
	public static boolean areKeysReleased(int... keys){
		boolean allReleased = true;
		for (int key : keys)
			if (isKeyPressed(key)) allReleased = false;
		return allReleased;
	}
}
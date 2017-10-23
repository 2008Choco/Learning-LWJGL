package me.choco.learning;

import me.choco.learning.engine.GameLogicController;

public class Main {
	
	public static void main(String[] args) {
		GameLogicController game = new LearningLWJGLGame();
		game.run();
	}
	
}
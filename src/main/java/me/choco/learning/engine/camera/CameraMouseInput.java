package me.choco.learning.engine.camera;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;
import org.joml.Vector2f;

import me.choco.learning.engine.Window;

/**
 * Handles the input from the mouse buttons and cursor position
 * 
 * @author Parker Hawke - 2008Choco
 */
public class CameraMouseInput {

    private final Vector2d previousPos, currentPos;
    private final Vector2f displayVector;

    private boolean leftButtonPressed = false, rightButtonPressed = false;
    private boolean inWindow = false;

    public CameraMouseInput() {
    	this.previousPos = new Vector2d(-1, -1);
    	this.currentPos = new Vector2d();
    	this.displayVector = new Vector2f();
    }

    /**
     * Initialize the mouse input for the given window
     * 
     * @param window the window instance
     */
    public void init(Window window) {
        glfwSetCursorPosCallback(window.getId(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        
        glfwSetCursorEnterCallback(window.getId(), (windowHandle, entered) -> inWindow = entered);
        
        glfwSetMouseButtonCallback(window.getId(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
            rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
        });
    }

    /**
     * Get the difference between the last and current cursor position in
     * form of a vector 2f
     * 
     * @return the position difference between frames
     */
    public Vector2f getDisplayVec() {
        return displayVector;
    }

    /**
     * Calculate the current and previous mouse position as well as the
     * display vector for the given window
     * 
     * @param window the window instance to calculate for
     */
    public void update(Window window) {
        this.displayVector.x = 0;
        this.displayVector.y = 0;
        
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltaX = currentPos.x - previousPos.x;
            double deltaY = currentPos.y - previousPos.y;
            
            if (deltaX != 0) displayVector.y = (float) deltaX;
            if (deltaY != 0) displayVector.x = (float) deltaY;
        }
        
        this.previousPos.x = currentPos.x;
        this.previousPos.y = currentPos.y;
    }

    /**
     * Check whether the left mouse button is pressed or not
     * 
     * @return true if pressed. false otherwise
     */
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    /**
     * Check whether the right mouse button is pressed or not
     * 
     * @return true if pressed. false otherwise
     */
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
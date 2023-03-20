package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float CAMERA_SPEED = -0.05f;

    private final Vector3f position = new Vector3f(100, 0.5f, 0);
    private float pitch;
    private float yaw = 180;
    private float roll;

    public Camera() {
    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) position.z -= CAMERA_SPEED;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) position.z += CAMERA_SPEED;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) position.x += CAMERA_SPEED;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) position.x -= CAMERA_SPEED;
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) yaw -= CAMERA_SPEED * 10;
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) yaw += CAMERA_SPEED * 10;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}

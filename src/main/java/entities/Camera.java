package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float CAMERA_SPEED = -0.05f;

    private final Vector3f position = new Vector3f(100, 2, 0);
    private float pitch;
    private float yaw = 180;
    private float roll;

    public Camera() {
    }

    public void move() {

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

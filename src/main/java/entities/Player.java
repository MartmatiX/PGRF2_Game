package entities;

import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

import static entities.Projectile.projectileModel;
import static game.utils.GlobalVariables.*;

public class Player extends Entity {

    private static float RUN_SPEED = 50;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -30;
    private static final float JUMP_POWER = 10;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    private int health = 100;

    public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain) {
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

        if (super.getPosition().y < terrainHeight + 0.5f) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight + 0.5f;
        }
    }

    private void jump() {
        if (!isInAir) {
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) currentSpeed = RUN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)) currentSpeed = -RUN_SPEED;
        else currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) currentTurnSpeed = -TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A)) currentTurnSpeed = TURN_SPEED;
        else currentTurnSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) jump();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static float getRunSpeed() {
        return RUN_SPEED;
    }

    public static void setRunSpeed(float runSpeed) {
        RUN_SPEED = runSpeed;
    }

    public void shootProjectile() {
        Vector3f projectilePosition = new Vector3f(super.getPosition());
        Vector3f lookDirection = getLookDirection();
        projectilePosition.x += lookDirection.x * 2;
        projectilePosition.y = player.getPosition().getY() + 2;
        projectilePosition.z += lookDirection.z * 2;
        projectileModel.getTexture().setUseFakeLighting(true);
        Projectile projectile = new Projectile(projectileModel, projectilePosition, lookDirection);
        projectiles.add(projectile);
    }

    public Vector3f getLookDirection() {
        float pitch = super.getRotX();
        float yaw = super.getRotY();
        float x = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        float y = (float) Math.sin(Math.toRadians(pitch));
        float z = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        return new Vector3f(x, y, z);
    }
}

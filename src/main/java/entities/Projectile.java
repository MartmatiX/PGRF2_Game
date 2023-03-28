package entities;

import game.utils.GlobalVariables;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Projectile extends Entity {

    private final Vector3f direction;
    private final float speed;

    public Projectile(TextureModel model, Vector3f position, Vector3f direction, float scale, float speed) {
        super(model, position, GlobalVariables.player.getRotX(), GlobalVariables.player.getRotY(), GlobalVariables.player.getRotZ(), scale);
        this.direction = new Vector3f(direction);
        this.speed = speed;
    }

    public void update() {
        float distance = speed * DisplayManager.getFrameTimeSeconds();
        float dx = distance * direction.x;
        float dy = distance * direction.y;
        float dz = distance * direction.z;
        super.increasePosition(dx, dy, dz);
    }

    // TODO: 28.03.2023 Add method for projectile destruction
    // TODO: 28.03.2023 Add method to prevent player from shooting indefinitely

}

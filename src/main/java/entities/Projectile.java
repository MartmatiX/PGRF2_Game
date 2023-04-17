package entities;

import game.utils.GlobalVariables;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import static game.utils.GlobalVariables.loader;

public class Projectile extends Entity {

    public static RawModel rawProjectileModel = OBJLoader.loadObjModel("Bullet", loader);
    public static ModelTexture projectileModelTexture = new ModelTexture(loader.loadTexture("texture"));
    public static TextureModel projectileModel = new TextureModel(rawProjectileModel, projectileModelTexture);

    private final Vector3f direction;

    private float timeToLive = 5;

    public Projectile(TextureModel model, Vector3f position, Vector3f direction) {
        super(model, position, GlobalVariables.player.getRotX(), GlobalVariables.player.getRotY(), GlobalVariables.player.getRotZ(), 8);
        this.direction = new Vector3f(direction);
    }

    public void update() {
        float speed = 150;
        float distance = speed * DisplayManager.getFrameTimeSeconds();
        float dx = distance * direction.x;
        float dy = distance * direction.y;
        float dz = distance * direction.z;
        super.increasePosition(dx, dy, dz);
        timeToLive -= 1 * DisplayManager.getFrameTimeSeconds();
    }

    public void destroy(int index) {
        if (this.timeToLive < 0) {
            GlobalVariables.projectiles.remove(index);
        }
    }

}

package game.utils;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import static game.utils.GlobalVariables.*;

public class EnemyCreator {

    private static final RawModel pineModel = OBJLoader.loadObjModel("pine", loader);
    private static final ModelTexture pineTexture = new ModelTexture(loader.loadTexture("pine"));
    private static final TextureModel pine = new TextureModel(pineModel, pineTexture);

    public static void createEnemies() {
        for (int i = 0; i < 10; i++) {
            Entity enemy = new Entity(pine, new Vector3f(random.nextInt(MAX_RENDER_DISTANCE), terrains.get(0).getHeightOfTerrain(100, 800), random.nextInt(MAX_RENDER_DISTANCE)), 0, 0, 0, 5f);
            enemies.add(enemy);
        }
    }

    public static void createSingleEnemy() {
        Entity enemy = new Entity(pine, new Vector3f(random.nextInt(MAX_RENDER_DISTANCE), terrains.get(0).getHeightOfTerrain(100, 800), random.nextInt(MAX_RENDER_DISTANCE)), 0, 0, 0, 5f);
        enemies.add(enemy);
    }

}

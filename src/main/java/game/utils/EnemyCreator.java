package game.utils;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import static game.utils.GlobalVariables.*;

public class EnemyCreator {

    private static final RawModel enemyModel = OBJLoader.loadObjModel("enemy_janga", loader);
    private static final ModelTexture enemyTexture = new ModelTexture(loader.loadTexture("enemy_janga"));
    private static final TextureModel enemyTM = new TextureModel(enemyModel, enemyTexture);

    public static void createEnemies() {
        for (int i = 0; i < 10; i++) {
            Entity enemy = new Entity(enemyTM, new Vector3f(random.nextInt(MAX_RENDER_DISTANCE), terrains.get(0).getHeightOfTerrain(100, 800), random.nextInt(MAX_RENDER_DISTANCE)), 0, 0, 0, 5f);
            enemies.add(enemy);
        }
    }

    public static void createSingleEnemy() {
        Entity enemy = new Entity(enemyTM, new Vector3f(random.nextInt(MAX_RENDER_DISTANCE), terrains.get(0).getHeightOfTerrain(100, 800), random.nextInt(MAX_RENDER_DISTANCE)), 0, 0, 0, 5f);
        enemies.add(enemy);
    }

}

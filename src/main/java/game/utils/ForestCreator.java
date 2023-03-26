package game.utils;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import static game.utils.GlobalVariables.*;

public class ForestCreator {

    public static void createForest() {
        Terrain terrain = GlobalVariables.terrains.get(0);

        RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
        ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
        TextureModel treeT = new TextureModel(treeModel, treeTexture);
        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat(MAX_RENDER_DISTANCE);
            float z = random.nextFloat(MAX_RENDER_DISTANCE);
            float y = terrain.getHeightOfTerrain(x, z);
            if (y > -38) {
                Entity entity1 = new Entity(treeT, new Vector3f(x, y, z), 0, 0, 0, 5);
                entitiesToRender.add(entity1);
            }
        }

        RawModel pineModel = OBJLoader.loadObjModel("pine", loader);
        ModelTexture pineTexture = new ModelTexture(loader.loadTexture("pine"));
        TextureModel pine = new TextureModel(pineModel, pineTexture);
        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat(MAX_RENDER_DISTANCE);
            float z = random.nextFloat(MAX_RENDER_DISTANCE);
            float y = terrain.getHeightOfTerrain(x, z);
            if (y > -38) {
                Entity entity1 = new Entity(pine, new Vector3f(x, y, z), 0, 0, 0, 1);
                entitiesToRender.add(entity1);
            }
        }

        RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernTexture.setNumberOfRows(2);
        TextureModel finishedFern = new TextureModel(fernModel, fernTexture);
        finishedFern.getTexture().setHasTransparency(true);
        finishedFern.getTexture().setUseFakeLighting(true);
        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat(MAX_RENDER_DISTANCE);
            float z = random.nextFloat(MAX_RENDER_DISTANCE);
            float y = terrain.getHeightOfTerrain(x, z);
            if (y > -38) {
                Entity entity1 = new Entity(finishedFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.5f);
                entitiesToRender.add(entity1);
            }
        }
    }

}

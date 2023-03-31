package game.utils;

import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import static game.utils.GlobalVariables.*;

public class LightAndSunCreator {

    public static void createSun() {
        Light sun = new Light(new Vector3f(0, 2000, 0), sunlight);
        GlobalVariables.lights.add(sun);
    }

    public static void createLamps() {
        Terrain terrain = GlobalVariables.terrains.get(0);

        RawModel lampModel = OBJLoader.loadObjModel("lamp", loader);
        ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
        TextureModel lamp = new TextureModel(lampModel, lampTexture);

        for (int i = 0; i < 15; i++) {
            float x = random.nextFloat(MAX_RENDER_DISTANCE);
            float z = random.nextFloat(MAX_RENDER_DISTANCE);
            float y = terrain.getHeightOfTerrain(x, z);
            while (y < -37) {
                x = random.nextFloat(MAX_RENDER_DISTANCE);
                z = random.nextFloat(MAX_RENDER_DISTANCE);
                y = terrain.getHeightOfTerrain(x, z);
            }
            Light light = new Light(new Vector3f(x, y + 10, z), new Vector3f(1f, 0.8f, 0.8f), new Vector3f(1, 0.00001f, 0.004f));
            Entity lampEntity = new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1);
            entitiesToRender.add(lampEntity);
            lights.add(light);
        }

    }

}

package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();

        // stall model
        RawModel model = OBJLoader.loadObjModel("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TextureModel textureModel = new TextureModel(model, texture);

        // dragon model
        RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
        TextureModel dragonTexture = new TextureModel(dragonModel, new ModelTexture(loader.loadTexture("texture")));
        ModelTexture texture1 = dragonTexture.getTexture();
        texture1.setShineDamper(10);
        texture1.setReflectivity(1);

        Entity entity = new Entity(textureModel, new Vector3f(0, 0, 40), 0, 0, 0, 1);
        Entity dragon = new Entity(dragonTexture, new Vector3f(0, 1, 30), 0, 50, 0, 0.3f);

        List<Entity> entitiesToRender = new ArrayList<>(List.of(entity, dragon));

        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("texture")));
        List<Terrain> terrains = new ArrayList<>(List.of(terrain));

        MasterRenderer renderer = new MasterRenderer();

        // game logic etc...
        while (!Display.isCloseRequested()) { // Checks whether the display is closed by user
            camera.move();
            terrains.forEach(renderer::processTerrain);
            entitiesToRender.forEach(renderer::processEntity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

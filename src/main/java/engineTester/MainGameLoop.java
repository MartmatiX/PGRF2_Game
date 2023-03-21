package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        Random random = new Random();

        DisplayManager.createDisplay();

        Loader loader = new Loader();

        // Texture Pack
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        // stall model
        RawModel model = OBJLoader.loadObjModel("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TextureModel textureModel = new TextureModel(model, texture);
        Entity entity = new Entity(textureModel, new Vector3f(0, 0, 40), 0, 0, 0, 1);


        // dragon model
        RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
        TextureModel dragonTexture = new TextureModel(dragonModel, new ModelTexture(loader.loadTexture("texture")));
        ModelTexture texture1 = dragonTexture.getTexture();
        texture1.setShineDamper(10);
        texture1.setReflectivity(1);
        Entity dragon = new Entity(dragonTexture, new Vector3f(0, 1, 30), 0, 50, 0, 0.3f);

        List<Entity> entitiesToRender = new ArrayList<>(List.of(entity, dragon));

        Light light = new Light(new Vector3f(100, 50, 0), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        List<Terrain> terrains = new ArrayList<>(List.of(terrain));

        MasterRenderer renderer = new MasterRenderer();

        // tree
        RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
        ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
        TextureModel treeT = new TextureModel(treeModel, treeTexture);
        List<Entity> forest = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Entity entity1 = new Entity(treeT, new Vector3f(x, y, z), 0, 0, 0, 5);
            forest.add(entity1);
        }

        // fern
        RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernTexture.setNumberOfRows(2);
        TextureModel finishedFern = new TextureModel(fernModel, fernTexture);
        finishedFern.getTexture().setHasTransparency(true);
        finishedFern.getTexture().setUseFakeLighting(true);
        List<Entity> ferns = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Entity entity1 = new Entity(finishedFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.5f);
            ferns.add(entity1);
        }

        RawModel playerRawModel = OBJLoader.loadObjModel("person", loader);
        TextureModel playerTexture = new TextureModel(playerRawModel, new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(playerTexture, new Vector3f(100, 0, 0), 0, 0, 0, 0.3f);

        Camera camera = new Camera(player);

        // game logic etc...
        while (!Display.isCloseRequested()) { // Checks whether the display is closed by user
            player.move(terrain);
            camera.move();

            renderer.processEntity(player);

            terrains.forEach(renderer::processTerrain);
            entitiesToRender.forEach(renderer::processEntity);
            forest.forEach(renderer::processEntity);
            ferns.forEach(renderer::processEntity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

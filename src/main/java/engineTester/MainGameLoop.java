package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

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

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        List<Terrain> terrains = new ArrayList<>(List.of(terrain));

        List<Light> lights = new ArrayList<>();

        // lamps and light
        RawModel lampModel = OBJLoader.loadObjModel("lamp", loader);
        ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
        TextureModel lamp = new TextureModel(lampModel, lampTexture);
        Light sun = new Light(new Vector3f(0, 1000, -7000), new Vector3f(1, 1, 1));
        for (int i = 0; i < 15; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Light light = new Light(new Vector3f(x, y + 5, z), new Vector3f(1f, 0.5f, 0.5f), new Vector3f(1, 0.008f, 0.0004f));
            Entity lampE = new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1);
            entitiesToRender.add(lampE);
            lights.add(light);
        }
        lights.add(sun);

        MasterRenderer renderer = new MasterRenderer(loader);

        // tree
        RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
        ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
        TextureModel treeT = new TextureModel(treeModel, treeTexture);
        for (int i = 0; i < 1000; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Entity entity1 = new Entity(treeT, new Vector3f(x, y, z), 0, 0, 0, 5);
            entitiesToRender.add(entity1);
        }

        RawModel pineModel = OBJLoader.loadObjModel("pine", loader);
        ModelTexture pineTexture = new ModelTexture(loader.loadTexture("pine"));
        TextureModel pine = new TextureModel(pineModel, pineTexture);
        for (int i = 0; i < 1000; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Entity entity1 = new Entity(pine, new Vector3f(x, y, z), 0, 0, 0, 1);
            entitiesToRender.add(entity1);
        }

        // fern
        RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernTexture.setNumberOfRows(2);
        TextureModel finishedFern = new TextureModel(fernModel, fernTexture);
        finishedFern.getTexture().setHasTransparency(true);
        finishedFern.getTexture().setUseFakeLighting(true);
        for (int i = 0; i < 1000; i++) {
            float x = (random.nextFloat() * 800) + 5;
            float z = (random.nextFloat() * 800) + 5;
            float y = terrain.getHeightOfTerrain(x, z);
            Entity entity1 = new Entity(finishedFern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.5f);
            entitiesToRender.add(entity1);
        }

        // player
        RawModel playerRawModel = OBJLoader.loadObjModel("untitled", loader);
        TextureModel playerTexture = new TextureModel(playerRawModel, new ModelTexture(loader.loadTexture("player_neon")));
        Player player = new Player(playerTexture, new Vector3f(100, 0, 800), 0, 0, 0, 1f);

        entitiesToRender.add(player);

        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        // water
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile(75, 600, terrain.getHeightOfTerrain(75, 1000));
        waters.add(water);

        // game logic etc...
        while (!Display.isCloseRequested()) { // Checks whether the display is closed by user
            player.move(terrain);
            camera.move();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // reflection texture
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entitiesToRender, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
            camera.getPosition().y += distance;
            camera.invertPitch();

            // refraction texture
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(entitiesToRender, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight() + 1f));
            buffers.unbindCurrentFrameBuffer();

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            renderer.renderScene(entitiesToRender, terrains, lights, camera, new Vector4f(0, 1, 0, 15));

            waterRenderer.render(waters, camera, sun);

            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

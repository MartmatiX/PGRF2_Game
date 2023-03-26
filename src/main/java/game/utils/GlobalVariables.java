package game.utils;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobalVariables {

    public static final int MAX_RENDER_DISTANCE = 2048;

    public static final List<Entity> entitiesToRender = new ArrayList<>();
    public static final List<Terrain> terrains = new ArrayList<>();
    public static final List<Light> lights = new ArrayList<>();
    public static final List<WaterTile> waters = new ArrayList<>();
    public static final List<Entity> enemies = new ArrayList<>();

    public static final Loader loader = new Loader();

    public static final Random random = new Random();

    public static final MasterRenderer renderer = new MasterRenderer(loader);
    public static final WaterFrameBuffers buffers = new WaterFrameBuffers();
    public static final WaterShader waterShader = new WaterShader();
    public static final WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);

    public static final Player player = EntityCreator.createPlayer();

    public static final Camera camera = new Camera(player);

}

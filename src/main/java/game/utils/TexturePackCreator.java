package game.utils;

import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import static game.utils.GlobalVariables.loader;

public class TexturePackCreator {

    public static void generateTexture() {
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, bTexture, gTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        GlobalVariables.terrains.add(terrain);
    }

}

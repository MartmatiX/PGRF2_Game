package game.utils;

import water.WaterTile;

import static game.utils.GlobalVariables.*;

public class WaterCreator {

    public static void createWater() {
        WaterTile water = new WaterTile((float) 1024, (float) 1024, terrains.get(0).getHeightOfTerrain(0, 0) + 1f);
        waters.add(water);
    }

}

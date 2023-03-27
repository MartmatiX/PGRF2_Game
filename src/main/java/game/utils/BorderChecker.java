package game.utils;

import org.lwjgl.util.vector.Vector3f;

import static game.utils.GlobalVariables.MAX_RENDER_DISTANCE;
import static game.utils.GlobalVariables.player;

public class BorderChecker {

    public static void checkBoundaries() {
        if (player.getPosition().x > MAX_RENDER_DISTANCE - 2) player.setPosition(new Vector3f(MAX_RENDER_DISTANCE - 3, GlobalVariables.terrains.get(0).getHeightOfTerrain(MAX_RENDER_DISTANCE - 3, player.getPosition().z), player.getPosition().z));
        if (player.getPosition().x < 2) player.setPosition(new Vector3f(3, GlobalVariables.terrains.get(0).getHeightOfTerrain(3, player.getPosition().z), player.getPosition().z));
        if (player.getPosition().z > MAX_RENDER_DISTANCE - 2) player.setPosition(new Vector3f(player.getPosition().x, GlobalVariables.terrains.get(0).getHeightOfTerrain(player.getPosition().x, MAX_RENDER_DISTANCE - 3), MAX_RENDER_DISTANCE - 3));
        if (player.getPosition().z < 2) player.setPosition(new Vector3f(player.getPosition().x, GlobalVariables.terrains.get(0).getHeightOfTerrain(player.getPosition().x, 2), 3));
    }

}

package game.utils;

import org.lwjgl.util.vector.Vector4f;

import static game.utils.GlobalVariables.*;
import static game.utils.GlobalVariables.camera;

public class ReflectionRefractionBuffers {

    public static void reflect() {
        buffers.bindReflectionFrameBuffer();
        float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
        camera.getPosition().y -= distance;
        camera.invertPitch();
        renderer.renderScene(player, entitiesToRender, enemies, projectiles, terrains, lights, camera, new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 1f));
        camera.getPosition().y += distance;
        camera.invertPitch();
    }

    public static void refract () {
        buffers.bindRefractionFrameBuffer();
        renderer.renderScene(player, entitiesToRender, enemies, projectiles, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight() + 1f));
        buffers.unbindCurrentFrameBuffer();
    }

}

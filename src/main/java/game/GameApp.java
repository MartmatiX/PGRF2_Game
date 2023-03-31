package game;

import game.utils.*;
import heightmap.HeightMapGenerator;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import terrains.Terrain;

import static game.utils.GlobalVariables.*;

public class GameApp {

    public static void main(String[] args) throws LWJGLException {
        HeightMapGenerator.generateHeightMap();

        DisplayManager.createDisplay();

        TexturePackCreator.generateTexture();
        Terrain terrain = terrains.get(0);

        // testing entities
//        EntityCreator.createStall();
//        EntityCreator.createDragon();

        LightAndSunCreator.createSun();
        LightAndSunCreator.createLamps();

        ForestCreator.createForest();

        EntityCreator.createPlayer();

        WaterCreator.createWater();

        EnemyCreator.createEnemies();

        time = 0;

        SoundManager.init();
        SoundManager.playSurroundSound("src/main/resources/sounds/metin_surround.wav", 0.02f);

        EndgameScreen endgameScreen = new EndgameScreen();

        Runnable run = EndgameScreen::updateLabels;

        Thread t = new Thread(() -> {
            while (true) {
                run.run();
            }
        });

        t.start();

        // game logic etc...
        while (isGameRunning) {
            player.move(terrain);
            camera.move();

            BorderChecker.checkBoundaries();

            time += 1 * DisplayManager.getFrameTimeSeconds();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            ReflectionRefractionBuffers.reflect();
            ReflectionRefractionBuffers.refract();

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            renderer.renderScene(player, entitiesToRender, enemies, projectiles, terrains, lights, camera, new Vector4f(0, 1, 0, 15));

            GameLogic.callGameLogic();

            waterRenderer.render(waters, camera, lights.get(0));

            DisplayManager.updateDisplay();
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        AL.destroy();
        DisplayManager.closeDisplay();

    }

}

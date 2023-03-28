package game;

import collision.CollisionDetection;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.Projectile;
import game.utils.*;
import heightmap.HeightMapGenerator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import terrains.Terrain;

import static game.utils.GlobalVariables.*;

public class GameApp {

    public static void main(String[] args) throws LWJGLException {
        // Creation of heightMap and generating in game entities
        HeightMapGenerator.generateHeightMap();

        DisplayManager.createDisplay();

        TexturePackCreator.generateTexture();
        Terrain terrain = terrains.get(0);

        // testing entities
//        EntityCreator.createStall();
//        EntityCreator.createDragon();

        LightAndSunCreator.createSun();
        Light sun = lights.get(0);
        LightAndSunCreator.createLamps();

        ForestCreator.createForest();

        EntityCreator.createPlayer();

        WaterCreator.createWater();

        EnemyCreator.createEnemies();

        // gui testing
//        List<GuiTexture> guis = new ArrayList<>();
//        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
//        guis.add(gui);
//        GuiRenderer guiRenderer = new GuiRenderer(loader);


        boolean sunMoveWest = true;
        boolean isDamageable = true;
        int timeBeforeNextHit = 1000;

        time = 0;

        SoundManager.init();
        SoundManager.playSurroundSound("src/main/resources/sounds/metin_surround.wav", 0.2f);

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


            // random testing bullshitery

            // TODO: 24.03.2023 remake this to method or something
            for (Entity entity1 : enemies) {
                if (CollisionDetection.detectCollision(player, entity1)) {
                    if (isDamageable) {
                        SoundManager.playDamageSound("src/main/resources/sounds/dmg_sound.wav");
                        player.setHealth(player.getHealth() - 10);
                        isDamageable = false;
                        Player.setRunSpeed(Player.getRunSpeed() * 2);
                    }
                }
            }
            if (!isDamageable) {
                timeBeforeNextHit -= 100 * DisplayManager.getFrameTimeSeconds();
                if (timeBeforeNextHit <= 0) {
                    isDamageable = true;
                    Player.setRunSpeed(Player.getRunSpeed() / 2);
                    timeBeforeNextHit = 1000;
                }
            }

            // TODO: 24.03.2023 remake this to method or something
            // TODO: 23.03.2023 finish sun movement around the Y axis to simulate day / night better
            if (sunMoveWest) {
                sun.getPosition().z += 200 * DisplayManager.getFrameTimeSeconds();
                if (sun.getPosition().z >= 3000) {
                    EnemyCreator.createSingleEnemy();
                    sunMoveWest = false;
                }
            } else {
                sun.getPosition().z -= 200 * DisplayManager.getFrameTimeSeconds();
                if (sun.getPosition().z <= -3000) {
                    sunMoveWest = true;
                }
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
                System.out.println(time);
                isGameRunning = false;
            }

            // TODO: 24.03.2023 recreate this to method or something, also add better movement
            // enemy movement
            for (int i = 0; i < enemies.size(); i++) {
                Entity enemy = enemies.get(i);
                boolean canMove = true;

                for (int j = 1; j < enemies.size() - 1; j++) {
                    if (i == j) continue;

                    Entity otherEnemy = enemies.get(j);
                    if (CollisionDetection.detectCollision(enemy, otherEnemy)) {
                        canMove = false;
                        break;
                    }
                }

                if (canMove) {
                    float dx = player.getPosition().x - enemy.getPosition().x;
                    float dz = player.getPosition().z - enemy.getPosition().z;
                    float distance2 = (float) Math.sqrt(dx * dx + dz * dz);

                    dx /= distance2;
                    dz /= distance2;
                    enemy.increasePosition(dx * DisplayManager.getFrameTimeSeconds() * 50, 0, dz * DisplayManager.getFrameTimeSeconds() * 50);
                    enemy.moveToPosition(new Vector3f(enemy.getPosition().x, terrain.getHeightOfTerrain(enemy.getPosition().x, enemy.getPosition().z), enemy.getPosition().z));
                }
            }

            // projectile testing
            if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                player.shootProjectile();
            }

            for (Projectile projectile : projectiles){
                projectile.update();
                if (projectile.getPosition().z > 1000) {
                    projectile.setPosition(new Vector3f(0, 0, 0));
                    break;
                }
            }

            for (int i = 0; i < enemies.size(); i++) {
                Entity enemy = enemies.get(i);
                for (int j = 0; j < projectiles.size(); j++) {
                    if (j == i) continue;

                    Projectile projectile = projectiles.get(j);
                    if (CollisionDetection.detectCollision(enemy, projectile)) {
                        enemies.remove(i);
                        projectiles.remove(j);
                        break;
                    }
                }
            }

            if (player.getPosition().getY() < -38) {
                Player.setRunSpeed(100);
            } else {
                Player.setRunSpeed(400);
            }

            waterRenderer.render(waters, camera, sun);

//            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }

        buffers.cleanUp();
        waterShader.cleanUp();
//        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        AL.destroy();
        DisplayManager.closeDisplay();

        EndgameScreen endgameScreen = new EndgameScreen();
    }

}

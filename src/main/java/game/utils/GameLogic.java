package game.utils;

import collision.CollisionDetection;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.Projectile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import static game.utils.GlobalVariables.*;

public class GameLogic {

    public static void callGameLogic() {
        playerCollisionWithEnemy();
        moveEnemies();
        checkShooting();
        updateProjectiles();
        destroyOldProjectiles();
        checkProjectileHit();
        moveSun();
        enemySpawner();
        slowPlayerInWater();
        checkGameExit();
    }

    private static void playerCollisionWithEnemy() {
        for (Entity enemy : GlobalVariables.enemies) {
            if (CollisionDetection.detectCollision(GlobalVariables.player, enemy) && isDamageable) {
                SoundManager.playSound("src/main/resources/sounds/dmg_sound.wav", 1f, false);
                player.setHealth(player.getHealth() - 10);
                isDamageable = false;
                Player.setRunSpeed(Player.getRunSpeed() * 2);
                if (player.getHealth() == 0) isGameRunning = false;
            }
        }
        if (!isDamageable) {
            invulnerabilityTimer -= 1 * DisplayManager.getFrameTimeSeconds();
            if (invulnerabilityTimer <= 0) {
                isDamageable = true;
                Player.setRunSpeed(Player.getRunSpeed() / 2);
                invulnerabilityTimer = 100;
            }
        }
    }

    private static void moveEnemies() {
        float safeDistance = 5f;
        float repulsionStrength = 50f;

        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            for (int j = 0; j < enemies.size(); j++) {
                if (i == j) continue;

                Entity otherEnemy = enemies.get(j);
                if (CollisionDetection.detectCollision(enemy, otherEnemy)) {
                    float dx = enemy.getPosition().x - otherEnemy.getPosition().x;
                    float dz = enemy.getPosition().z - otherEnemy.getPosition().z;
                    float distance = (float) Math.sqrt(dx * dx + dz * dz);
                    float repulsion = repulsionStrength / (distance * distance);
                    Vector3f repulsionForce = new Vector3f(dx / distance * repulsion, 0, dz / distance * repulsion);
                    enemy.increasePosition(repulsionForce.x, 0, repulsionForce.z);
                }
            }

            Vector3f playerPos = player.getPosition();
            Vector3f enemyPos = enemy.getPosition();
            float dx = playerPos.x - enemyPos.x;
            float dz = playerPos.z - enemyPos.z;
            float distance = (float) Math.sqrt(dx * dx + dz * dz);

            float dirX = dx / distance;
            float dirZ = dz / distance;
            float rotY = (float) Math.toDegrees(Math.atan2(dirX, dirZ));
            enemy.setRotY(rotY);

            if (distance < safeDistance) {
                enemy.increasePosition(-dirX * DisplayManager.getFrameTimeSeconds() * 25, 0, -dirZ * DisplayManager.getFrameTimeSeconds() * 25);
            } else {
                enemy.increasePosition(dirX * DisplayManager.getFrameTimeSeconds() * 25, 0, dirZ * DisplayManager.getFrameTimeSeconds() * 25);
            }

            enemy.moveToPosition(new Vector3f(enemy.getPosition().x, terrains.get(0).getHeightOfTerrain(enemy.getPosition().x, enemy.getPosition().z), enemy.getPosition().z));
        }
    }




    private static void checkShooting() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && canShoot) {
            player.shootProjectile();
            SoundManager.playSound("src/main/resources/sounds/shootSound.wav", 1f, false);
            canShoot = false;
        }
        if (!canShoot) {
            timeBeforeNextShot -= 1 * DisplayManager.getFrameTimeSeconds();
            if (timeBeforeNextShot <= 0) {
                canShoot = true;
                timeBeforeNextShot = 100;
            }
        }
    }

    private static void checkProjectileHit() {
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            for (int j = 0; j < projectiles.size(); j++) {
                if (j == i) continue;

                Projectile projectile = projectiles.get(j);
                if (CollisionDetection.detectCollision(enemy, projectile)) {
                    enemies.remove(i);
                    projectiles.remove(j);
                    playerKills++;
                    SoundManager.playSound("src/main/resources/sounds/bulletHit.wav", 1f, false);
                    break;
                }
            }
        }
    }

    private static void updateProjectiles() {
        for (entities.Projectile projectile : projectiles) {
            projectile.update();
        }
    }

    private static void destroyOldProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).destroy(i);
        }
    }

    private static void moveSun() {
        Light sun = lights.get(0);
        if (sunMoveWest) {
            sun.getPosition().z += 100 * DisplayManager.getFrameTimeSeconds();
            if (sun.getPosition().z >= 3000) {
                sunMoveWest = false;
            }
        } else {
            sun.getPosition().z -= 100 * DisplayManager.getFrameTimeSeconds();
            if (sun.getPosition().z <= -3000) {
                sunMoveWest = true;
            }
        }
    }

    private static void slowPlayerInWater() {
        if (player.getPosition().getY() < -38) {
            Player.setRunSpeed(25);
        } else {
            Player.setRunSpeed(50);
        }
    }

    private static void checkGameExit() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
            isGameRunning = false;
        }
    }

    private static void enemySpawner() {
        enemySpawnTimer -= 1 * DisplayManager.getFrameTimeSeconds();
        if (enemySpawnTimer <= 0) {
            for (int i = 0; i < enemiesToSpawn; i++) {
                EnemyCreator.createSingleEnemy();
            }
            enemiesToSpawn++;
            enemySpawnTimer = 15;
        }
    }

}

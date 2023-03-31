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

    // TODO: 28.03.2023 move all game logic functions here once they are finished
    public static void callGameLogic() {
        playerCollisionWithEnemy();
        moveEnemies();
        checkShooting();
        updateProjectiles();
        destroyOldProjectiles();
        checkProjectileHit();
        moveSun();
        slowPlayerInWater();
        checkGameExit();
    }

    private static void playerCollisionWithEnemy() {
        for (Entity enemy : GlobalVariables.enemies) {
            if (CollisionDetection.detectCollision(GlobalVariables.player, enemy) && isDamageable) {
                SoundManager.playDamageSound("src/main/resources/sounds/dmg_sound.wav");
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
                invulnerabilityTimer = 1000;
            }
        }
    }

    private static void moveEnemies() {
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
                enemy.moveToPosition(new Vector3f(enemy.getPosition().x, terrains.get(0).getHeightOfTerrain(enemy.getPosition().x, enemy.getPosition().z), enemy.getPosition().z));
            }
        }
    }

    private static void checkShooting() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && canShoot) {
            player.shootProjectile();
            SoundManager.playShootingSound("src/main/resources/shootSound.wav");
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
                    SoundManager.playHitSound("src/main/resources/bulletHit.wav");
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

    // TODO: 23.03.2023 finish sun movement around the Y axis to simulate day / night better
    private static void moveSun() {
        Light sun = lights.get(0);
        if (sunMoveWest) {
            sun.getPosition().z += 200 * DisplayManager.getFrameTimeSeconds();
            if (sun.getPosition().z >= 3000) {
                for (int i = 0; i < 3; i++) {
                    EnemyCreator.createSingleEnemy();
                }
                sunMoveWest = false;
            }
        } else {
            sun.getPosition().z -= 200 * DisplayManager.getFrameTimeSeconds();
            if (sun.getPosition().z <= -3000) {
                sunMoveWest = true;
            }
        }
    }

    private static void slowPlayerInWater() {
        if (player.getPosition().getY() < -38) {
            Player.setRunSpeed(100);
        } else {
            Player.setRunSpeed(400);
        }
    }

    private static void checkGameExit() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
            isGameRunning = false;
        }
    }

}

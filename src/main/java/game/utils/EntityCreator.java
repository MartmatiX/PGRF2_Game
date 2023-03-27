package game.utils;

import entities.Entity;
import entities.Player;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import static game.utils.GlobalVariables.entitiesToRender;
import static game.utils.GlobalVariables.loader;

public class EntityCreator {

    public static void createStall() {
        RawModel model = OBJLoader.loadObjModel("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TextureModel textureModel = new TextureModel(model, texture);
        Entity entity = new Entity(textureModel, new Vector3f(0, 0, 40), 0, 0, 0, 1);
        GlobalVariables.entitiesToRender.add(entity);
    }

    public static void createDragon() {
        RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
        TextureModel dragonTexture = new TextureModel(dragonModel, new ModelTexture(loader.loadTexture("texture")));
        ModelTexture texture1 = dragonTexture.getTexture();
        texture1.setShineDamper(10);
        texture1.setReflectivity(1);
        Entity dragon = new Entity(dragonTexture, new Vector3f(0, 1, 30), 0, 50, 0, 0.3f);
        GlobalVariables.entitiesToRender.add(dragon);
    }

    public static Player createPlayer() {
        RawModel playerRawModel = OBJLoader.loadObjModel("untitled", loader);
        TextureModel playerTexture = new TextureModel(playerRawModel, new ModelTexture(loader.loadTexture("player_neon")));
        Player player = new Player(playerTexture, new Vector3f(100, 0, 800), 0, 0, 0, 1f);

        //entitiesToRender.add(player);
        return player;
    }

}

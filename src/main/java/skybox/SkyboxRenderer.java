package skybox;

import entities.Camera;
import game.utils.GlobalVariables;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;

import static game.utils.GlobalVariables.sunlight;

public class SkyboxRenderer {

    private static final float SIZE = 1000;

    private static final float[] VERTICES = {-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

            SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

            -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE};

    private static final String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
    private static final String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};

    private final RawModel cube;
    private final int texture;
    private final int nightTexture;
    private final SkyboxShader shader;

    private float time = 0;

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
        cube = loader.loadToVao(VERTICES, 3);
        texture = loader.loadCube(TEXTURE_FILES);
        nightTexture = loader.loadCube(NIGHT_TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, float r, float g, float b) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(r, g, b);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures() {
        time += DisplayManager.getFrameTimeSeconds() * 1000;
        time %= 120000;
        int texture1 = 0;
        int texture2 = 0;
        float blendFactor = 0.0f;
        float sunlightIntensity = 0.0f;

        if (time < 30000) {
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = time / 30000.0f;
            sunlightIntensity = time / 30000.0f;
        } else if (time < 60000) {
            texture1 = nightTexture;
            texture2 = texture;
            blendFactor = (time - 30000.0f) / 30000.0f;
            sunlightIntensity = 1.0f;
        } else if (time < 90000) {
            texture1 = texture;
            texture2 = texture;
            blendFactor = (time - 60000.0f) / 30000.0f;
            sunlightIntensity = 1.0f;
        } else if (time < 120000) {
            texture1 = texture;
            texture2 = nightTexture;
            blendFactor = (time - 90000.0f) / 30000.0f;
            sunlightIntensity = (120000.0f - time) / 30000.0f;
        }

        sunlight.x = sunlightIntensity;
        sunlight.y = sunlightIntensity;
        sunlight.z = sunlightIntensity;

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

}

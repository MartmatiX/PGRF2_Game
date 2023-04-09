package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    private static final String RESOURCES_DIR = "src/main/resources/";

    public static RawModel loadObjModel(String fileName, Loader loader) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(RESOURCES_DIR + fileName + ".obj"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading OBJ file: " + fileName, e);
        }

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                switch (tokens[0]) {
                    case "v" ->  vertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "vt" -> textures.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                    case "vn" -> normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "f" -> processFace(tokens, indices, textures, normals);
                    default -> {
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading OBJ file: " + fileName, e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[textures.size() * 2];
        float[] normalsArray = new float[normals.size() * 3];
        int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            verticesArray[i * 3] = vertex.x;
            verticesArray[i * 3 + 1] = vertex.y;
            verticesArray[i * 3 + 2] = vertex.z;

            if (!textures.isEmpty()) {
                Vector2f texture = textures.get(i);
                texturesArray[i * 2] = texture.x;
                texturesArray[i * 2 + 1] = 1 - texture.y;
            }

            if (!normals.isEmpty()) {
                Vector3f normal = normals.get(i);
                normalsArray[i * 3] = normal.x;
                normalsArray[i * 3 + 1] = normal.y;
                normalsArray[i * 3 + 2] = normal.z;
            }
        }

        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processFace(String[] tokens, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals) {
        for (int i = 1; i <= 3; i++) {
            String[] vertexData = tokens[i].split("/");
            int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
            indices.add(vertexIndex);

            if (vertexData.length > 1 && !vertexData[1].isEmpty()) {
                int textureIndex = Integer.parseInt(vertexData[1]) - 1;
                textures.add(textures.get(textureIndex));
            }

            if (vertexData.length > 2 && !vertexData[2].isEmpty()) {
                int normalIndex = Integer.parseInt(vertexData[2]) - 1;
                normals.add(normals.get(normalIndex));
            }
        }
    }
}
package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static RawModel loadObjModel(String fileName, Loader loader) {
        FileReader fR;
        try {
            fR = new FileReader("src/main/resources/" + fileName + ".obj");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(fR);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray;
        float[] normalsArray = null;
        float[] texturesArray = null;
        int[] indicesArray;

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("f ")) {
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        if (texturesArray != null && normalsArray != null) {
            RawModel model = loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
            return model;
        } else {
            RawModel model = loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
            return model;
        }
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
        int index = Integer.parseInt(vertexData[0]) - 1;
        int textureIndex = Integer.parseInt(vertexData[1]) - 1;
        int normalIndex = Integer.parseInt(vertexData[2]) - 1;

        if (index >= 0 && index < texturesArray.length) {
            indices.add(index);
        }

        if (textures.size() > 0 && textureIndex >= 0 && textureIndex < textures.size()) {
            Vector2f texture = textures.get(textureIndex);
            if (index * 2 >= 0 && index * 2 + 1 < texturesArray.length) {
                texturesArray[index * 2] = texture.x;
                texturesArray[index * 2 + 1] = 1 - texture.y;
            }
        }

        if (normals.size() > 0 && normalIndex >= 0 && normalIndex < normals.size()) {
            Vector3f normal = normals.get(normalIndex);
            if (index * 3 >= 0 && index * 3 + 2 < normalsArray.length) {
                normalsArray[index * 3] = normal.x;
                normalsArray[index * 3 + 1] = normal.y;
                normalsArray[index * 3 + 2] = normal.z;
            }
        }
    }

}

package me.choco.learning.engine.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import me.choco.learning.engine.texture.Material;

/**
 * A utility class to load models from an OBJ file
 * 
 * @author Parker Hawke - 2008Choco
 */
public class ModelLoader {
	
	/**
	 * Load a model from an OBJ file with the given name and provide it with
	 * a texture
	 * 
	 * @param fileName the name of the OBJ file (including the path)
	 * @param material the material to set for the model, or null if none
	 * 
	 * @return the loaded model
	 */
	public static VertexModel loadOBJModel(String fileName, Material material) {
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textureCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		float[] verticesArray = null, textureCoordsArray = null, normalsArray = null;
		int[] indicesArray = null;
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(ModelLoader.class.getResourceAsStream(fileName)))) {
			String line;
			while((line = reader.readLine()) != null){
				String[] currentLine = line.split("\\s+");
				
				switch (currentLine[0]) {
				case "v":
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
					break;
				case "vt":
					Vector2f textureCoord = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textureCoords.add(textureCoord);
					break;
				case "vn":
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
					break;
				case "f":
					if (textureCoordsArray == null) textureCoordsArray = new float[vertices.size() * 2];
					if (normalsArray == null) normalsArray = new float[vertices.size() * 3];
					
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");
					
					processVertex(vertex1, indices, textureCoords, normals, textureCoordsArray, normalsArray);
					processVertex(vertex2, indices, textureCoords, normals, textureCoordsArray, normalsArray);
					processVertex(vertex3, indices, textureCoords, normals, textureCoordsArray, normalsArray);
					break;
				default: break;
				}
			}
		} catch(IOException e) { e.printStackTrace(); }
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++)
			indicesArray[i] = indices.get(i);
		
		VertexModel model = new VertexModel(verticesArray, indicesArray, textureCoordsArray, normalsArray);
		if (material != null) {
			model.setMaterial(material);
		}
		
		return model;
	}
	
	/**
	 * Load a model from an OBJ file with the given name with no texture
	 * 
	 * @param fileName the name of the OBJ file (including the path)
	 * @return the loaded model
	 */
	public static VertexModel loadOBJModel(String fileName) {
		return loadOBJModel(fileName, null);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		if (vertexData[1].equalsIgnoreCase("")) {
			textureArray[currentVertexPointer * 2] = -1;
			textureArray[currentVertexPointer * 2 + 1] = -1;
		}
		else {
			Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
			textureArray[currentVertexPointer * 2] = currentTexture.x;
			textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
		}
		
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNormal.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
	}
}
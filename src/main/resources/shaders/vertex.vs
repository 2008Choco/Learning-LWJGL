#version 330

in vec3 position;
in vec2 inTextureCoords;
in vec3 inVertexNormal;

out vec2 textureCoords;
out vec3 vertexNormal;
out vec3 toLightVector;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightPosition;

void main() {
	vec4 modelPosition = transformationMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
	
	textureCoords = inTextureCoords;
	vertexNormal = (transformationMatrix * vec4(inVertexNormal, 0.0)).xyz;
	toLightVector = modelPosition.xyz - lightPosition; 
}
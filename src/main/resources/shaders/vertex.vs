#version 330

in vec3 position;
in vec2 inTextureCoords;
in vec3 inVertexNormal;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	vec4 modelViewPosition = modelViewMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;
	
	textureCoords = inTextureCoords;
}
#version 330

in vec2 textureCoords;
in vec3 vertexNormal;
in vec3 toLightVector;

out vec4 fragColour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;

void main() {
	vec3 unitVertexNormal = normalize(vertexNormal);
	vec3 unitToLightVector = normalize(toLightVector);
	
	float brightness = max(dot(unitVertexNormal, unitToLightVector), 0.0);
	vec3 diffuse = brightness * lightColour;
	
	fragColour = vec4(diffuse, 1.0) * texture(textureSampler, textureCoords);
}
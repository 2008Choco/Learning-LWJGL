#version 330

in vec2 textureCoords;

out vec4 fragColour;

uniform sampler2D textureSampler;
uniform vec3 colour;
uniform bool useColour;

void main() {
	if (useColour) {
		fragColour = vec4(colour, 1);
	}
	else {
		fragColour = texture(textureSampler, textureCoords);
	}
}
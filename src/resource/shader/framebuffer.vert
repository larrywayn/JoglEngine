#version 400

uniform mat4 projMat;

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uvs;
out vec3 fragVert;
out vec2 vUV;

void main(void) {
    fragVert = position;
    vUV = uvs;
    gl_Position = vec4(position, 1.0f);
}
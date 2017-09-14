#version 420

uniform mat4 projMat;
uniform mat4 modelMat;

layout (location = 0) in vec3 position;
layout (location = 2) in vec2 uvs;
out vec2 vUV;

void main(void) {
    vUV = uvs;
    gl_Position = projMat * modelMat * vec4(position.xyz, 1.0f);
}
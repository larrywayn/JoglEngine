#version 420

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uvs;
out vec2 vUV;

void main(void) {
    vUV = uvs;
    gl_Position = vec4(position.xy, 0.0f, 1.0f);
}
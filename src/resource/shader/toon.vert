#version 420
uniform mat4 projMat;
uniform mat4 camMat;
uniform mat4 modelMat;

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 uvs;
out vec3 vNormal;

void main(void) {  
    vNormal = normal;
    gl_Position =   projMat * camMat *  modelMat *  vec4(position, 1.0f);
}
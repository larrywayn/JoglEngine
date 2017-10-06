#version 420
const vec2 madd=vec2(0.0,1.0);
uniform mat4 projMat;
uniform mat4 modelMat;
uniform vec2 posS ;
layout (location = 0) in vec3 position;
//layout (location = 1) in vec2 uvs;
out vec2 vUV;

void main(void) {
 mat4 orthomatrix = mat4(
        2.0/posS.x, 0, 0, 0,
        0, -2.0/-posS.y, 0, 0,
        0, 0, -1, 0,
        -1, -1, 0, 1
        );

    vUV = position.xy*madd+madd;
    gl_Position = orthomatrix * modelMat * vec4(position.xy, 0.0f, 1.0f);
}
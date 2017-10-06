#version 420
const vec2 madd = vec2(0.5,0.5);

layout (location = 0) in vec3 position;
out vec2 vUV;

void main(void) {
    vUV = position.xy * madd + madd; 
    gl_Position = vec4(position.xy, 0.0f, 1.0f);
}
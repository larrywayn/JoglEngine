#version 400

out vec4 glFragColor;
in vec3 fragVert;
in vec2 vUV;

uniform sampler2D texture0;

layout(location = 0) out vec4 color;
void main(){
           glFragColor = vec4(vec3(0.0f,0.5f,0.5f),1.0f);
}
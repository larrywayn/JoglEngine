#version 420

smooth in vec2 vUV;
uniform float isTex;
uniform sampler2D texture0; 
layout(location=0) out vec4 color;

void main(){
    if(isTex == 1.0f){
        color = vec4(texture2D(texture0, vUV));
        //color = vec4(0.0f,1.0f,0.0f,1.0f);
    }else{
        color = vec4(0.0f,1.0f,0.0f,1.0f);
    }
}
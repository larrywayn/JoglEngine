#version 420

in vec2 vUV;
uniform sampler2D texture0;
//uniform sampler2D texture1;
layout(location = 0) out vec4 color;

void main(){
    vec4 colorA = texture2D(texture0, vUV);
   // vec4 colorB = texture2D(texture1, vUV);
   // float _Blend1 = 0.9;
   // vec4 mainOutput = colorA.rgba * (1.0 - (colorB.a * _Blend1));
    //vec4 blendOutput = colorB.rgba * colorB.a * _Blend1;
color = vec4(0.5f,0.8f,0.7f,1.0f);
//colorA;
//vec4(vec3(vUV.x * vUV.x + vUV.y * vUV.y),1.0); 
//vec4(vec3(vUV.x,vUV.y,0.0),1.0);
//vec4((mainOutput.rgb + blendOutput.rgb), (mainOutput.a + blendOutput.a));
}
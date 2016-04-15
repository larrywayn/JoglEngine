#version 400

out vec4 glFragColor;
in vec3 vNormal;
in vec3 fragVert;
in vec2 vUV;
uniform mat4 camMat;
uniform float isTex;
uniform mat4 modelMat;
uniform sampler2D texture0;

void main(){
    mat3 mm = mat3(camMat);
    mm = transpose(inverse(mm));
    vec3 normal = normalize(mm * vNormal);
    vec3 fragPosition = vec3(modelMat * vec4(fragVert, 1.0f));

    vec3 surfaceToLight = vec3(0.0f,0.0f,1.0f) - fragPosition;
    float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
    brightness = clamp(brightness, 0.0f, 1.0f);

    if(isTex == 1.0f){
        vec4 colorT = texture2D(texture0, vUV);
        glFragColor = vec4(brightness * vec3(colorT.rgb),1.0f);
    }else{
        glFragColor = vec4(brightness * vec3(1.0f,0.0f,0.0f),1.0f);
    }
}
#version 400

out vec4 glFragColor;
in vec3 vNormal;
in vec3 fragVert;
uniform mat4 camMat;
uniform mat4 modelMat;

void main(){
mat3 mm = mat3(camMat);
mm = transpose(inverse(mm));
vec3 normal = normalize(mm * vNormal);
 //calculate the location of this fragment (pixel) in world coordinates
    vec3 fragPosition = vec3(modelMat * vec4(fragVert, 1.0f));

    //calculate the vector from this pixels surface to the light source
    vec3 surfaceToLight = vec3(0.0f,0.0f,1.0f) - fragPosition;
    //calculate the cosine of the angle of incidence
    float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
    brightness = clamp(brightness, 0.0f, 1.0f);

	glFragColor = vec4(brightness * vec3(1.0f,0.0f,0.0f),1.0f);
}
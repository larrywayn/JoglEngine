#version 420

in vec3 vNormal;
uniform mat4 camMat;
uniform mat4 modelMat;
layout(location = 0) out vec4 color;

void main(){
    mat3 mm = mat3(camMat) * mat3(modelMat);
    mm = transpose(inverse(mm));
    vec3 normal = normalize(mm * vNormal);  
 vec3 lightDir = vec3(0.8,0.7,0.6);
  float  intensity = dot(lightDir,normal);

	if (intensity > 0.95)
		color = vec4(0.5,1.0,0.5,1.0);
	else if (intensity > 0.5)
		color = vec4(0.3,0.6,0.3,1.0);
	else if (intensity > 0.25)
		color = vec4(0.2,0.4,0.2,1.0);
	else
		color = vec4(0.1,0.2,0.1,1.0);
}
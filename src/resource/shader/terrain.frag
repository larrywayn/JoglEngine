#version 400

in vec3 vNormal;
in vec3 fragVert;
in vec2 vUV;
uniform mat4 camMat;
uniform float isTex;
uniform mat4 modelMat;
uniform float zufall;
uniform sampler2D texture0;
uniform vec3 colorIn;
layout(location = 0) out vec4 color;

void main(){
  /*  
    vec3 normal = normalize(mm * vNormal);
    vec3 fragPosition = vec3(modelMat * vec4(fragVert, 1.0f));

vec3 Eye = normalize(-fragVert);
vec3 Reflected = normalize( 2.0 * dot(normal, lightvec) * normal - lightvec);
 */  
  vec3 lightvec = vec3(0.0f,0.5f,1.0f);
mat3 mm = mat3(camMat * modelMat);
    mm = transpose(inverse(mm));
    vec3 normal = normalize(mm * vNormal);
    vec3 fragPosition = vec3(modelMat * vec4(fragVert, 1.0f));

vec4 base;
 if(isTex == 2.0f){  
        base = vec4(colorIn,1.0f);
    } else {
       if(isTex == 1.0f){
           vec4 colorT = texture2D(texture0, vUV);
           base = vec4(vec3(colorT.rgb),1.0f);
       }else{
        float height = fragPosition.y;
        if (height < -40.0f)
		base =  vec4(0.219, 0.164, 0.141,1.0);
        else if (height < -30.0f)
		base =  vec4(0.517, 0.305, 0.164,1.0);
	else if (height < 20.0f)
		base =  vec4(0.321, 0.427, 0.211,1.0);
	else if (height < 30.0f)
		base =  vec4(0.321, 0.376, 0.345,1.0);
	else if (height < 60.0f)
		base =  vec4(0.388, 0.388, 0.388,1.0); 
	else
		base = vec4(0.847, 0.921, 0.933,1.0);
         //  color = vec4(brightness * vec3(0.0f,0.5f,0.9f),1.0f);
       }
    }

//vec3 base = vec3(0.5f,0.5f,0.5f); //texture2D(diffspec,vec2(gl_TexCoord[0])).xyz;
vec4 lightsource = vec4(0.988f, 0.984f, 0.870f, 0.5f);
vec4 IAmbient =  vec4(0.5f,0.5f,0.5f,0.5f); //gl_LightSource[0].ambient;
vec4 IDiffuse =  lightsource * max(dot(normal, lightvec), 0.0) ; //gl_LightSource[0].diffuse * max(dot(normal, lightvec), 0.0);
float lambertTerm = dot(normal,lightvec);
vec4 finalcolor = IAmbient * base * (1.0-lambertTerm);


   if (lambertTerm > 0.0)
{
finalcolor += lightsource * base * lambertTerm;
vec3 E = normalize(-fragVert);
vec3 R = reflect(-lightvec, normal);
float specular = pow( max(dot(R, E), 0.0), 1.0 );
finalcolor += lightsource * 0.8 * (specular);
}
color = vec4(finalcolor.xyz,1.0);

}



/*
uniform sampler2D diffspec;
uniform sampler2D diffspec2;
varying vec3 normal;
varying vec3 v;
varying vec3 lightvec;
void main(void)
{


if (lambertTerm > 0.0)
{
finalcolor += gl_LightSource[0].diffuse * vec4(base,1.0) * lambertTerm;
vec3 E = normalize(-fragVert);
vec3 R = reflect(-lightvec, normal);
float specular = pow( max(dot(R, E), 0.0), 120.0 );
finalcolor += gl_LightSource[0].diffuse * texture2D(diffspec2,vec2(gl_TexCoord[0])).a * texture2D(diffspec,vec2(gl_TexCoord[0])).a * (specular*3.0);
}
gl_FragColor = vec4(finalcolor.xyz,1.0);
}*/
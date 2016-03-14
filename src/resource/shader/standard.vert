#version 400

uniform vec3 abc;
uniform mat4 projMat;
uniform mat4 tmpMat;
uniform vec4 viewQuat;
uniform vec4 modelQuat;

uniform vec3 viewPos;
uniform vec3 modelPos;

layout (location = 0) in vec3 position;

vec4 quatMult( vec4 q1, vec4 q2 ) {
   vec4 r;
   r.x   = q1.x * q2.x - dot( q1.yzw, q2.yzw );
   r.yzw = q1.x * q2.yzw + q2.x * q1.yzw + cross( q1.yzw, q2.yzw );
   return r;
}

vec3 vecMult(vec4 q, vec3 v){
    vec3 temp = cross(q.xyz, v) + q.w * v;
    return (cross(temp, -q.xyz) + dot(q.xyz,v) * q.xyz + q.w * temp);
}

void main(void) {
    vec4 worldModelQuat = quatMult(viewQuat , modelQuat);
    gl_Position = projMat * tmpMat * vec4( position, 1.0);
    //gl_Position  = projMat * vec4(viewPos,1.0) * vec4(vecMult(viewQuat ,modelPos),1.0) * vec4(vecMult(worldModelQuat  , position), 1.0);
}
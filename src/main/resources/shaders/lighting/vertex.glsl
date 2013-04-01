#version 330 core

layout (location = 0) in vec4 in_Position;
layout (location = 1) in vec3 in_Normal;
layout (location = 2) in vec2 in_TexCoord;
layout (location = 3) in vec4 in_Color;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix
uniform mat3 normalMatrix;           // inverted and transposed of the modelMatrix or just the 3x3 piece  
uniform vec3 lightPosition;          // in worldspace

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;                // normal in eyespace
out vec3 pass_Position;              // vertex position in eyespace
out vec3 pass_LightPosition;


void main(void) {

    pass_Normal = normalize(normalMatrix * in_Normal);
    pass_Color = in_Color;
    pass_TextureCoord = in_TexCoord;
    pass_LightPosition = vec3(worldToCameraMatrix * vec4(lightPosition,0));
    pass_Position = vec3(worldToCameraMatrix * modelToWorldMatrix * in_Position);

    gl_Position = cameraToClipMatrix 
                  * worldToCameraMatrix 
                  * modelToWorldMatrix 
                  * in_Position;
 
}

#version 330 core

layout (location = 0) in vec4 in_Position;
layout (location = 1) in vec3 in_Normal;
layout (location = 2) in vec2 in_TexCoord;
layout (location = 3) in vec4 in_Color;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix
uniform mat3 normalMatrix;            // 
uniform vec4 lightPosition;

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec4 pass_LightPosition;
out vec3 pass_Normal;

// checkout: http://wiki.delphigl.com/index.php/Tutorial_glsl

void main(void) {

    vec4 worldPos = modelToWorldMatrix * in_Position;
    vec4 cameraPos = worldToCameraMatrix * worldPos;
    gl_Position = cameraToClipMatrix * cameraPos;
    
    pass_Normal = normalize(normalMatrix * in_Normal);

    pass_Color = in_Color;
    pass_TextureCoord = in_TexCoord;
    pass_LightPosition = lightPosition;
}

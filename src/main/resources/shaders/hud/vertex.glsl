#version 330 core

layout (location = 0) in vec4 in_Position;
layout (location = 1) in vec4 in_Color;
layout (location = 2) in vec2 in_TexCoord;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * in_Position;
    pass_Color = in_Color;
    pass_TextureCoord = in_TexCoord;
}

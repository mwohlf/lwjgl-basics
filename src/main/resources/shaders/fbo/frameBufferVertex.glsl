#version 330 core

layout (location = 0) in vec4 in_Position;
layout (location = 1) in vec4 in_Color;
layout (location = 2) in vec2 in_TexCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
    gl_Position = in_Position;
    pass_Color = in_Color;
    pass_TextureCoord = in_TexCoord;
}

#version 330 core

uniform sampler2D texture2d;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
    out_Color = texture(texture2d, pass_TextureCoord);
}

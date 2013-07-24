#version 330 core

uniform sampler2D uniformTexture;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
    out_Color = texture2D(uniformTexture, pass_TextureCoord);
}

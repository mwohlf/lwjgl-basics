#version 330 core

uniform sampler2D uniformTexture;

in vec4 pass_Color;

out vec4 out_Color;

void main(void) {
    out_Color = pass_Color;
}

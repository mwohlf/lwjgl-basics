#version 330 core

// fragments are possible pixels

uniform sampler2D texture01;
uniform sampler2D texture02;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;          // must be already normalized
in vec3 pass_Position;

out vec4 gl_FragColor;

void main(void) {
    gl_FragColor = texture2D(texture01, pass_TextureCoord) * pass_Color;
}

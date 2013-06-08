#version 330 compatibility

uniform sampler2D texture01;
uniform sampler2D texture02;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;          // must be already normalized
in vec3 pass_Position;
in vec3 pass_LightPosition;


void main(void) {
    vec4 lightColor = vec4 (1, 1, 1, 0.0);

    vec3 lightRay = normalize(pass_LightPosition - pass_Position);

    gl_FragColor = texture2D(texture01, pass_TextureCoord)
            * max(dot(pass_Normal, lightRay), 0.1)
            * lightColor;

}

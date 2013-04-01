#version 330 compatibility

uniform sampler2D texture01;
uniform sampler2D texture02;

in vec4 pass_Color;
in vec3 pass_Normal;
in vec2 pass_TextureCoord;

out vec4 out_Color;

// checkout: http://iloveshaders.blogspot.de/2011/04/implementing-basic-lights.html

void main(void) {
    vec4 light = vec4 (1, 1, 1, 0.0);  
    vec3 lightdir = normalize( vec3 (-1.0, 1.0, 1.0) );

    // Override out_Color with our texture pixel
    out_Color = texture2D(texture01, pass_TextureCoord) 
            * max(dot(pass_Normal, lightdir), 0.2)
            * light;
}

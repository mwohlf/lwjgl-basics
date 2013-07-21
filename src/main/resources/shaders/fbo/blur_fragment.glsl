#version 330 core

uniform sampler2D uniformTexture;
uniform vec2 dir; // blur direction: (1.0, 0.0) -> x-axis blur, (0.0, 1.0) -> y-axis blur
uniform float dist; // how far off center to sample from


in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
    //this will be our RGBA sum
    vec4 sum = vec4(0.0);

    //our original texcoord for this fragment
    vec2 tc = pass_TextureCoord;
    vec4 pc = pass_Color;

    float hstep = dir.x;
    float vstep = dir.y;

    float s = 1;

    sum += texture2D(uniformTexture, vec2(tc.x - 4.0*dist*hstep, tc.y - 4.0*dist*vstep)) * 0.0162162162 * s;
    sum += texture2D(uniformTexture, vec2(tc.x - 3.0*dist*hstep, tc.y - 3.0*dist*vstep)) * 0.0540540541 * s;
    sum += texture2D(uniformTexture, vec2(tc.x - 2.0*dist*hstep, tc.y - 2.0*dist*vstep)) * 0.1216216216 * s;
    sum += texture2D(uniformTexture, vec2(tc.x - 1.0*dist*hstep, tc.y - 1.0*dist*vstep)) * 0.1945945946 * s;

    vec4 current = texture2D(uniformTexture, vec2(tc.x, tc.y));
    sum += current * 0.2270270270 * s;

    sum += texture2D(uniformTexture, vec2(tc.x + 1.0*dist*hstep, tc.y + 1.0*dist*vstep)) * 0.1945945946 * s;
    sum += texture2D(uniformTexture, vec2(tc.x + 2.0*dist*hstep, tc.y + 2.0*dist*vstep)) * 0.1216216216 * s;
    sum += texture2D(uniformTexture, vec2(tc.x + 3.0*dist*hstep, tc.y + 3.0*dist*vstep)) * 0.0540540541 * s;
    sum += texture2D(uniformTexture, vec2(tc.x + 4.0*dist*hstep, tc.y + 4.0*dist*vstep)) * 0.0162162162 * s;


    //out_Color = pass_Color;
    //out_Color = texture2D(uniformTexture, pass_TextureCoord);

    //pc = vec4(1.0, 1.0, 1.0, 1.0);
    //out_Color = pc * sum;
    //out_Color = vec4(sum.rgb, 1.0);

    //sum.a = max(sum.a, current.a);

    out_Color = sum;
}

#version 330 core

uniform sampler2D uniformTexture;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
    //this will be our RGBA sum
    vec4 sum = vec4(0.0);

    //our original texcoord for this fragment
    vec2 tc = pass_TextureCoord;

    vec4 pc = pass_Color;

    //the amount to blur, i.e. how far off center to sample from
    //1.0 -> blur by one pixel
    //2.0 -> blur by two pixels, etc.
    float blur = 0.005;

    float hstep = 1.0;
    float vstep = 0.0;

    sum += texture2D(uniformTexture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;
    sum += texture2D(uniformTexture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;
    sum += texture2D(uniformTexture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;
    sum += texture2D(uniformTexture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;

    sum += texture2D(uniformTexture, vec2(tc.x, tc.y)) * 0.2270270270;

    sum += texture2D(uniformTexture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;
    sum += texture2D(uniformTexture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;
    sum += texture2D(uniformTexture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;
    sum += texture2D(uniformTexture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;


    //out_Color = pass_Color;
    //out_Color = texture2D(uniformTexture, pass_TextureCoord);

    pc = vec4(1.0, 1.0, 1.0, 0.0);
    out_Color = pc * vec4(sum.rgb, 1.0);
    //out_Color = vec4(sum.rgb, 1.0);


}

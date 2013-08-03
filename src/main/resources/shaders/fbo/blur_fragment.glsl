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
    vec2 flipped_texcoord = vec2(pass_TextureCoord.x, 1.0 - pass_TextureCoord.y);
    vec2 tc = flipped_texcoord;
    vec4 pc = pass_Color;

    float hstep = dir.x;
    float vstep = dir.y;


    // check: http://home.comcast.net/~tom_forsyth/blog.wiki.html#[[Premultiplied%20alpha]]

    sum += texture2D(uniformTexture, vec2(tc.x - 4.0*dist*hstep, tc.y - 4.0*dist*vstep)) * 0.0162162162;
    sum += texture2D(uniformTexture, vec2(tc.x - 3.0*dist*hstep, tc.y - 3.0*dist*vstep)) * 0.0540540541;
    sum += texture2D(uniformTexture, vec2(tc.x - 2.0*dist*hstep, tc.y - 2.0*dist*vstep)) * 0.1216216216;
    sum += texture2D(uniformTexture, vec2(tc.x - 1.0*dist*hstep, tc.y - 1.0*dist*vstep)) * 0.1945945946;

    sum += texture2D(uniformTexture, vec2(tc.x, tc.y)) * 0.2270270270;

    sum += texture2D(uniformTexture, vec2(tc.x + 1.0*dist*hstep, tc.y + 1.0*dist*vstep)) * 0.1945945946;
    sum += texture2D(uniformTexture, vec2(tc.x + 2.0*dist*hstep, tc.y + 2.0*dist*vstep)) * 0.1216216216;
    sum += texture2D(uniformTexture, vec2(tc.x + 3.0*dist*hstep, tc.y + 3.0*dist*vstep)) * 0.0540540541;
    sum += texture2D(uniformTexture, vec2(tc.x + 4.0*dist*hstep, tc.y + 4.0*dist*vstep)) * 0.0162162162;



    //out_Color = pass_Color;
    //out_Color = texture2D(uniformTexture, pass_TextureCoord);

    //pc = vec4(1.0, 1.0, 1.0, 0.5);
    //out_Color = pc * vec4(sum.rgb, 1.0);
    //out_Color = vec4(sum.rgb, 1.0);

    //sum.a = max(sum.a, current.a);
    //out_Color = vec4(sum.rgb, 0.0);
    out_Color = sum;
}

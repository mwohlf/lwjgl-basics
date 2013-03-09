#version 330 compatibility

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix


layout (location = 1) in vec4 in_Position;
layout (location = 0) in vec4 in_Color;
layout (location = 2) in vec2 in_TexCoord;
layout (location = 3) in vec2 in_Normal;

// uniform vec4 uni_Color;


out vec4 pass_Color;
out vec2 pass_TextureCoord;

// predefined variable names:
//  out gl_PerVertex {
//     vec4  gl_Position;
//     float gl_PointSize;
//     float gl_ClipDistance[];
//  };

void main(void) {
    gl_PointSize = 1;
    //gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * in_Position;

    // step 2: move, rotate, morph the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
    // read: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
    // need to do lights and stuff here

    // step 3: project the object from 3D cam space into 2D view
    gl_Position = cameraToClipMatrix * cameraPos;


    pass_Color = in_Color;
    //pass_Color = vec4(1.0f, 1.0f, 0.0f, 1.0f);
    pass_TextureCoord = in_TexCoord;
}

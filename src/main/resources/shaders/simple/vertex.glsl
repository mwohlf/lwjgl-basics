#version 330 compatibility

// see: http://www.lighthouse3d.com/cg-topics/code-samples/opengl-3-3-glsl-1-5-sample/
// see: http://www.arcsynthesis.org/gltut/Basics/Tut02%20Vertex%20Attributes.html


layout (location = 0) in vec4 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 textureCoord;

uniform mat4 projectionMatrix; // cameraToClipMatrix
uniform mat4 viewMatrix;       // worldToCameraMatrix
uniform mat4 modelMatrix;      // modelToWorldMatrix


out vec4 pass_Color;
out vec2 pass_TextureCoord;

// we need to write:
//   vec4 gl_Position
//   float gl_PointSize

void main(void) {
    //gl_PointSize = 1;
    //gl_Position = projectionMatrix * viewMatrix * modelMatrix * position;

    // step 1: move, rotate the object
    vec4 worldPos = modelMatrix * position;

    // step 2: move, rotate the object according to the cam direction/ position:
    vec4 cameraPos = viewMatrix * worldPos;

    // http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
    // read: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
    // need to do lights and stuff here

    // step 3: project the cobject from cam space into 2D view
    gl_Position = projectionMatrix * cameraPos;


    pass_Color = color;
    pass_TextureCoord = textureCoord;
}

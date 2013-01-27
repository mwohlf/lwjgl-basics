#version 330 compatibility


layout (location = 0) in vec4 in_Position;
layout (location = 1) in vec4 in_Color;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix

out vec4 pass_Color;

void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * in_Position;

    // step 2: move, rotate, morph the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    gl_Position = cameraToClipMatrix * cameraPos;


    pass_Color = in_Color;

}

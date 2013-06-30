#version 330 core

layout (location = 1) in vec3 in_Position;
layout (location = 0) in vec4 in_Color;
layout (location = 2) in vec2 in_TexCoord;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix

out vec4 pass_Color;
out vec2 pass_TextureCoord;

// predefined variable names:
//  out gl_PerVertex {
//     vec4  gl_Position;
//     float gl_PointSize;
//     float gl_ClipDistance[];
//  };

void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * vec4(in_Position, 1.0);

    // step 2: move, rotate, morph the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    gl_Position = cameraToClipMatrix * cameraPos;

    pass_Color = in_Color;
    pass_TextureCoord = in_TexCoord;
}

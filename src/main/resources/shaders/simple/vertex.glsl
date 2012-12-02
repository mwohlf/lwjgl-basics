#version 330 compatibility

// see: http://www.lighthouse3d.com/cg-topics/code-samples/opengl-3-3-glsl-1-5-sample/
// see: http://www.arcsynthesis.org/gltut/Basics/Tut02%20Vertex%20Attributes.html


layout (location = 0) in vec4 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 textureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;


out vec4 pass_Color;
out vec2 pass_TextureCoord;

// we need to write:
//   vec4 gl_Position
//   float gl_PointSize

void main(void) {
    gl_PointSize = 1;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * position;
    pass_Color = color;
    pass_TextureCoord = textureCoord;
}

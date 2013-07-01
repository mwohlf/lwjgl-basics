#version 330 core

//
//  see: http://pyopengl.sourceforge.net/context/tutorials/shader_6.xhtml
//       http://http.developer.nvidia.com/CgTutorial/cg_tutorial_chapter05.html
//       http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
//       http://www.learnopengles.com/android-lesson-three-moving-to-per-fragment-lighting/

layout (location = 0) in vec3 in_Position;
layout (location = 1) in vec3 in_Normal;
layout (location = 2) in vec2 in_TexCoord;
layout (location = 3) in vec4 in_Color;      // RGBA

struct VertexLight {
  float attenuation;
  vec4 diffuse;
  vec3 position;
};
uniform VertexLight lights[2];

uniform mat4 modelToWorldMatrix;     // modelMatrix contains model position
uniform mat4 worldToCameraMatrix;    // viewMatrix contains camera position and directons
uniform mat4 cameraToClipMatrix;     // projectionMatrix contains the view angle
// ModelviewMatrix = worldToCameraMatrix * modelToWorldMatrix
uniform mat3 normalMatrix;           // == inverse(transpose(ModelviewMatrix)), or just the 3x3 piece;

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;                // normal in eye-space
out vec3 pass_Position;              // vertex position in eye-space

out vec4 gl_Position;


void main(void) {

    // transforms the vertex into eye space
    pass_Position = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0));

    // transforms the normal's orientation into eye space
    pass_Normal = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(in_Normal, 0.0));


    // needed for attenuation.
    float distance = length(lights[0].position - pass_Position);

    // Get a lighting direction vector from the light to the vertex.
    vec3 lightVector = normalize(lights[0].position - pass_Position);

    // Calculate the dot product of the light vector and vertex normal.
    // If the normal and light vector are pointing in the same direction then it will get max illumination.
    float diffuse = max(dot(pass_Normal, lightVector), 0.1);

    // Attenuate the light based on distance.
    diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));

    // Multiply the color by the illumination level. It will be interpolated across the triangle.
    pass_Color = in_Color * diffuse;

    // nothing happens to the texture coords
    pass_TextureCoord = in_TexCoord;

    // gl_Position is a special variable used to store the final position.
    // its a vec4() in normalized screen coordinates calculated by
    // transferring the in_Position from model-space to world-space to view/cam-space to clip-space
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0);

}

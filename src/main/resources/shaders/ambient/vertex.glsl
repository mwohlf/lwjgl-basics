#version 330 core

//
//  see: http://pyopengl.sourceforge.net/context/tutorials/shader_6.xhtml
//       http://http.developer.nvidia.com/CgTutorial/cg_tutorial_chapter05.html
//       http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
//       http://www.learnopengles.com/android-lesson-three-moving-to-per-fragment-lighting/
//       http://en.wikibooks.org/wiki/GLSL_Programming/Unity/Multiple_Lights
//

// vertex position in model space
layout (location = 0) in vec3 in_Position;
// the normal for the current vertex
layout (location = 1) in vec3 in_Normal;
// the texture position of the vertex
layout (location = 2) in vec2 in_TexCoord;

struct VertexLight {
  float attenuation;
  vec4 diffuse;
  vec3 position;
};
uniform VertexLight lights[10];

// a matrix that transforms the model into the world-space,
// the modelToWorldMatrix is created by using the models world position and rotation
uniform mat4 modelToWorldMatrix;

// a matrix that transforms the vertex from world-space into the cam-space
// the worldToCameraMatrix contains camera position and direction
uniform mat4 worldToCameraMatrix;


uniform mat4 cameraToClipMatrix;     // projectionMatrix contains the view angle
// ModelviewMatrix = worldToCameraMatrix * modelToWorldMatrix
uniform mat3 normalMatrix;           // == inverse(transpose(ModelviewMatrix)), or just the 3x3 piece;

out vec4 pass_Light;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;                // normal in eye-space
out vec3 pass_Position;              // vertex position in eye-space

out vec4 gl_Position;


void main(void) {
    // gl_Position is a special variable used to store the final position.
    // its a vec4() in normalized screen coordinates calculated by
    // transferring the in_Position from model-space to world-space to view/cam-space to clip-space
    // the 1.0 is needed to do translations with the matrices
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0);

    // transforms the vertex into cam-space
    pass_Position = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0));
    // transforms the normal's orientation into cam-space
    pass_Normal =  normalize(vec3(in_Normal * normalMatrix));
    // nothing happens to the texture coords, they are piced up in the fragment shader and mapped to
    // one of the textures that are bound
    pass_TextureCoord = in_TexCoord;

    pass_Light = vec4(0,0,0,0);
    for (int index = 0; index < 2; index++) {
       vec3 lightPos = lights[index].position;
       vec3 vertexPos = vec3(modelToWorldMatrix * vec4(in_Position, 1.0));
       vec3 lightVector = normalize(lights[index].position - vertexPos);

       // the dot product of the light vector and vertex normal is a indication for the amount of light on the surface
       float diffuse = max(dot(pass_Normal, lightVector), 0.0);

       // Attenuate the light based on distance.
       float distance = max(length(lights[index].position - vertexPos), 0.0);

       diffuse = diffuse * (1.0 / (1.0 + (lights[index].attenuation * distance * distance)));

       // Multiply the color by the illumination level. It will be interpolated across the triangle.
       pass_Light = pass_Light + lights[index].diffuse * diffuse;
    }

}

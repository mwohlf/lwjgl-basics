#version 330 core

//
//  see: http://pyopengl.sourceforge.net/context/tutorials/shader_6.xhtml
//       http://http.developer.nvidia.com/CgTutorial/cg_tutorial_chapter05.html
//       http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders
//       http://www.learnopengles.com/android-lesson-three-moving-to-per-fragment-lighting/


// vertex position in model space
layout (location = 0) in vec3 in_Position;
// the normal for the current vertex
layout (location = 1) in vec3 in_Normal;
// the texture position of the vertex
layout (location = 2) in vec2 in_TexCoord;

struct LightSource {
  vec4 ambient;
  vec4 diffuse;
  vec4 specular;
  vec3 position;
  vec3 direction;
};
uniform LightSource lights[10];       // see: http://www.lwjgl.org/wiki/index.php?title=GLSL_Tutorial:_Communicating_with_Shaders

// a matrix that transforms the model into the world-space, 
// the modelToWorldMatrix is created by using the models world position and rotation
uniform mat4 modelToWorldMatrix;

// a matrix that transforms the vertex from world-space into the cam-space
// the worldToCameraMatrix contains camera position and direction
uniform mat4 worldToCameraMatrix;    


uniform mat4 cameraToClipMatrix;     // projectionMatrix contains the view angle
// ModelviewMatrix = worldToCameraMatrix * modelToWorldMatrix
uniform mat3 normalMatrix;           // == inverse(transpose(ModelviewMatrix)), or just the 3x3 piece;

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;                // normal in eye-space
out vec3 pass_Position;              // vertex position in eye-space

out vec4 gl_Position;


void main(void) {

    // transforms the vertex into cam-space
    pass_Position = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0));

    // transforms the normal's orientation into cam-space
    pass_Normal = vec3(normalMatrix * in_Normal);

    // get the vector from the light to the vertex in cam-space
    vec3 lightPos = vec3(worldToCameraMatrix * vec4(lights[0].position, 1.0));
    vec3 lightVector = normalize(lightPos - pass_Position);
    float distance = max(length(lightPos - pass_Position), 0.0);

    // the dot product of the light vector and vertex normal is a
    float diffuse = max(dot(pass_Normal, lightVector), 0.1);

    // Attenuate the light based on distance.
    diffuse = diffuse * (1.0 / (1.0 + (0.002 * distance * distance)));
    //diffuse = 1.0;

    // Multiply the color by the illumination level. It will be interpolated across the triangle.
    pass_Color = lights[0].diffuse * diffuse + lights[0].ambient;



    // nothing happens to the texture coords, they are piced up in the fragment shader and mapped to
    // one of the textures that are bound
    pass_TextureCoord = in_TexCoord;

    // gl_Position is a special variable used to store the final position.
    // its a vec4() in normalized screen coordinates calculated by
    // transferring the in_Position from model-space to world-space to view/cam-space to clip-space
    // the 1.0 is needed to do translations with the matrices
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * vec4(in_Position, 1.0);

}

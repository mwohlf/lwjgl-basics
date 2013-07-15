#version 330 core

struct VertexLight {
  float attenuation;
  vec4 diffuse;
  vec3 position;
};

struct Material {
  vec3 ambient;
  vec4 diffuse;
  vec3 specular;
  float shininess;
};

layout (location = 0) in vec3 in_Position;    // vertex position in model space
layout (location = 1) in vec3 in_Normal;      // the normal for the current vertex
layout (location = 2) in vec2 in_TexCoord;    // the texture position of the vertex


uniform VertexLight lights[10];
uniform Material material;

uniform mat4 modelToWorldMatrix;
uniform mat4 worldToCameraMatrix;
uniform mat4 cameraToClipMatrix;
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


       //diffuse = 0.5; // for testing only

       // Multiply the color by the illumination level. It will be interpolated across the triangle.
       pass_Light = pass_Light + lights[index].diffuse * diffuse;
    }

}

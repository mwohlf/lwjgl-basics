package net.wohlfart.gl.shader.mesh;

import net.wohlfart.gl.renderer.IsRenderable;

public class ParticleMesh implements IsRenderable {


    private ParticleMesh() {

    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }



    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }



    public static final class Builder {


        public ParticleMesh build() {
            return new ParticleMesh();
        }
    }

}

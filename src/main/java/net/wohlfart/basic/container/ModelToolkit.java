package net.wohlfart.basic.container;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.antlr4.ModelLoader;
import net.wohlfart.gl.spatial.Model;

public final class ModelToolkit {

    public static Model loadModelFromFile(String path) {
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new GenericGameException("input stream is null for path '" + path + "'");
            }
            return new ModelLoader().getModel(inputStream);
        } catch (final IOException ex) {
            throw new GenericGameException("I/O Error while loading model from file with path '" + path + "'", ex);
        }
    }

    public static Model createCube() {
        return loadModelFromFile("/models/cube/cube.obj");
    }

    public static Model createIcosphere() {
        return loadModelFromFile("/models/icosphere/icosphere.obj");
    }

    public static Model createShip01() {
        return loadModelFromFile("/models/ships/01.obj");
    }

    public static Model createShip02() {
        return loadModelFromFile("/models/ships/02.obj");
    }

}

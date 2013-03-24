package net.wohlfart.basic.states;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.antlr4.ModelLoader;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.elements.ColoredQuad;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.elements.debug.Circle;
import net.wohlfart.gl.elements.debug.Cube;
import net.wohlfart.gl.elements.debug.Icosphere;
import net.wohlfart.gl.elements.debug.Tetrahedron;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>SceneCreator for providing dummy data to test rendering for different states.</p>
 */
final class SceneCreator {

    static Collection<IsRenderable> createCircledTarget() {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();
        elemBucket.add(new Circle(1));
        elemBucket.add(new Circle(10));
        elemBucket.add(new Circle(20));
        elemBucket.add(new Circle(30));
        elemBucket.add(new Circle(40));
        elemBucket.add(new Circle(50));
        elemBucket.add(new Circle(60));
        elemBucket.add(new Circle(70));
        elemBucket.add(new Circle(80));
        elemBucket.add(new Circle(90));
        elemBucket.add(new Circle(100));
        return elemBucket;
    }

    static Collection<IsRenderable> createRandomLocatedSpheres() {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();
        GraphicContextManager graphContext = GraphicContextManager.INSTANCE;
        float farPlane= graphContext.getFarPlane();
        for (int i = 0; i < 10000; i++) {
            final float x = SimpleMath.random(-farPlane, farPlane);
            final float y = SimpleMath.random(-farPlane, farPlane);
            final float z = SimpleMath.random(-farPlane, farPlane);

            AbstractRenderable mesh = new Icosphere(1, 1).withColor(ReadableColor.CYAN);
            mesh.setTranslation(new Vector3f(x, y, z));
            elemBucket.add(mesh);
        }
        return elemBucket;
    }


    static Collection<IsRenderable> createOriginAxis() {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();
        elemBucket.add(new Arrow(new Vector3f(1, 0, 0)).withColor(ReadableColor.RED));
        elemBucket.add(new Arrow(new Vector3f(0, 1, 0)).withColor(ReadableColor.GREEN));
        elemBucket.add(new Arrow(new Vector3f(0, 0, 1)).withColor(ReadableColor.BLUE));
        return elemBucket;
    }

    static Collection<IsRenderable> createDebugElements() {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();
        elemBucket.add(new Arrow(new Vector3f(1, 0, 0)).withColor(ReadableColor.GREEN).withTranslation(new Vector3f(-10,0,0)));
        elemBucket.add(new Circle(1).withColor(ReadableColor.RED).withTranslation(new Vector3f(-8,0,0)));
        elemBucket.add(new Cube(1).withColor(ReadableColor.BLUE).withTranslation(new Vector3f(-4,0,0)));
        elemBucket.add(new Icosphere().withColor(ReadableColor.CYAN).withTranslation(new Vector3f(-0,0,0)));
        elemBucket.add(new Tetrahedron().withColor(ReadableColor.DKGREY).withTranslation(new Vector3f(3,0,0)));
        return elemBucket;
    }




    static Collection<IsRenderable> createRandomElements() {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();

        elemBucket.add(new Arrow(new Vector3f(1, 0, 0)).withColor(ReadableColor.RED));
        elemBucket.add(new Arrow(new Vector3f(0, 1, 0)).withColor(ReadableColor.GREEN));
        elemBucket.add(new Arrow(new Vector3f(0, 0, 1)).withColor(ReadableColor.BLUE));

        elemBucket.add(new Icosphere(2, 1).withColor(ReadableColor.RED).withTranslation(new Vector3f(3, 5, 0)));
        elemBucket.add(new Icosphere(2, 2).withColor(ReadableColor.GREEN).withTranslation(new Vector3f(0, 5, 0)));
        elemBucket.add(new Icosphere(2, 1).withColor(ReadableColor.BLUE).withTranslation(new Vector3f(-3, 5, 0)));

        elemBucket.add(new Tetrahedron(3).withColor(ReadableColor.WHITE).withTranslation(new Vector3f(-3, -5, 0)));

        elemBucket.add(new Cube(1).withColor(ReadableColor.ORANGE).withTranslation(new Vector3f(-3, -2, 0))
                .withRotation(SimpleMath.createQuaternion(new Vector3f(1, 0, -1), new Vector3f(0, 1, 0), new Quaternion())));

        elemBucket.add(new Circle(1).withTranslation(new Vector3f(3, 2, 0)));

        elemBucket.add(new Icosphere(1, 1).withColor(ReadableColor.RED).withTranslation(new Vector3f(5, -7, 0)));
        elemBucket.add(new Icosphere(1, 2).withColor(ReadableColor.GREEN).withTranslation(new Vector3f(0, -7, 0)));
        elemBucket.add(new Icosphere(1, 1).withColor(ReadableColor.BLUE).withTranslation(new Vector3f(-5, -7, 0)));


        elemBucket.add(new TexturedQuad().withTranslation(new Vector3f(-1, 5, 0)));
        elemBucket.add(new ColoredQuad().withTranslation(new Vector3f(-1, 5, 0)));

        return elemBucket;
    }

    public static Collection<IsRenderable> loadFromFile(String path) {
        HashSet<IsRenderable> elemBucket = new HashSet<IsRenderable>();
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(path);) {
            if (inputStream == null) {
                throw new GenericGameException("input stream is null for path '" + path + "'");
            }
            elemBucket.add(new ModelLoader().getRenderable(inputStream));
        } catch (IOException ex) {
            throw new GenericGameException("i/O Error while loading model from file with path '" + path + "'", ex);
        }
        return elemBucket;
    }



}

package net.wohlfart.basic.states;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.elements.ColoredQuad;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.elements.debug.Circle;
import net.wohlfart.gl.elements.debug.CubeMesh;
import net.wohlfart.gl.elements.debug.IcosphereMesh;
import net.wohlfart.gl.elements.debug.AbstractRenderableGrid;
import net.wohlfart.gl.elements.debug.TerahedronRefinedMesh;
import net.wohlfart.gl.elements.debug.TetrahedronMesh;
import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>ElementCreator class.</p>
 *
 *
 *
 */
public class ElementCreator {

    protected GraphicContextManager graphContext = GraphicContextManager.INSTANCE;


    Collection<Renderable> createCircles() {
        HashSet<Renderable> elemBucket = new HashSet<Renderable>();

        elemBucket.add(new Circle(1).lineWidth(2));
        elemBucket.add(new Circle(10).lineWidth(2));
        elemBucket.add(new Circle(20).lineWidth(2));
        elemBucket.add(new Circle(30).lineWidth(2));
        elemBucket.add(new Circle(40).lineWidth(2));
        elemBucket.add(new Circle(50).lineWidth(2));
        elemBucket.add(new Circle(60).lineWidth(2));
        elemBucket.add(new Circle(70).lineWidth(2));
        elemBucket.add(new Circle(80).lineWidth(2));
        elemBucket.add(new Circle(90).lineWidth(2));
        elemBucket.add(new Circle(100).lineWidth(2));

        return elemBucket;
    }


    Collection<Renderable> createRandomElements() {

        HashSet<Renderable> elemBucket = new HashSet<Renderable>();

        elemBucket.add(new Arrow(new Vector3f(1, 0, 0)).color(ReadableColor.RED));
        elemBucket.add(new Arrow(new Vector3f(0, 1, 0)).color(ReadableColor.GREEN));
        elemBucket.add(new Arrow(new Vector3f(0, 0, 1)).color(ReadableColor.BLUE));

        elemBucket.add(new TerahedronRefinedMesh(2, 1).lineWidth(1).color(ReadableColor.RED).translate(new Vector3f(3, 5, 0)));
        elemBucket.add(new TerahedronRefinedMesh(2, 2).lineWidth(2).color(ReadableColor.GREEN).translate(new Vector3f(0, 5, 0)));
        elemBucket.add(new TerahedronRefinedMesh(2, 1).lineWidth(2).color(ReadableColor.BLUE).translate(new Vector3f(-3, 5, 0)));

        elemBucket.add(new TetrahedronMesh(3).lineWidth(2).color(ReadableColor.WHITE).translate(new Vector3f(-3, -5, 0)));

        elemBucket.add(new CubeMesh(1).lineWidth(1).color(ReadableColor.ORANGE).translate(new Vector3f(-3, -2, 0))
                .rotate(SimpleMath.createQuaternion(new Vector3f(1, 0, -1), new Vector3f(0, 1, 0), new Quaternion())));

        elemBucket.add(new Circle(1).lineWidth(2).translate(new Vector3f(3, 2, 0)));

        elemBucket.add(new IcosphereMesh(1, 1).lineWidth(1).color(ReadableColor.RED).translate(new Vector3f(5, -7, 0)));
        elemBucket.add(new IcosphereMesh(1, 2).lineWidth(2).color(ReadableColor.GREEN).translate(new Vector3f(0, -7, 0)));
        elemBucket.add(new IcosphereMesh(1, 1).lineWidth(2).color(ReadableColor.BLUE).translate(new Vector3f(-5, -7, 0)));

        float farPlane= graphContext.getFarPlane();
        for (int i = 0; i < 10000; i++) {
            final float x = SimpleMath.random(-farPlane, farPlane);
            final float y = SimpleMath.random(-farPlane, farPlane);
            final float z = SimpleMath.random(-farPlane, farPlane);

            AbstractRenderableGrid mesh = new IcosphereMesh(1, 1).lineWidth(1).color(ReadableColor.CYAN);
            mesh.setTranslation(new Vector3f(x, y, z));
            elemBucket.add(mesh);
        }

        elemBucket.add(new TexturedQuad().translate(new Vector3f(-1, 5, 0)));
        elemBucket.add(new ColoredQuad().translate(new Vector3f(-1, 5, 0)));

        return elemBucket;
    }

}

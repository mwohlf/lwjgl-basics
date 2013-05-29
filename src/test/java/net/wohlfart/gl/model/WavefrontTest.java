package net.wohlfart.gl.model;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.antlr4.ModelLoader;

import org.junit.Test;

/**
 * <p>WavefrontTest class.</p>
 */
public class WavefrontTest {


    /**
     * <p>simpleParse.</p>
     * @throws UnsupportedEncodingException
     * @throws GenericGameException
     */
    @Test
    public void simpleParse() throws GenericGameException, UnsupportedEncodingException {
        Model model = new ModelLoader().getModel(new ByteArrayInputStream(getCube().getBytes("UTF-8")));
        // 6 faces 2 triangles each face, 3 vertices each triangle
        assertEquals(6 * 2 * 3, model.getAttrIndices().size());
        // 8 edges of a cube
        assertEquals(8, model.getPositions().size());
    }


    /**
     * <p>getCube.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getCube() {
        return ""
                + "# Blender v2.66 (sub 1) OBJ File: ''\n"
                + "# www.blender.org\n"
                + "mtllib cube2.mtl\n"
                + "o Cube\n"
                + "v 1.000000 -1.000000 -1.000000\n"
                + "v 1.000000 -1.000000 1.000000\n"
                + "v -1.000000 -1.000000 1.000000\n"
                + "v -1.000000 -1.000000 -1.000000\n"
                + "v 1.000000 1.000000 -0.999999\n"
                + "v 0.999999 1.000000 1.000001\n"
                + "v -1.000000 1.000000 1.000000\n"
                + "v -1.000000 1.000000 -1.000000\n"
                + "vt 0.332332 0.001001\n"
                + "vt 0.001001 0.001001\n"
                + "vt 0.001001 0.332332\n"
                + "vt 0.665666 0.334334\n"
                + "vt 0.334334 0.334334\n"
                + "vt 0.334334 0.665666\n"
                + "vt 0.332332 0.334334\n"
                + "vt 0.001001 0.334334\n"
                + "vt 0.001001 0.665666\n"
                + "vt 0.665666 0.001001\n"
                + "vt 0.334334 0.001001\n"
                + "vt 0.665666 0.332332\n"
                + "vt 0.998999 0.667668\n"
                + "vt 0.667668 0.667668\n"
                + "vt 0.998999 0.998999\n"
                + "vt 0.665666 0.667668\n"
                + "vt 0.334334 0.667668\n"
                + "vt 0.334334 0.998999\n"
                + "vt 0.332332 0.332332\n"
                + "vt 0.665666 0.665666\n"
                + "vt 0.332332 0.665666\n"
                + "vt 0.334334 0.332332\n"
                + "vt 0.667668 0.998999\n"
                + "vt 0.665666 0.998999\n"
                + "vn 0.000000 -1.000000 0.000000\n"
                + "vn -0.000000 1.000000 0.000000\n"
                + "vn 1.000000 -0.000000 0.000001\n"
                + "vn -0.000000 -0.000000 1.000000\n"
                + "vn -1.000000 -0.000000 -0.000000\n"
                + "vn 0.000000 0.000000 -1.000000\n"
                + "vn 1.000000 0.000000 -0.000000\n"
                + "usemtl Material\n"
                + "s off\n"
                + "f 1/1/1 2/2/1 3/3/1\n"
                + "f 5/4/2 8/5/2 7/6/2\n"
                + "f 1/7/3 5/8/3 6/9/3\n"
                + "f 2/10/4 6/11/4 3/12/4\n"
                + "f 3/13/5 7/14/5 4/15/5\n"
                + "f 5/16/6 1/17/6 4/18/6\n"
                + "f 4/19/1 1/1/1 3/3/1\n"
                + "f 6/20/2 5/4/2 7/6/2\n"
                + "f 2/21/7 1/7/7 6/9/7\n"
                + "f 6/11/4 7/22/4 3/12/4\n"
                + "f 7/14/5 8/23/5 4/15/5\n"
                + "f 8/24/6 5/16/6 4/18/6\n";
    }
}

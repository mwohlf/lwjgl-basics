package net.wohlfart.antlr4;

import static org.junit.Assert.assertEquals;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class WavefrontTest {



    @Test
    public void simpleParse() {
        ANTLRInputStream input = new ANTLRInputStream(getCube());
        WavefrontLexer lexer = new WavefrontLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WavefrontParser parser = new WavefrontParser(tokens);

        ParseTree tree = parser.wavefront();

        ParseTreeWalker walker = new ParseTreeWalker();
        WavefrontReader reader = new WavefrontReader();
        walker.walk(reader, tree);

        assertEquals(12 * 3, reader.getMeshBuilder().getIndicesCount());
        assertEquals(8, reader.getMeshBuilder().getVerticesCount());

        System.out.println(tree.toStringTree(parser)); // print LISP-style tree

    }


    protected String getCube() {
        return ""
                +"# Blender v2.66 (sub 0) OBJ File: ''" + '\n'
                +"# www.blender.org" + '\n'
                +"mtllib cube.mtl" + '\n'
                +"o Cube" + '\n'
                +"v -1.000000 -1.000000 -1.000000" + '\n'
                +"v 1.000000 -1.000000 1.000000" + '\n'
                +"v -1.000000 -1.000000 1.000000" + '\n'
                +"v -1.000000 -1.000000 -1.000000" + '\n'
                +"v 1.000000 1.000000 -0.999999" + '\n'
                +"v 0.999999 1.000000 1.000001" + '\n'
                +"v -1.000000 1.000000 1.000000" + '\n'
                +"v -1.000000 1.000000 -1.000000" + '\n'
                +"vn 0.000000 -1.000000 0.000000" + '\n'
                +"vn -0.000000 1.000000 0.000000" + '\n'
                +"vn 1.000000 -0.000000 0.000001" + '\n'
                +"vn -0.000000 -0.000000 1.000000" + '\n'
                +"vn -1.000000 -0.000000 -0.000000" + '\n'
                +"vn 0.000000 0.000000 -1.000000" + '\n'
                +"vn 1.000000 0.000000 -0.000000" + '\n'
                +"usemtl Material" + '\n'
                +"s off" + '\n'
                +"f 1//1 2//1 3//1" + '\n'
                +"f 5//2 8//2 7//2" + '\n'
                +"f 1//3 5//3 6//3" + '\n'
                +"f 2//4 6//4 3//4" + '\n'
                +"f 3//5 7//5 4//5" + '\n'
                +"f 5//6 1//6 4//6" + '\n'
                +"f 4//1 1//1 3//1" + '\n'
                +"f 6//2 5//2 7//2" + '\n'
                +"f 2//7 1//7 6//7" + '\n'
                +"f 6//4 7//4 3//4" + '\n'
                +"f 7//5 8//5 4//5" + '\n'
                +"f 8//6 5//6 4//6";
    }
}

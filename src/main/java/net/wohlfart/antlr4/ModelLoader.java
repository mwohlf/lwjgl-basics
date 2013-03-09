package net.wohlfart.antlr4;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ModelLoader {

    public GenericMeshBuilder load(InputStream input) throws IOException {
        ANTLRInputStream antlrInput = new ANTLRInputStream(input);
        WavefrontLexer lexer = new WavefrontLexer(antlrInput);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WavefrontParser parser = new WavefrontParser(tokens);
        ParseTree tree = parser.wavefront();
        WavefrontReader reader = new WavefrontReader();
        new ParseTreeWalker().walk(reader, tree);
        return reader.getMeshBuilder();
    }

}

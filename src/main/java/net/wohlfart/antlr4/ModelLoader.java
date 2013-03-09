package net.wohlfart.antlr4;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * <p>ModelLoader class.</p>
 */
public class ModelLoader {

    /**
     * <p>getBuilder.</p>
     *
     * @param input a {@link java.io.InputStream} object.
     * @return a {@link net.wohlfart.gl.shader.mesh.GenericMeshBuilder} object.
     * @throws java.io.IOException if any.
     */
    public GenericMeshBuilder getBuilder(InputStream input) throws IOException {
        final ANTLRInputStream antlrInput = new ANTLRInputStream(input);
        final WavefrontLexer lexer = new WavefrontLexer(antlrInput);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final WavefrontParser parser = new WavefrontParser(tokenStream);
        final ParseTree tree = parser.wavefront();
        final MeshBuilderProviderImpl provider = new MeshBuilderProviderImpl();
        new ParseTreeWalker().walk(provider, tree);
        return provider.getMeshBuilder();
    }

}

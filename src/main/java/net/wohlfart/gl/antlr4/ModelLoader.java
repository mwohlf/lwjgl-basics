package net.wohlfart.gl.antlr4;

import java.io.IOException;
import java.io.InputStream;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * <p>
 * ModelLoader class, the facade for this package. The idea is to provide an object description
 * through an input stream or file and get a MeshBuilder to create the objects.
 * </p>
 */
public class ModelLoader {

    /**
     * <p>Turn an input stream into a builder for creating the object defined by the input stream.</p>
     *
     * @param input a {@link java.io.InputStream} object thans contains an object defintion
     * @return a {@link net.wohlfart.gl.shader.mesh.GenericMeshBuilder} object for creating elements
     * @throws new.wohlfart.basic.GenericGameException if something goes wrong whileparsing the input stream.
     */
    public GenericMeshBuilder getBuilder(InputStream input) throws GenericGameException {
        try {
            final ANTLRInputStream antlrInput = new ANTLRInputStream(input);
            final WavefrontLexer lexer = new WavefrontLexer(antlrInput);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final WavefrontParser parser = new WavefrontParser(tokenStream);
            final ParseTree tree = parser.wavefront();
            final MeshBuilderProviderImpl provider = new MeshBuilderProviderImpl();
            new ParseTreeWalker().walk(provider, tree);
            return provider.getMeshBuilder();
        } catch (IOException ex) {
            throw new GenericGameException("exception while parsing input stream", ex);
        }
    }

}

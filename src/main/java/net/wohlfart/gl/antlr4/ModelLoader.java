package net.wohlfart.gl.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.antlr4.WavefrontParser.FaceContext;
import net.wohlfart.gl.antlr4.WavefrontParser.NormalContext;
import net.wohlfart.gl.antlr4.WavefrontParser.ObjectNameContext;
import net.wohlfart.gl.antlr4.WavefrontParser.VertNormIdxContext;
import net.wohlfart.gl.antlr4.WavefrontParser.VertexContext;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;


public class ModelLoader extends WavefrontBaseListener {

    private GenericMeshBuilder currentBuilder;


    public IsRenderable getRenderable(InputStream input) throws GenericGameException {
        try {
            final ANTLRInputStream antlrInput = new ANTLRInputStream(input);
            final WavefrontLexer lexer = new WavefrontLexer(antlrInput);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final WavefrontParser parser = new WavefrontParser(tokenStream);
            final ParseTree tree = parser.wavefront();
            new ParseTreeWalker().walk(this, tree);
            return currentBuilder.build();
        } catch (IOException ex) {
            throw new GenericGameException("exception while parsing input stream", ex);
        }
    }

    GenericMeshBuilder getMeshBuilder() {
        return currentBuilder;
    }


    @Override
    public void exitObjectName(ObjectNameContext ctx) {
        final String objectName = ctx.IDENTIFIER().getText();
        currentBuilder = new GenericMeshBuilder(objectName);
    }

    @Override
    public void exitVertex(VertexContext ctx) {
        final List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addVertex(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitNormal(NormalContext ctx) {
        final List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addNormal(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitFace(FaceContext ctx) {
        final List<VertNormIdxContext> vertNormIdx = ctx.vertNormIdx();
        for (final VertNormIdxContext n : vertNormIdx) {
            currentBuilder.addVertexIndex(Integer.parseInt(n.NATURAL(0).getText()) - 1);
        }
    }

}

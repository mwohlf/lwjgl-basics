package net.wohlfart.gl.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.wohlfart.basic.GenericGameException;
import net.wohlfart.gl.antlr4.WavefrontParser.FaceContext;
import net.wohlfart.gl.antlr4.WavefrontParser.NormalContext;
import net.wohlfart.gl.antlr4.WavefrontParser.ObjectNameContext;
import net.wohlfart.gl.antlr4.WavefrontParser.PositionContext;
import net.wohlfart.gl.antlr4.WavefrontParser.TextureCoordContext;
import net.wohlfart.gl.antlr4.WavefrontParser.VertIndicesContext;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;


public class ModelLoader extends WavefrontBaseListener {

    private Model currentModel;

    public Model getModel(InputStream input) throws GenericGameException {
        try {
            final ANTLRInputStream antlrInput = new ANTLRInputStream(input);
            final WavefrontLexer lexer = new WavefrontLexer(antlrInput);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final WavefrontParser parser = new WavefrontParser(tokenStream);
            final ParseTree tree = parser.wavefront();
            new ParseTreeWalker().walk(this, tree);
            return currentModel;
        } catch (IOException ex) {
            throw new GenericGameException("exception while parsing input stream", ex);
        }
    }

    @Override
    public void exitObjectName(ObjectNameContext ctx) {
        final String objectName = ctx.IDENTIFIER().getText();
        currentModel = new Model(objectName);
    }

    @Override
    public void exitPosition(PositionContext ctx) {
        final List<TerminalNode> coords = ctx.REAL();
        currentModel.addPosition(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitNormal(NormalContext ctx) {
        final List<TerminalNode> coords = ctx.REAL();
        currentModel.addNormal(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitTextureCoord(TextureCoordContext ctx) {
        final List<TerminalNode> coords = ctx.REAL();
        currentModel.addTextureCoord(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()));
    }

    @Override
    public void exitFace(FaceContext ctx) {
        final List<VertIndicesContext> vertNormIdx = ctx.vertIndices();
        for (final VertIndicesContext n : vertNormIdx) {
            currentModel.addVertexForStream(
                    Integer.parseInt(n.NATURAL(0).getText()) - 1,   // position
                    Integer.parseInt(n.NATURAL(1).getText()) - 1,   // textureCoords
                    Integer.parseInt(n.NATURAL(2).getText()) - 1);  // normal
        }
    }

}

package net.wohlfart.gl.antlr4;

import java.util.List;

import net.wohlfart.gl.antlr4.WavefrontParser.FaceContext;
import net.wohlfart.gl.antlr4.WavefrontParser.NormalContext;
import net.wohlfart.gl.antlr4.WavefrontParser.ObjectNameContext;
import net.wohlfart.gl.antlr4.WavefrontParser.VertNormIdxContext;
import net.wohlfart.gl.antlr4.WavefrontParser.VertexContext;
import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * <p>An implementation of a MeshBuilderProvider.</p>
 */
class MeshBuilderProviderImpl extends WavefrontBaseListener implements MeshBuilderProvider {

    private GenericMeshBuilder currentBuilder;

    /** {@inheritDoc} */
    @Override
    public GenericMeshBuilder getMeshBuilder() {
        return currentBuilder;
    }

    /** {@inheritDoc} */
    @Override
    public void exitObjectName(ObjectNameContext ctx) {
        String objectName = ctx.IDENTIFIER().getText();
        currentBuilder = new GenericMeshBuilder(objectName);
    }

    /** {@inheritDoc} */
    @Override
    public void exitVertex(VertexContext ctx) {
        List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addVertex(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    /** {@inheritDoc} */
    @Override
    public void exitNormal(NormalContext ctx) {
        List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addNormal(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    /** {@inheritDoc} */
    @Override
    public void exitFace(FaceContext ctx) {
        List<VertNormIdxContext> vertNormIdx = ctx.vertNormIdx();
        for (VertNormIdxContext n : vertNormIdx) {
            currentBuilder.addVertexIndex(Integer.parseInt(n.NATURAL(0).getText()) - 1);
        }
    }

}

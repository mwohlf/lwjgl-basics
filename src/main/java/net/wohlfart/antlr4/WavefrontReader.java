package net.wohlfart.antlr4;

import java.util.List;

import net.wohlfart.antlr4.WavefrontParser.FaceContext;
import net.wohlfart.antlr4.WavefrontParser.NormalContext;
import net.wohlfart.antlr4.WavefrontParser.ObjectNameContext;
import net.wohlfart.antlr4.WavefrontParser.VertNormIdxContext;
import net.wohlfart.antlr4.WavefrontParser.VertexContext;
import net.wohlfart.gl.shader.mesh.GenericMeshBuilder;

import org.antlr.v4.runtime.tree.TerminalNode;

public class WavefrontReader extends WavefrontBaseListener {

    private GenericMeshBuilder currentBuilder;

    @Override
    public void exitObjectName(ObjectNameContext ctx) {
        String objectName = ctx.IDENTIFIER().getText();
        currentBuilder = new GenericMeshBuilder(objectName);
    }

    @Override
    public void exitVertex(VertexContext ctx) {
        List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addVertex(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitNormal(NormalContext ctx) {
        List<TerminalNode> coords = ctx.REAL();
        currentBuilder.addNormal(
                Float.parseFloat(coords.get(0).getText()),
                Float.parseFloat(coords.get(1).getText()),
                Float.parseFloat(coords.get(2).getText()));
    }

    @Override
    public void exitFace(FaceContext ctx) {
        List<VertNormIdxContext> vertNormIdx = ctx.vertNormIdx();
        for (VertNormIdxContext n : vertNormIdx) {
            currentBuilder.addVertexIndex(Integer.parseInt(n.NATURAL(0).getText()) - 1);
        }
    }

    public GenericMeshBuilder getMeshBuilder() {
        return currentBuilder;
    }

}

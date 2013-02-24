package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.TextComponent;
import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;


/**
 * this class is responsible for switching context and rendering the hud components
 */
public class Hud implements Renderable {

    private final GraphicContextManager contextManagert = GraphicContextManager.INSTANCE;

    private IGraphicContext hudContext;
    private Avatar avatar;

    private LayerImpl layer;


    public void init(IGraphicContext hudContext, Avatar avatar) {
        this.avatar = avatar;
        this.hudContext = hudContext;
        this.layer = new LayerImpl();
        add(new Label(0, 0, "hello world at (0,0)"));
    }

    public void add(TextComponent label) {
        layer.add(label);
    }

    @Override
    public void render() {
        contextManagert.setCurrentGraphicContext(hudContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.CAM_TO_CLIP.set(contextManagert.getPerspectiveProjMatrix());
        //ShaderUniformHandle.CAM_TO_CLIP.set(contextManagert.getOrthographicProjMatrix());
        //ShaderUniformHandle.CAM_TO_CLIP.set(SimpleMath.UNION_MATRIX);
        GL11.glEnable(GL11.GL_BLEND);
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}

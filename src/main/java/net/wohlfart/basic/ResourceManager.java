package net.wohlfart.basic;

import java.net.URL;

public final class ResourceManager {

    protected String fontDirectory = "resources/fonts/";
    protected String gfxDirectory = "resources/gfx/";
    protected String modelsDirectory = "resources/models/";
    protected String shadersDirectory = "resources/shaders/";


    public void setFontDirectory(String fontDirectory) {
        this.fontDirectory = fontDirectory;
    }

    public void setGfxDirectory(String gfxDirectory) {
        this.gfxDirectory = gfxDirectory;
    }

    public URL getGfxUrl(String string) {
        URL result = getClass().getResource(gfxDirectory + string);
        if (result == null) {
            throw new IllegalArgumentException("can't find resource '" + string + "'"
                    + " in directory '" + gfxDirectory + "'");
        }
        return result;
    }

    public void setModelsDirectory(String modelsDirectory) {
        this.modelsDirectory = modelsDirectory;
    }

    public void setShadersDirectory(String shadersDirectory) {
        this.shadersDirectory = shadersDirectory;
    }

}

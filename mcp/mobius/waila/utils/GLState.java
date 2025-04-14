package mcp.mobius.waila.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GLState {

    private final boolean hasLight;
    private final boolean hasDepthTest;
    private final boolean hasLight0;
    private final boolean hasLight1;
    private final boolean hasRescaleNormal;
    private final boolean hasColorMaterial;
    private final boolean depthMask;
    private final int depthFunc;

    public GLState() {
        hasLight = GL11.glGetBoolean(GL11.GL_LIGHTING);
        hasLight0 = GL11.glGetBoolean(GL11.GL_LIGHT0);
        hasLight1 = GL11.glGetBoolean(GL11.GL_LIGHT1);
        hasDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        hasRescaleNormal = GL11.glGetBoolean(GL12.GL_RESCALE_NORMAL);
        hasColorMaterial = GL11.glGetBoolean(GL11.GL_COLOR_MATERIAL);
        depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
    }

    public void reset() {
        GL11.glPopAttrib();
        GL11.glDepthMask(depthMask);
        GL11.glDepthFunc(depthFunc);
        if (hasColorMaterial) GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        else GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        if (hasRescaleNormal) GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        else GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        if (hasDepthTest) GL11.glEnable(GL11.GL_DEPTH_TEST);
        else GL11.glDisable(GL11.GL_DEPTH_TEST);
        if (hasLight1) GL11.glEnable(GL11.GL_LIGHT1);
        else GL11.glDisable(GL11.GL_LIGHT1);
        if (hasLight0) GL11.glEnable(GL11.GL_LIGHT0);
        else GL11.glDisable(GL11.GL_LIGHT0);
        if (hasLight) GL11.glEnable(GL11.GL_LIGHTING);
        else GL11.glDisable(GL11.GL_LIGHTING);
    }

}

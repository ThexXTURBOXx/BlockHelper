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
        GL11.glPushMatrix();
    }

    public void reset() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        GL11.glDepthMask(depthMask);
        GL11.glDepthFunc(depthFunc);
        glSetBoolean(GL11.GL_COLOR_MATERIAL, hasColorMaterial);
        glSetBoolean(GL12.GL_RESCALE_NORMAL, hasRescaleNormal);
        glSetBoolean(GL11.GL_DEPTH_TEST, hasDepthTest);
        glSetBoolean(GL11.GL_LIGHT1, hasLight1);
        glSetBoolean(GL11.GL_LIGHT0, hasLight0);
        glSetBoolean(GL11.GL_LIGHTING, hasLight);
    }

    private static void glSetBoolean(int cap, boolean value) {
        if (value) GL11.glEnable(cap);
        else GL11.glDisable(cap);
    }

}

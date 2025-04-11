package mcp.mobius.waila.api;

import java.awt.Dimension;


public interface ITooltipRenderer {
    /**
     * @param params   Array of string parameters as passed to the RENDER arg in the tooltip ({rendername,param1,
     *                 param2,...})
     * @param accessor A global accessor for TileEntities and Entities
     * @return Dimension of the reserved area
     */
    Dimension getSize(String[] params, ICommonAccessor accessor);

    /**
     * Draw method for the renderer. All calls should be relative to (0,0)
     *
     * @param params   Array of string parameters as passed to the RENDER arg in the tooltip ({rendername,param1,
     *                 param2,...})
     * @param accessor A global accessor for TileEntities and Entities
     * @param x        The x offset to draw at
     * @param y        The y offset to draw at
     */
    void draw(String[] params, ICommonAccessor accessor, int x, int y);
}

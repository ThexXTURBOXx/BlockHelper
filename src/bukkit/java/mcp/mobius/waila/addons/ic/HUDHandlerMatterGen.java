package mcp.mobius.waila.addons.ic;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen;
import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen_matterCost;
import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen_matterGeneration;

public class HUDHandlerMatterGen implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMatterGen();

    private HUDHandlerMatterGen() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int matterGeneration = -1;
            int matterCost = -1;

            if (TileEntityMatterGen.isInstance(te)) {
                matterGeneration = TileEntityMatterGen_matterGeneration.getInt(te);
                matterCost = TileEntityMatterGen_matterCost.getInt(te);
            }

            tag.a("matterGeneration", matterGeneration);
            tag.a("matterCost", matterCost);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass());
        }
    }

}

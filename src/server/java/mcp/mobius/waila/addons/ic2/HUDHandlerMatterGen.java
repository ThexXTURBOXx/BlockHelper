package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityMatter;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityMatter_getProgressAsString;

public class HUDHandlerMatterGen implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMatterGen();

    private HUDHandlerMatterGen() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            String matterProgress = null;

            if (TileEntityMatter.isInstance(te)) {
                matterProgress = (String) TileEntityMatter_getProgressAsString.invoke(te);
            }

            if (matterProgress != null)
                tag.setString("matterProgress", matterProgress);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass());
        }
    }

}

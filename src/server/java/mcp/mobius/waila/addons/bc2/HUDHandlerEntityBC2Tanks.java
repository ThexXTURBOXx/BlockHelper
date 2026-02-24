package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;

public final class HUDHandlerEntityBC2Tanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityBC2Tanks();

    private HUDHandlerEntityBC2Tanks() {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            LiquidHelper.writeToNBT(ent, tag);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass());
        }
    }

}

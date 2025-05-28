package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;

public final class HUDHandlerEntityBC2Tanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityBC2Tanks();

    private HUDHandlerEntityBC2Tanks() {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT(ent, tag);
    }

}

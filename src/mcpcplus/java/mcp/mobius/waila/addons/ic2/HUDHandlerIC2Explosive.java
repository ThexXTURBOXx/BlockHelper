package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.EntityIC2Explosive;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.EntityIC2Explosive_fuse;

public class HUDHandlerIC2Explosive implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerIC2Explosive();

    private HUDHandlerIC2Explosive() {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            int fuse = 0;

            if (EntityIC2Explosive.isInstance(ent)) {
                fuse = EntityIC2Explosive_fuse.getInt(ent);
            }

            tag.setInt("Fuse", fuse);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, ent.getClass());
        }
    }

}

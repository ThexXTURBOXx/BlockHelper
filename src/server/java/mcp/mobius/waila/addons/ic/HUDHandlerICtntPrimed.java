package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;

public class HUDHandlerICtntPrimed implements IEntityProvider {

    private final Field fuse;

    public HUDHandlerICtntPrimed(Field fuse) {
        this.fuse = fuse;
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            tag.setInteger("Fuse", fuse.getInt(ent));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, ent.getClass());
        }
    }

}

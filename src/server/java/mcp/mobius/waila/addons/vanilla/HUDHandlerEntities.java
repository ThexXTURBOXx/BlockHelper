package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

public final class HUDHandlerEntities implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntities();

    private HUDHandlerEntities() {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        if (ent instanceof EntityLiving)
            tag.setInteger("MaxHealth", ((EntityLiving) ent).func_40095_c());
    }

}

package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HUDHandlerEntitiesDev implements IEntityProvider {

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {

    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (!PluginConfig.instance().get("general.dev")) return;

        if (accessor.getEntity() != null)
            currenttip.add(accessor.getEntity().getClass().getName());
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {

    }

    @Override
    public void appendServerData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
    }

    public static void register() {
        WailaRegistrar.instance().registerBodyProvider(new HUDHandlerEntitiesDev(), Block.class);
    }

}

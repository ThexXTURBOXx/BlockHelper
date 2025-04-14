package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerDev implements IDataProvider {

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!mod_BlockHelper.DEV_MODE || !PluginConfig.instance().get("general.dev")) return;

        if (accessor.getBlock() != null)
            currenttip.add(accessor.getBlock().getClass().getName());
        if (accessor.getTileEntity() != null)
            currenttip.add(accessor.getTileEntity().getClass().getName());
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
    }

}

package mcp.mobius.waila.addons.ee;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class HUDHandlerEE implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerEE();

    private HUDHandlerEE() {
    }

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
        try {
            /* EMC */
            if (config.get("ee.emc")) {
                Object registry = EEPlugin.EMCRegistry_instance.invoke(null);
                if (registry != null) {
                    Object entry = EEPlugin.EMCRegistry_getEMCValue.invoke(registry,
                            accessor.getBlockID(), accessor.getMetadata());
                    if (entry != null) {
                        currenttip.add("\u00a7eEMC:\u00a77 " + EEPlugin.EMCEntry_getCost.invoke(entry));
                    }
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
        }
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

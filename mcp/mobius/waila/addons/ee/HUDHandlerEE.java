package mcp.mobius.waila.addons.ee;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerEE implements IDataProvider {

    @Override
    public ItemStack getWailaStack(IDataAccessor accessor, IConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        try {
            /* EMC */
            if (ConfigHandler.instance().getConfig("ee.emc")) {
                Object registry = EEModule.EMCRegistry_instance.invoke(null);
                if (registry != null) {
                    Object entry = EEModule.EMCRegistry_getEMCValue.invoke(registry,
                            accessor.getBlockID(), accessor.getMetadata());
                    if (entry != null) {
                        currenttip.add("\u00a7eEMC:\u00a77 " + EEModule.EMCEntry_getCost.invoke(entry));
                    }
                }
            }
        } catch (Exception e) {
            currenttip = WailaExceptionHandler.handleErr(e, accessor.getTileEntity().getClass().getName(), currenttip);
        }

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        return tag;
    }

}

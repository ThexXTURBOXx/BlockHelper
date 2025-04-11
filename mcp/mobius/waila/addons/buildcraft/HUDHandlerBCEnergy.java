package mcp.mobius.waila.addons.buildcraft;

import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerBCEnergy implements IDataProvider {

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
        if (!config.getConfig("bcapi.storage")) return currenttip;
        if (!accessor.getNBTData().hasKey("Energy")) return currenttip;

        int energy = accessor.getNBTInteger(accessor.getNBTData(), "Energy");
        int maxEnergy = accessor.getNBTInteger(accessor.getNBTData(), "MaxStorage");
        try {
            if (maxEnergy > 0 && currenttip.getEntries("MJEnergyStorage").isEmpty()) {
                currenttip.add(String.format("%d / %d MJ", energy, maxEnergy), "MJEnergyStorage");
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
        try {
            Float energy = -1f;
            Integer maxsto = -1;
            if (BCModule.TileEngine.isInstance(te)) {
                Object engine = BCModule.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BCModule.Engine_energy.getFloat(engine);
                    maxsto = BCModule.Engine_maxEnergy.getInt(engine);
                }
            } else if (BCModule.IPowerReceptor.isInstance(te)) {
                Object prov = BCModule.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = (Float) BCModule.IPowerProvider_getEnergyStored.invoke(prov);
                    maxsto = (Integer) BCModule.IPowerProvider_getMaxEnergyStored.invoke(prov);
                }
            }

            tag.setInteger("Energy", Math.round(energy));
            tag.setInteger("MaxStorage", maxsto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tag;
    }

}

package mcp.mobius.waila.addons.buildcraft;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerBCEnergy implements IDataProvider {

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
        if (!config.get("bcapi.storage")) return;
        if (!accessor.getNBTData().hasKey("Energy")) return;

        int energy = accessor.getNBTInteger(accessor.getNBTData(), "Energy");
        int maxEnergy = accessor.getNBTInteger(accessor.getNBTData(), "MaxStorage");
        try {
            if (maxEnergy > 0 && currenttip.getEntries("MJEnergyStorage").isEmpty()) {
                currenttip.add(String.format("%d / %d MJ", energy, maxEnergy), "MJEnergyStorage");
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

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return tag;
    }

}

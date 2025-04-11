package mcp.mobius.waila.addons.thermalexpansion;

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
import net.minecraftforge.common.ForgeDirection;

public class HUDHandlerIEnergyHandler implements IDataProvider {

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

        if (!config.get("thermalexpansion.energyhandler")) return;
        if (!accessor.getNBTData().hasKey("Energy")) return;

        int energy = accessor.getNBTInteger(accessor.getNBTData(), "Energy");
        int maxEnergy = accessor.getNBTInteger(accessor.getNBTData(), "MaxStorage");
        try {
            if ((maxEnergy != 0) && currenttip.getEntries("RFEnergyStorage").isEmpty()) {
                currenttip.add(String.format("%d / %d RF", energy, maxEnergy), "RFEnergyStorage");
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
            Integer energy = -1;
            Integer maxsto = -1;
            if (ThermalExpansionModule.IEnergyInfo.isInstance(te)) {
                energy = (Integer) ThermalExpansionModule.IEnergyInfo_getCurStorage.invoke(te);
                maxsto = (Integer) ThermalExpansionModule.IEnergyInfo_getMaxStorage.invoke(te);
            } else if (ThermalExpansionModule.IEnergyProvider.isInstance(te)) {
                energy = (Integer) ThermalExpansionModule.IEnergyProvider_getCurStorage.invoke(te,
                        ForgeDirection.UNKNOWN);
                maxsto = (Integer) ThermalExpansionModule.IEnergyProvider_getMaxStorage.invoke(te,
                        ForgeDirection.UNKNOWN);
            } else if (ThermalExpansionModule.IEnergyReceiver.isInstance(te)) {
                energy = (Integer) ThermalExpansionModule.IEnergyReceiver_getCurStorage.invoke(te,
                        ForgeDirection.UNKNOWN);
                maxsto = (Integer) ThermalExpansionModule.IEnergyReceiver_getMaxStorage.invoke(te,
                        ForgeDirection.UNKNOWN);
            }

            tag.setInteger("Energy", energy);
            tag.setInteger("MaxStorage", maxsto);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return tag;
    }

}

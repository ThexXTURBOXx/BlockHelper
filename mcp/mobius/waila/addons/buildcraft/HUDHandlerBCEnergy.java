package mcp.mobius.waila.addons.buildcraft;

import java.util.List;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerBCEnergy implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		if (!config.getConfig("bcapi.storage")) return currenttip;
		if (!accessor.getNBTData().hasKey("Energy")) return currenttip;

		int energy    = accessor.getNBTInteger(accessor.getNBTData(), "Energy");
		int maxEnergy = accessor.getNBTInteger(accessor.getNBTData(), "MaxStorage");
		try {
			if ((maxEnergy != 0) && (((ITaggedList)currenttip).getEntries("MJEnergyStorage").size() == 0)){
				((ITaggedList)currenttip).add(String.format("%d / %d MJ", energy, maxEnergy), "MJEnergyStorage");
			}
		} catch (Exception e){
			currenttip = WailaExceptionHandler.handleErr(e, accessor.getTileEntity().getClass().getName(), currenttip);
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
		try{
			Float energy = -1f;
			Integer maxsto = -1;
			if (BCModule.IPowerReceptor.isInstance(te)){
				Object prov = BCModule.IPowerReceptor_getPowerProvider.invoke(te);
				if (prov != null) {
					energy = (Float) BCModule.IPowerProvider_getEnergyStored.invoke(prov);
					maxsto = (Integer) BCModule.IPowerProvider_getMaxEnergyStored.invoke(prov);
				}
			}

			tag.setInteger("Energy",     Math.round(energy));
			tag.setInteger("MaxStorage", maxsto);

		} catch (Exception e){
			throw new RuntimeException(e);
		}

		return tag;
	}

}

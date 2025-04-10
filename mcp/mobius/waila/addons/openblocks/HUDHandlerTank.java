package mcp.mobius.waila.addons.openblocks;

import java.util.List;

import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerTank implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (!config.getConfig("openblocks.fluidamount")) return currenttip;

		ITankContainer handler = (ITankContainer)accessor.getTileEntity();
		if (handler == null) return currenttip;

		ILiquidTank[] tanks = handler.getTanks(ForgeDirection.UNKNOWN);
		if (tanks.length != 1) return currenttip;

		LiquidStack liquid = tanks[0].getLiquid();
		String name       = currenttip.get(0);

		try{
			name += String.format(" < %s >", LiquidHelper.getLiquidName(liquid));
		} catch (NullPointerException f){
			name += " " + LangUtil.translateG("hud.msg.empty");
		}

		currenttip.set(0, name);

		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		if (!config.getConfig("openblocks.fluidamount")) return currenttip;

		ITankContainer handler = (ITankContainer)accessor.getTileEntity();
		if (handler == null) return currenttip;

		ILiquidTank[] tanks = handler.getTanks(ForgeDirection.UNKNOWN);
		if (tanks.length != 1) return currenttip;

		if(tanks[0].getLiquid() != null)
			currenttip.add(String.format("%d / %d mB", tanks[0].getLiquid().amount, tanks[0].getCapacity()));
		else
			currenttip.add(String.format("0 / %d mB", tanks[0].getCapacity()));

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
		if (te != null)
			te.writeToNBT(tag);
		return tag;
	}

}

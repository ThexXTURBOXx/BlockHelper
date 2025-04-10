package mcp.mobius.waila.addons.railcraft;

import java.util.List;

import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerTank implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		if (!config.getConfig("railcraft.fluidamount")) return currenttip;
		try {
			ILiquidTank tank = (ILiquidTank)RailcraftModule.ITankTile_getTank.invoke(RailcraftModule.ITankTile.cast(accessor.getTileEntity()));
			if (tank == null) return currenttip;

			LiquidStack liquid = tank.getLiquid();

			String name = currenttip.get(0);

			try{
				name += String.format(" < %s >", LiquidHelper.getLiquidName(liquid));
			} catch (NullPointerException f){
				name += " " + LangUtil.translateG("hud.msg.empty");
			}

			currenttip.set(0, name);

		} catch (Exception e){
			currenttip = WailaExceptionHandler.handleErr(e, accessor.getTileEntity().getClass().getName(), currenttip);
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		if (!config.getConfig("railcraft.fluidamount")) return currenttip;

		try {
			ILiquidTank tank = (ILiquidTank)RailcraftModule.ITankTile_getTank.invoke(RailcraftModule.ITankTile.cast(accessor.getTileEntity()));
			if (tank == null) return currenttip;

			LiquidStack fluid = tank.getLiquid();
			if (fluid != null)
				currenttip.add(String.format("%d / %d mB", fluid.amount, tank.getCapacity()));
			else
				currenttip.add(String.format("0 / %d mB", tank.getCapacity()));

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
		if (te != null)
			te.writeToNBT(tag);
		return tag;
	}

}

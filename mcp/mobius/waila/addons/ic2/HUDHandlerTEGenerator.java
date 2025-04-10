package mcp.mobius.waila.addons.ic2;

import java.util.List;

import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import static mcp.mobius.waila.api.SpecialChars.*;

public class HUDHandlerTEGenerator implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		try{
			short  storage    = accessor.getNBTData().getShort("storage");
			int    production = accessor.getNBTData().getInteger("production");
			short  maxStorage = accessor.getNBTData().getShort("maxStorage");

			String storedStr  = LangUtil.translateG("hud.msg.stored");
			String outputStr  = LangUtil.translateG("hud.msg.output");

			/* EU Storage */
			if (ConfigHandler.instance().getConfig("ic2.storage")){
				if (maxStorage > 0)
					currenttip.add(String.format("%s%s\u00a7f%d\u00a7r / \u00a7f%d\u00a7r EU", storedStr, TAB + ALIGNRIGHT, Math.round(Math.min(storage,maxStorage)), maxStorage));
			}

			if (ConfigHandler.instance().getConfig("ic2.outputeu")){
				currenttip.add(String.format("%s%s\u00a7f%d\u00a7r EU/t", outputStr, TAB + ALIGNRIGHT, production));
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
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag,	World world, int x, int y, int z) {

		try{
			short storage    = -1;
			int   production = -1;
			short maxStorage = -1;

			if (IC2Module.TileBaseGenerator.isInstance(te)){
				storage    = IC2Module.TileBaseGenerator_storage.getShort(te);
				production = IC2Module.TileBaseGenerator_production.getInt(te);
				maxStorage = IC2Module.TileBaseGenerator_maxStorage.getShort(te);
			}

			tag.setShort  ("storage",    storage);
			tag.setInteger("production", production);
			tag.setShort  ("maxStorage", maxStorage);

		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return tag;
	}

}

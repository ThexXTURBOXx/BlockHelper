package mcp.mobius.waila.addons.thermaldynamics;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import net.minecraftforge.liquids.LiquidStack;

/**
 * Created by Lordmau5 on 28.02.2015.
 */
public class HUDHandlerDuct implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
        if (!config.getConfig("thermaldynamics.fluiductsFluid")) return currenttip;

        LiquidStack liquid = LiquidStack.loadLiquidStackFromNBT(accessor.getNBTData());

        String name = "";

        try{
            name += String.format(" < %s >", LiquidHelper.getLiquidName(liquid));
        } catch (NullPointerException f){
            name += " " + LangUtil.translateG("hud.msg.empty");
        }

        currenttip.add(name);
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
        if (!config.getConfig("thermaldynamics.fluiductsAmount")) return currenttip;

        int amount = 0;

        NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("Amount"))
            amount = accessor.getNBTInteger(tag, "Amount");

        currenttip.add(String.format(" %d / 1000 mB", amount));

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

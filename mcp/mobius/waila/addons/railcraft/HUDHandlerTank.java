package mcp.mobius.waila.addons.railcraft;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerTank implements IDataProvider {

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("railcraft.fluidamount")) return;
        try {
            ILiquidTank tank =
                    (ILiquidTank) RailcraftModule.ITankTile_getTank.invoke(RailcraftModule.ITankTile.cast(accessor.getTileEntity()));
            if (tank == null) return;

            LiquidStack liquid = tank.getLiquid();

            String name = currenttip.get(0);

            try {
                name += String.format(" < %s >", LiquidHelper.getLiquidName(liquid));
            } catch (NullPointerException f) {
                name += " " + LangUtil.translateG("hud.msg.empty");
            }

            currenttip.set(0, name);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("railcraft.fluidamount")) return;

        try {
            ILiquidTank tank =
                    (ILiquidTank) RailcraftModule.ITankTile_getTank.invoke(RailcraftModule.ITankTile.cast(accessor.getTileEntity()));
            if (tank == null) return;

            LiquidStack fluid = tank.getLiquid();
            if (fluid != null)
                currenttip.add(String.format("%d / %d mB", fluid.amount, tank.getCapacity()));
            else
                currenttip.add(String.format("0 / %d mB", tank.getCapacity()));

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
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }

}

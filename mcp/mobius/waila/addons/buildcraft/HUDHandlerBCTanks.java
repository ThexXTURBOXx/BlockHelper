package mcp.mobius.waila.addons.buildcraft;

import java.util.logging.Level;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerBCTanks implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        ILiquidTank tank = this.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;

        String name = currenttip.get(0);
        if (stack != null && ConfigHandler.instance().getConfig("bc.tanktype"))
            name = name + " (" + LiquidHelper.getLiquidName(stack) + ")";
        else if (stack == null && ConfigHandler.instance().getConfig("bc.tanktype"))
            name = name + " " + LangUtil.translateG("hud.msg.empty");
        currenttip.set(0, name);
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        ILiquidTank tank = this.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int liquidAmount = stack != null ? stack.amount : 0;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (ConfigHandler.instance().getConfig("bc.tankamount"))
            currenttip.add(liquidAmount + "/" + capacity + " mB");

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public ILiquidTank getTank(IWailaDataAccessor accessor) {
        ILiquidTank tank;
        try {
            tank = ((ILiquidTank[]) BCModule.TileTank_getTanks.invoke(BCModule.TileTank.cast(accessor.getTileEntity()), ForgeDirection.UNKNOWN))[0];
        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.SEVERE,
                    "[BC] Unhandled exception trying to access a tank for display !.\n" + e);
            return null;
        }
        return tank;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        return tag;
    }

}

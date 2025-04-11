package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerForgeTanks implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        ILiquidTank tank = LiquidHelper.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (capacity > 0) {
            String name = currenttip.get(0);
            if (stack != null && ConfigHandler.instance().getConfig("forge.tanktype"))
                name = name + " (" + LiquidHelper.getLiquidName(stack) + ")";
            else if (stack == null && ConfigHandler.instance().getConfig("forge.tanktype"))
                name = name + " " + LangUtil.translateG("hud.msg.empty");
            currenttip.set(0, name);
        }

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        ILiquidTank tank = LiquidHelper.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int liquidAmount = stack != null ? stack.amount : 0;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (capacity > 0 && ConfigHandler.instance().getConfig("forge.tankamount"))
            currenttip.add(liquidAmount + "/" + capacity + " mB");

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        return tag;
    }

}

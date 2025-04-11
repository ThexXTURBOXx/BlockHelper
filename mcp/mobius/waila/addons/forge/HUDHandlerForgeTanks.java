package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IDataProvider;
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

public class HUDHandlerForgeTanks implements IDataProvider {

    @Override
    public ItemStack getWailaStack(IDataAccessor accessor, IConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
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
                                                    IDataAccessor accessor, IConfigHandler config) {
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
                                                    IDataAccessor accessor, IConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        return tag;
    }

}

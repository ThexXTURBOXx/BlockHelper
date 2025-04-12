package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.LangUtil;
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
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        ILiquidTank tank = LiquidHelper.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (capacity > 0) {
            String name = currenttip.get(0);
            if (stack != null && PluginConfig.instance().get("forge.tanktype"))
                name = name + " (" + LiquidHelper.getLiquidName(stack) + ")";
            else if (stack == null && PluginConfig.instance().get("forge.tanktype"))
                name = name + " " + LangUtil.translateG("hud.msg.empty");
            currenttip.set(0, name);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        ILiquidTank tank = LiquidHelper.getTank(accessor);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int liquidAmount = stack != null ? stack.amount : 0;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (capacity > 0 && PluginConfig.instance().get("forge.tankamount"))
            currenttip.add(liquidAmount + "/" + capacity + " mB");
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
    }

}

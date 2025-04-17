package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public final class HUDHandlerForgeTanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerForgeTanks();

    private HUDHandlerForgeTanks() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        NBTTagCompound compound = accessor.getNBTData();
        LiquidStack stack = compound.hasKey("liquidstack")
                ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                : null;
        int capacity = accessor.getNBTInteger("liquidcapacity");

        if (capacity > 0 && config.get("forge.tanktype")) {
            String name = currenttip.get(0);
            name += " " + (stack == null
                    ? I18n.translate("hud.msg.empty")
                    : ("(" + LiquidHelper.getLiquidName(stack) + ")"));
            currenttip.set(0, name);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        NBTTagCompound compound = accessor.getNBTData();
        LiquidStack stack = compound.hasKey("liquidstack")
                ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                : null;
        int liquidAmount = stack != null ? stack.amount : 0;
        int capacity = accessor.getNBTInteger("liquidcapacity");

        if (capacity > 0 && config.get("forge.tankamount"))
            currenttip.add(liquidAmount + "/" + capacity + " mB");
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        ILiquidTank tank = LiquidHelper.getTank((ITankContainer) te);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (stack != null) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stack.writeToNBT(stackNBT);
            tag.setCompoundTag("liquidstack", stackNBT);
        }
        tag.setInteger("liquidcapacity", capacity);
    }

}

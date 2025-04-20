package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.LiquidHelper;
import net.minecraft.block.Block;
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
        if (config.get("forge.tanktype")) {
            LiquidStack stack = null;
            int capacity = 0;

            if (accessor.getTileEntity() instanceof ITankContainer) {
                NBTTagCompound compound = accessor.getNBTData();
                stack = compound.hasKey("liquidstack")
                        ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                        : null;
                capacity = accessor.getNBTInteger("liquidcapacity");
            } else if (accessor.getBlock() == Block.cauldron) {
                int meta = accessor.getMetadata();
                stack = meta == 0 ? null : new LiquidStack(Block.waterStill, Math.min(4, meta) * 250);
                capacity = 1000;
            }

            if (capacity > 0) {
                String name = currenttip.get(0);
                name += " " + (stack == null
                        ? I18n.translate("hud.msg.empty")
                        : ("(" + LiquidHelper.getLiquidName(stack) + ")"));
                currenttip.set(0, name);
            }
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (config.get("forge.tankamount")) {
            int liquidAmount = 0;
            int capacity = 0;

            if (accessor.getTileEntity() instanceof ITankContainer) {
                NBTTagCompound compound = accessor.getNBTData();
                LiquidStack stack = compound.hasKey("liquidstack")
                        ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                        : null;
                liquidAmount = stack != null ? stack.amount : 0;
                capacity = accessor.getNBTInteger("liquidcapacity");
            } else if (accessor.getBlock() == Block.cauldron) {
                liquidAmount = Math.min(4, accessor.getMetadata()) * 250;
                capacity = 1000;
            }

            if (capacity > 0)
                currenttip.add(liquidAmount + "/" + capacity + " mB");
        }
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

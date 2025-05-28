package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_amount;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_init;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_loadLiquidStackFromNBT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerBC3Tanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC3Tanks();

    private HUDHandlerBC3Tanks() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tanktype")) {
                Object stack = null;
                int capacity = 0;

                if (ITankContainer.isInstance(accessor.getTileEntity())) {
                    NBTTagCompound compound = accessor.getNBTData();
                    stack = compound.hasKey("liquidstack")
                            ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                            : null;
                    capacity = accessor.getNBTInteger("liquidcapacity");
                } else if (accessor.getBlock() == Block.cauldron) {
                    int meta = accessor.getMetadata();
                    stack = meta == 0 ? null : LiquidStack_init.newInstance(Block.waterStill, Math.min(4, meta) * 250);
                    capacity = 1000;
                }

                if (capacity > 0) {
                    String name = currenttip.get(0);
                    name += " " + (stack == null
                            ? I18n.translate("hud.msg.empty")
                            : ("(" + LiquidHelper.getLiquidName(stack) + RESET + WHITE + ")"));
                    currenttip.set(0, name);
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tankamount")) {
                int liquidAmount = 0;
                int capacity = 0;

                if (ITankContainer.isInstance(accessor.getTileEntity())) {
                    NBTTagCompound compound = accessor.getNBTData();
                    Object stack = compound.hasKey("liquidstack")
                            ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                            : null;
                    liquidAmount = stack != null ? LiquidStack_amount.getInt(stack) : 0;
                    capacity = accessor.getNBTInteger("liquidcapacity");
                } else if (accessor.getBlock() == Block.cauldron) {
                    liquidAmount = Math.min(4, accessor.getMetadata()) * 250;
                    capacity = 1000;
                }

                if (capacity > 0)
                    currenttip.add(liquidAmount + "/" + capacity + " mB");
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT(te, tag);
    }

}

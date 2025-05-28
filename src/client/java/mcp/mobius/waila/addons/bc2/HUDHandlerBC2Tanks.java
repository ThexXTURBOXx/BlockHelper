package mcp.mobius.waila.addons.bc2;

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

import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerBC2Tanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC2Tanks();

    private HUDHandlerBC2Tanks() {
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
                int liquidId = 0;
                int capacity = 0;

                if (ILiquidContainer.isInstance(accessor.getTileEntity())) {
                    liquidId = accessor.getNBTInteger("liquidtype");
                    capacity = accessor.getNBTInteger("liquidcapacity");
                } else if (accessor.getBlock() == Block.cauldron) {
                    liquidId = Block.waterStill.blockID;
                    capacity = 1000;
                }

                if (capacity > 0) {
                    String name = currenttip.get(0);
                    name += " " + (liquidId == 0
                            ? I18n.translate("hud.msg.empty")
                            : ("(" + LiquidHelper.getLiquidName(liquidId) + RESET + WHITE + ")"));
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

                if (ILiquidContainer.isInstance(accessor.getTileEntity())) {
                    liquidAmount = accessor.getNBTInteger("liquidamt");
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

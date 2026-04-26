package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.addons.bc2.LiquidHelper.LiquidData;
import mcp.mobius.waila.addons.core.HUDHandlerBlocks;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

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
            if (config.get("bc.tanktype") && !config.get("bcapi.liquidbars")) {
                LiquidData data = LiquidHelper.getLiquidData(accessor, config);

                if (data.getCapacity() > 0) {
                    String name = currenttip.getFirstEntry(HUDHandlerBlocks.BLOCK_NAME_TAG);
                    name += " " + (data.getId() == TTRenderLiquidBar.EMPTY_LIQUID
                            ? I18n.translate("hud.msg.empty")
                            : ("(" + LiquidHelper.findLiquidName(data) + RESET + WHITE + ")"));
                    currenttip.replaceFirstTagEntry(name, HUDHandlerBlocks.BLOCK_NAME_TAG);
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
                LiquidData data = LiquidHelper.getLiquidData(accessor, config);
                String tip = LiquidHelper.getLiquidTooltip(data, config.get("bcapi.liquidbars"));
                if (tip != null) currenttip.add(tip);
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
        try {
            LiquidHelper.writeToNBT(te, tag);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

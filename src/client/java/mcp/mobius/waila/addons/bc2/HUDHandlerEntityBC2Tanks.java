package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.addons.bc2.LiquidHelper.LiquidData;
import mcp.mobius.waila.addons.core.HUDHandlerEntities;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntityBC2Tanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityBC2Tanks();

    private HUDHandlerEntityBC2Tanks() {
    }

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tanktype") && !config.get("bcapi.liquidbars")) {
                LiquidData data = LiquidHelper.getLiquidData(accessor, config);

                if (data.getCapacity() > 0) {
                    String name = currenttip.getFirstEntry(HUDHandlerEntities.ENTITY_NAME_TAG);
                    name += " " + (data.getId() == TTRenderLiquidBar.EMPTY_LIQUID
                            ? I18n.translate("hud.msg.empty")
                            : ("(" + LiquidHelper.findLiquidName(data) + RESET + WHITE + ")"));
                    currenttip.replaceFirstTagEntry(name, HUDHandlerEntities.ENTITY_NAME_TAG);
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tankamount")) {
                LiquidData data = LiquidHelper.getLiquidData(accessor, config);
                String tip = LiquidHelper.getLiquidTooltip(data, config.get("bcapi.liquidbars"));
                if (tip != null) currenttip.add(tip);
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            LiquidHelper.writeToNBT(ent, tag);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), null);
        }
    }

}

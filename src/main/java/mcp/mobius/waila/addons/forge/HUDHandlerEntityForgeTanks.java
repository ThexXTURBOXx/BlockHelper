package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.addons.core.HUDHandlerEntities;
import mcp.mobius.waila.api.LiquidData;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.Replacer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntityForgeTanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityForgeTanks();

    private HUDHandlerEntityForgeTanks() {
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
        if (config.get("forge.tanktype") && !config.get("forge.liquidbars")) {
            for (LiquidData data : LiquidHelper.getLiquidData(accessor, config)) {
                if (data != null && data.getCapacity() > 0) {
                    LiquidStack stack = data.getLiquidStack();
                    currenttip.replaceFirstTagEntry(new Replacer.Appender(" " + (stack == null
                                    ? I18n.translate("hud.msg.empty")
                                    : ("(" + DisplayUtil.itemDisplayNameShort(stack.asItemStack()) +
                                       RESET + WHITE + ")"))),
                            HUDHandlerEntities.ENTITY_NAME_TAG);
                    break;
                }
            }
        }
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("forge.tankamount")) {
            for (LiquidData data : LiquidHelper.getLiquidData(accessor, config)) {
                String tip = LiquidHelper.getLiquidTooltip(data, config.get("forge.liquidbars"));
                if (tip != null) currenttip.add(tip);
            }
        }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT((ITankContainer) ent, tag);
    }

}

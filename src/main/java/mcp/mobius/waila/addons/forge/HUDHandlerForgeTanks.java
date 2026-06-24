package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.addons.core.HUDHandlerBlocks;
import mcp.mobius.waila.api.LiquidData;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.Replacer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

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
        if (config.get("forge.tanktype") && !config.get("forge.liquidbars")) {
            for (LiquidData data : LiquidHelper.getLiquidData(accessor, config)) {
                if (data != null && data.getCapacity() > 0) {
                    LiquidStack stack = data.getLiquidStack();
                    currenttip.replaceFirstTagEntry(new Replacer.Appender(" " + (stack == null
                                    ? I18n.translate("hud.msg.empty")
                                    : ("(" + DisplayUtil.itemDisplayNameShort(stack.asItemStack()) +
                                       RESET + WHITE + ")"))),
                            HUDHandlerBlocks.BLOCK_NAME_TAG);
                    break;
                }
            }
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (config.get("forge.tankamount")) {
            for (LiquidData data : LiquidHelper.getLiquidData(accessor, config)) {
                String tip = LiquidHelper.getLiquidTooltip(data, config.get("forge.liquidbars"));
                if (tip != null) currenttip.add(tip);
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT((ITankContainer) te, tag);
    }

}

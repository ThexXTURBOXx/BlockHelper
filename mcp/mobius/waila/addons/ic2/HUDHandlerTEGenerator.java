package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerTEGenerator implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerTEGenerator();

    private HUDHandlerTEGenerator() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            short storage = accessor.getNBTData().getShort("storage");
            int production = accessor.getNBTData().getInteger("production");
            short maxStorage = accessor.getNBTData().getShort("maxStorage");

            String storedStr = I18n.translate("hud.msg.stored");
            String outputStr = I18n.translate("hud.msg.output");

            /* EU Storage */
            if (config.get("ic2.storage")) {
                if (maxStorage > 0)
                    currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                                   RESET + " / " + WHITE + maxStorage + RESET + " EU");
            }

            if (config.get("ic2.outputeu")) {
                currenttip.add(outputStr + TAB + ALIGNRIGHT + WHITE + production + RESET + " EU/t");
            }

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
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
            short storage = -1;
            int production = -1;
            short maxStorage = -1;

            if (IC2Plugin.TileBaseGenerator.isInstance(te)) {
                storage = IC2Plugin.TileBaseGenerator_storage.getShort(te);
                production = IC2Plugin.TileBaseGenerator_production.getInt(te);
                maxStorage = IC2Plugin.TileBaseGenerator_maxStorage.getShort(te);
            }

            tag.setShort("storage", storage);
            tag.setInteger("production", production);
            tag.setShort("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}

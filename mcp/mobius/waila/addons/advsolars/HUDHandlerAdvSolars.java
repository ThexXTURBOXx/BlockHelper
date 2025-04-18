package mcp.mobius.waila.addons.advsolars;

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

public final class HUDHandlerAdvSolars implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvSolars();

    private HUDHandlerAdvSolars() {
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
            int storage = accessor.getNBTData().getInteger("storage");
            int maxStorage = accessor.getNBTData().getInteger("maxStorage");

            String storedStr = I18n.translate("hud.msg.stored");

            /* EU Storage */
            if (config.get("advsolars.storage")) {
                if (maxStorage > 0)
                    currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                                   RESET + " / " + WHITE + maxStorage + RESET + " EU");
            }

            int production = accessor.getNBTData().getInteger("production");
            int maxPacketSize = accessor.getNBTData().getInteger("maxPacketSize");

            String prodStr = I18n.translate("hud.msg.production");

            /* QGenerator Production */
            if (config.get("advsolars.qproduction")) {
                if (production > 0)
                    currenttip.add(prodStr + TAB + ALIGNRIGHT + WHITE + production + RESET + " EU/t");
                if (maxPacketSize > 0)
                    currenttip.add(WHITE + maxPacketSize + RESET + " EU/packet");
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
            int storage = -1;
            int maxStorage = -1;

            if (AdvSolarsPlugin.TileEntitySolarPanel.isInstance(te)) {
                storage = AdvSolarsPlugin.TileEntitySolarPanel_storage.getInt(te);
                maxStorage = AdvSolarsPlugin.TileEntitySolarPanel_maxStorage.getInt(te);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        try {
            int production = -1;
            int maxPacketSize = -1;

            if (AdvSolarsPlugin.TileEntityQGenerator.isInstance(te)) {
                production = AdvSolarsPlugin.TileEntityQGenerator_production.getInt(te);
                maxPacketSize = AdvSolarsPlugin.TileEntityQGenerator_maxPacketSize.getInt(te);
            }

            tag.setInteger("production", production);
            tag.setInteger("maxPacketSize", maxPacketSize);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}

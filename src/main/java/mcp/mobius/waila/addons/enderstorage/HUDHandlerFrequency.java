package mcp.mobius.waila.addons.enderstorage;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin.GetColourFromFreq;
import static mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin.TileEnderChest_freq;

public final class HUDHandlerFrequency implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFrequency();

    private HUDHandlerFrequency() {
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
        if (config.get("enderstorage.colors")) {
            try {
                int freq = TileEnderChest_freq.getInt(accessor.getTileEntity());
                int freqLeft = (Integer) GetColourFromFreq.invoke(null, freq, 0);
                int freqCenter = (Integer) GetColourFromFreq.invoke(null, freq, 1);
                int freqRight = (Integer) GetColourFromFreq.invoke(null, freq, 2);

                currenttip.add(I18n.color(BlockCloth.getBlockFromDye(freqLeft)) + "/" +
                               I18n.color(BlockCloth.getBlockFromDye(freqCenter)) + "/" +
                               I18n.color(BlockCloth.getBlockFromDye(freqRight)));
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
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
    }

}

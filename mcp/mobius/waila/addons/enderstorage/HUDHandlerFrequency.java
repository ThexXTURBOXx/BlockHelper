package mcp.mobius.waila.addons.enderstorage;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.BlockCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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

                int freq = EnderStoragePlugin.TileFrequencyOwner_Freq.getInt(accessor.getTileEntity());
                int freqLeft = (Integer) EnderStoragePlugin.GetColourFromFreq.invoke(null, freq, 0);
                int freqCenter = (Integer) EnderStoragePlugin.GetColourFromFreq.invoke(null, freq, 1);
                int freqRight = (Integer) EnderStoragePlugin.GetColourFromFreq.invoke(null, freq, 2);

                if (!EnderStoragePlugin.TileEnderTank.isInstance(accessor.getTileEntity()))
                    currenttip.add(I18n.color(BlockCloth.getBlockFromDye(freqLeft)) + "/" +
                                   I18n.color(BlockCloth.getBlockFromDye(freqCenter)) + "/" +
                                   I18n.color(BlockCloth.getBlockFromDye(freqRight)));
                else
                    currenttip.add(I18n.color(BlockCloth.getBlockFromDye(freqRight)) + "/" +
                                   I18n.color(BlockCloth.getBlockFromDye(freqCenter)) + "/" +
                                   I18n.color(BlockCloth.getBlockFromDye(freqLeft)));

            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
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
        if (te != null)
            te.writeToNBT(tag);
    }

}

package mcp.mobius.waila.addons.enderstorage;

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

public final class HUDHandlerFrequency implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFrequency();

    private HUDHandlerFrequency() {
    }

    private static final String[] colors = {
            I18n.translate("hud.msg.white"),
            I18n.translate("hud.msg.orange"),
            I18n.translate("hud.msg.magenta"),
            I18n.translate("hud.msg.lblue"),
            I18n.translate("hud.msg.yellow"),
            I18n.translate("hud.msg.lime"),
            I18n.translate("hud.msg.pink"),
            I18n.translate("hud.msg.gray"),
            I18n.translate("hud.msg.lgray"),
            I18n.translate("hud.msg.cyan"),
            I18n.translate("hud.msg.purple"),
            I18n.translate("hud.msg.blue"),
            I18n.translate("hud.msg.brown"),
            I18n.translate("hud.msg.green"),
            I18n.translate("hud.msg.red"),
            I18n.translate("hud.msg.black")
    };

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
                    currenttip.add(colors[freqLeft] + "/" + colors[freqCenter] + "/" + colors[freqRight]);
                else
                    currenttip.add(colors[freqRight] + "/" + colors[freqCenter] + "/" + colors[freqLeft]);


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

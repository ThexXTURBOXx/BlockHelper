package mcp.mobius.waila.addons.enderstorage;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.LangUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class HUDHandlerStorage implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerStorage();

    private HUDHandlerStorage() {
    }

    private static final String[] colors = {
            LangUtil.translateG("hud.msg.white"),
            LangUtil.translateG("hud.msg.orange"),
            LangUtil.translateG("hud.msg.magenta"),
            LangUtil.translateG("hud.msg.lblue"),
            LangUtil.translateG("hud.msg.yellow"),
            LangUtil.translateG("hud.msg.lime"),
            LangUtil.translateG("hud.msg.pink"),
            LangUtil.translateG("hud.msg.gray"),
            LangUtil.translateG("hud.msg.lgray"),
            LangUtil.translateG("hud.msg.cyan"),
            LangUtil.translateG("hud.msg.purple"),
            LangUtil.translateG("hud.msg.blue"),
            LangUtil.translateG("hud.msg.brown"),
            LangUtil.translateG("hud.msg.green"),
            LangUtil.translateG("hud.msg.red"),
            LangUtil.translateG("hud.msg.black")
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
                    currenttip.add(String.format("%s/%s/%s", colors[freqLeft], colors[freqCenter], colors[freqRight]));
                else
                    currenttip.add(String.format("%s/%s/%s", colors[freqRight], colors[freqCenter], colors[freqLeft]));


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
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
    }

}

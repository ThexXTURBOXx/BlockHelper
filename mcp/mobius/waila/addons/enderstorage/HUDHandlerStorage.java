package mcp.mobius.waila.addons.enderstorage;

import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerStorage implements IDataProvider {

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
    public ItemStack getWailaStack(IDataAccessor accessor, IConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        if (config.getConfig("enderstorage.colors")) {
            try {

                int freq = EnderStorageModule.TileFrequencyOwner_Freq.getInt(accessor.getTileEntity());
                int freqLeft = (Integer) EnderStorageModule.GetColourFromFreq.invoke(null, freq, 0);
                int freqCenter = (Integer) EnderStorageModule.GetColourFromFreq.invoke(null, freq, 1);
                int freqRight = (Integer) EnderStorageModule.GetColourFromFreq.invoke(null, freq, 2);

                if (!EnderStorageModule.TileEnderTank.isInstance(accessor.getTileEntity()))
                    currenttip.add(String.format("%s/%s/%s", colors[freqLeft], colors[freqCenter], colors[freqRight]));
                else
                    currenttip.add(String.format("%s/%s/%s", colors[freqRight], colors[freqCenter], colors[freqLeft]));


            } catch (Exception e) {
                currenttip = WailaExceptionHandler.handleErr(e, accessor.getTileEntity().getClass().getName(),
                        currenttip);
            }
        }


        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }

}

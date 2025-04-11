package mcp.mobius.waila.addons.appeng;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.TAB;

public class HUDHandlerTEGenerator implements IDataProvider {

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
        try {
            int storage = accessor.getNBTData().getInteger("storage");
            int maxStorage = accessor.getNBTData().getInteger("maxStorage");

            String storedStr = LangUtil.translateG("hud.msg.stored");

            /* EU Storage */
            if (ConfigHandler.instance().getConfig("appeng.storage")) {
                if (maxStorage > 0)
                    currenttip.add(String.format("%s%s\u00a7f%d\u00a7r / \u00a7f%d\u00a7r AE", storedStr,
                            TAB + ALIGNRIGHT, Math.min(storage, maxStorage), maxStorage));
            }
        } catch (Throwable t) {
            currenttip = WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
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
        try {
            float storage = -1;
            float maxStorage = -1;

            if (AppEngModule.IMEPowerStorage.isInstance(te)) {
                storage = (float) (double) (Double) AppEngModule.IMEPowerStorage_currentPower.invoke(te);
                maxStorage = (float) (double) (Double) AppEngModule.IMEPowerStorage_maxPower.invoke(te);
            }

            tag.setInteger("storage", Math.round(storage));
            tag.setInteger("maxStorage", Math.round(maxStorage));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return tag;
    }

}

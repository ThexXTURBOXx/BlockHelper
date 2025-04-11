package mcp.mobius.waila.addons.advsolars;

import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.ITaggedList;
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
            if (ConfigHandler.instance().getConfig("advsolars.storage")) {
                if (maxStorage > 0)
                    currenttip.add(String.format("%s%s\u00a7f%d\u00a7r / \u00a7f%d\u00a7r EU", storedStr,
                            TAB + ALIGNRIGHT, Math.min(storage, maxStorage), maxStorage));
            }

            int production = accessor.getNBTData().getInteger("production");
            int maxPacketSize = accessor.getNBTData().getInteger("maxPacketSize");

            String prodStr = LangUtil.translateG("hud.msg.production");
            String maxPacketSizeStr = LangUtil.translateG("hud.msg.maxPacketSize");

            /* QGenerator Production */
            if (ConfigHandler.instance().getConfig("advsolars.qproduction")) {
                if (production > 0)
                    currenttip.add(String.format("%s%s\u00a7f%d\u00a7r EU/t",
                            prodStr, TAB + ALIGNRIGHT, production));
                if (maxPacketSize > 0)
                    currenttip.add(String.format("\u00a7f%d\u00a7r EU/packet", maxPacketSize));
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
            int storage = -1;
            int maxStorage = -1;

            if (AdvSolarsModule.TileEntitySolarPanel.isInstance(te)) {
                storage = AdvSolarsModule.TileEntitySolarPanel_storage.getInt(te);
                maxStorage = AdvSolarsModule.TileEntitySolarPanel_maxStorage.getInt(te);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        try {
            int production = -1;
            int maxPacketSize = -1;

            if (AdvSolarsModule.TileEntityQGenerator.isInstance(te)) {
                production = AdvSolarsModule.TileEntityQGenerator_production.getInt(te);
                maxPacketSize = AdvSolarsModule.TileEntityQGenerator_maxPacketSize.getInt(te);
            }

            tag.setInteger("production", production);
            tag.setInteger("maxPacketSize", maxPacketSize);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return tag;
    }

}

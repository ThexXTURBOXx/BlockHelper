package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.LangUtil;
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

            String storedStr = LangUtil.translateG("hud.msg.stored");
            String outputStr = LangUtil.translateG("hud.msg.output");

            /* EU Storage */
            if (PluginConfig.instance().get("ic2.storage")) {
                if (maxStorage > 0)
                    currenttip.add(String.format("%s%s\u00a7f%d\u00a7r / \u00a7f%d\u00a7r EU", storedStr,
                            TAB + ALIGNRIGHT, Math.min(storage, maxStorage), maxStorage));
            }

            if (PluginConfig.instance().get("ic2.outputeu")) {
                currenttip.add(String.format("%s%s\u00a7f%d\u00a7r EU/t", outputStr, TAB + ALIGNRIGHT, production));
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
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
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

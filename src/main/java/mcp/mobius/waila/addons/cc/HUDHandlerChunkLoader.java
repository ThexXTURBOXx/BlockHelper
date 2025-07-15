package mcp.mobius.waila.addons.cc;

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

import static mcp.mobius.waila.addons.cc.ChickenChunksPlugin.TileChunkLoader;
import static mcp.mobius.waila.addons.cc.ChickenChunksPlugin.TileChunkLoaderBase_active;
import static mcp.mobius.waila.addons.cc.ChickenChunksPlugin.TileChunkLoader_shape;

public final class HUDHandlerChunkLoader implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerChunkLoader();

    private HUDHandlerChunkLoader() {
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
        TileEntity te = accessor.getTileEntity();

        if (config.get("cc.owner"))
            currenttip.add(I18n.translate("hud.msg.owner") + ": " +
                           accessor.getNBTData().getString("owner"));

        if (config.get("cc.active")) {
            String on = accessor.getNBTData().getBoolean("active")
                    ? I18n.translate("hud.msg.on")
                    : I18n.translate("hud.msg.off");
            currenttip.add(I18n.translate("hud.msg.state") + ": " + on);
        }

        if (config.get("cc.radius"))
            try {
                if (TileChunkLoader.isInstance(te)) {
                    int radius = accessor.getNBTInteger("radius");
                    currenttip.add(I18n.translate("hud.msg.radius") + ": " + radius + " " +
                                   (radius == 1
                                           ? I18n.translate("hud.msg.chunk")
                                           : I18n.translate("hud.msg.chunks")));
                }
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
            }

        if (config.get("cc.shape"))
            try {
                if (TileChunkLoader.isInstance(te))
                    currenttip.add(I18n.translate("hud.msg.shape") + ": " +
                                   accessor.getNBTData().getString("shapeStr"));
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
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
            tag.setBoolean("active", TileChunkLoaderBase_active.getBoolean(te));

            if (TileChunkLoader.isInstance(te))
                tag.setString("shapeStr", TileChunkLoader_shape.get(te).toString());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

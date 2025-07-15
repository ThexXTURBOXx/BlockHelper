package mcp.mobius.waila.addons.meteors;

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

public final class HUDHandlerShield implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerShield();

    private HUDHandlerShield() {
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
        if (config.get("fm.radius"))
            try {
                int meta = accessor.getMetadata();
                if (meta == 0) {
                    currenttip.add(I18n.translate("hud.msg.state") + ": " +
                                   I18n.translate("hud.msg.charging"));
                } else {
                    int srm = 4;
                    String size = (meta * srm) + "x" + (meta * srm);
                    currenttip.add(I18n.translate("hud.msg.radius") + ": " +
                                   size + " " + I18n.translate("hud.msg.chunks"));
                }
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
    }

}

package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityMatter;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityMatter_getProgressAsString;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerMatterGen implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMatterGen();

    private HUDHandlerMatterGen() {
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
        if (config.get("ic2.mattergen"))
            try {
                String matterProgress = accessor.getNBTData().getString("matterProgress");

                String progressStr = I18n.translate("hud.msg.progress");

                if (matterProgress != null && !matterProgress.isEmpty()) {
                    currenttip.add(progressStr + TAB + ALIGNRIGHT + WHITE + matterProgress);
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
        try {
            String matterProgress = null;

            if (TileEntityMatter.isInstance(te)) {
                matterProgress = (String) TileEntityMatter_getProgressAsString.invoke(te);
            }

            if (matterProgress != null)
                tag.setString("matterProgress", matterProgress);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass(), null);
        }
    }

}

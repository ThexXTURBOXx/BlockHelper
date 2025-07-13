package mcp.mobius.waila.addons.ic;

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

import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen;
import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen_matterCost;
import static mcp.mobius.waila.addons.ic.ICPlugin.TileEntityMatterGen_matterGeneration;
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
        if (config.get("ic.mattergen"))
            try {
                int matterGeneration = accessor.getNBTInteger("matterGeneration");
                int matterCost = accessor.getNBTInteger("matterCost");

                String progressStr = I18n.translate("hud.msg.progress");

                if (matterCost > 0) {
                    int p = (int) (100f * matterGeneration / matterCost);
                    currenttip.add(progressStr + TAB + ALIGNRIGHT + WHITE + Math.min(p, 100) + "%");
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
            int matterGeneration = -1;
            int matterCost = -1;

            if (TileEntityMatterGen.isInstance(te)) {
                matterGeneration = TileEntityMatterGen_matterGeneration.getInt(te);
                matterCost = TileEntityMatterGen_matterCost.getInt(te);
            }

            tag.setInteger("matterGeneration", matterGeneration);
            tag.setInteger("matterCost", matterCost);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass(), null);
        }
    }

}

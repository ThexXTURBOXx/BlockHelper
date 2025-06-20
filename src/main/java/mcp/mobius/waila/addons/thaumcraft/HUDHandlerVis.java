package mcp.mobius.waila.addons.thaumcraft;

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

import static mcp.mobius.waila.addons.thaumcraft.ThaumcraftPlugin.TileCrystalCapacitor;
import static mcp.mobius.waila.addons.thaumcraft.ThaumcraftPlugin.TileCrystalCapacitor_maxVis;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.TAB;

public final class HUDHandlerVis implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerVis();

    private HUDHandlerVis() {
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
        if (config.get("thaumcraft.storedvis")) {
            if (TileCrystalCapacitor.isInstance(accessor.getTileEntity())) {
                try {
                    currenttip.add(I18n.translate("hud.msg.stored") + TAB + ALIGNRIGHT +
                                   accessor.getNBTInteger("storedVis") + "/" +
                                   accessor.getNBTInteger("maxVis") + " vis", "vis");
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
                }
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
        if (TileCrystalCapacitor.isInstance(accessor.getTileEntity()))
            try {
                short maxVis = TileCrystalCapacitor_maxVis.getShort(accessor.getTileEntity());
                tag.setShort("maxVis", maxVis);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
            }
    }

}

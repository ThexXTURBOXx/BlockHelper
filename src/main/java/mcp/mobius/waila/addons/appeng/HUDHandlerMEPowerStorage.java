package mcp.mobius.waila.addons.appeng;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderEnergyBar;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.NumberFormatter;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerMEPowerStorage implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMEPowerStorage();

    private HUDHandlerMEPowerStorage() {
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
        if (!config.get("appeng.storage")) return;
        if (!accessor.getNBTData().hasKey("AEMaxStorage")) return;
        if (!accessor.getNBTData().hasKey("AEStorage")) return;

        int maxEnergy = accessor.getNBTInteger("AEMaxStorage");
        int energy = Math.min(accessor.getNBTInteger("AEStorage"), maxEnergy);
        try {
            if (maxEnergy > 0 && currenttip.getEntries("AEEnergyStorage").isEmpty()) {
                if (config.get("appeng.energybars")) {
                    currenttip.add(TTRenderEnergyBar.createAE(energy, maxEnergy), "AEEnergyStorage");
                } else {
                    currenttip.add(I18n.translate("hud.msg.stored") + TAB + ALIGNRIGHT +
                                   WHITE + NumberFormatter.format(energy) + RESET + " / " +
                                   WHITE + NumberFormatter.format(maxEnergy) + RESET + " AE",
                            "AEEnergyStorage");
                }
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
            float energy = -1;
            float maxEnergy = -1;

            if (AppEngPlugin.IMEPowerStorage.isInstance(te)) {
                energy = (float) (double) (Double) AppEngPlugin.IMEPowerStorage_currentPower.invoke(te);
                maxEnergy = (float) (double) (Double) AppEngPlugin.IMEPowerStorage_maxPower.invoke(te);
            }

            tag.setInteger("AEStorage", Math.round(energy));
            tag.setInteger("AEMaxStorage", Math.round(maxEnergy));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

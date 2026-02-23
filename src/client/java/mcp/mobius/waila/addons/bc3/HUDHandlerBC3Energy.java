package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderEnergyBar;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.NumberFormatter;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.Engine_energy;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.Engine_maxEnergy;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerProvider_getEnergyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerProvider_getMaxEnergyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerReceptor;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerReceptor_getPowerProvider;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.PowerProvider_energyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.PowerProvider_maxEnergyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.TileEngine;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.TileEngine_engine;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerBC3Energy implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC3Energy();

    private HUDHandlerBC3Energy() {
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
        if (!config.get("bcapi.storage")) return;
        if (!accessor.getNBTData().hasKey("MJMaxStorage")) return;
        if (!accessor.getNBTData().hasKey("MJEnergy")) return;

        int maxEnergy = accessor.getNBTInteger("MJMaxStorage");
        int energy = Math.min(accessor.getNBTInteger("MJEnergy"), maxEnergy);
        try {
            if (maxEnergy > 0 && currenttip.getEntries("MJEnergyStorage").isEmpty()) {
                if (config.get("bcapi.energybars")) {
                    currenttip.add(TTRenderEnergyBar.createMJ(energy, maxEnergy), "MJEnergyStorage");
                } else {
                    currenttip.add(I18n.translate("hud.msg.stored") + TAB + ALIGNRIGHT +
                                   WHITE + NumberFormatter.format(energy) + RESET + " / " +
                                   WHITE + NumberFormatter.format(maxEnergy) + RESET + " MJ",
                            "MJEnergyStorage");
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
            Float energy = -1f;
            Integer maxsto = -1;
            if (TileEngine.isInstance(te)) {
                Object engine = TileEngine_engine.get(te);
                if (engine != null) {
                    energy = Engine_energy.getFloat(engine);
                    maxsto = Engine_maxEnergy.getInt(engine);
                }
            } else if (IPowerReceptor.isInstance(te)) {
                Object prov = IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = IPowerProvider_getEnergyStored != null
                            ? (Float) IPowerProvider_getEnergyStored.invoke(prov)
                            : PowerProvider_energyStored.getFloat(prov);
                    maxsto = IPowerProvider_getMaxEnergyStored != null
                            ? (Integer) IPowerProvider_getMaxEnergyStored.invoke(prov)
                            : PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            if (energy != null && maxsto != null) {
                tag.setInteger("MJEnergy", Math.round(energy));
                tag.setInteger("MJMaxStorage", maxsto);
            }

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

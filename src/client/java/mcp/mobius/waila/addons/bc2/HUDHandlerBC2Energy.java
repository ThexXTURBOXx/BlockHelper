package mcp.mobius.waila.addons.bc2;

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

import static mcp.mobius.waila.addons.bc2.BC2Plugin.Engine_energy;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.Engine_maxEnergy;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.IPowerReceptor;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.IPowerReceptor_getPowerProvider;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.PowerProvider_energyStored;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.PowerProvider_maxEnergyStored;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.TileEngine;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.TileEngine_engine;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerBC2Energy implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC2Energy();

    private HUDHandlerBC2Energy() {
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
            int energy = -1;
            int maxsto = -1;
            if (TileEngine.isInstance(te)) {
                Object engine = TileEngine_engine.get(te);
                if (engine != null) {
                    energy = Engine_energy.getInt(engine);
                    maxsto = Engine_maxEnergy.getInt(engine);
                }
            } else if (IPowerReceptor.isInstance(te)) {
                Object prov = IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = PowerProvider_energyStored.getInt(prov);
                    maxsto = PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            tag.setInteger("MJEnergy", energy);
            tag.setInteger("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

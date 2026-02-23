package mcp.mobius.waila.addons.advmachines;

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

public final class HUDHandlerAdvGenerator implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvGenerator();

    private HUDHandlerAdvGenerator() {
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
        if (!config.get("advmachines.storage")) return;
        if (!accessor.getNBTData().hasKey("maxStorage")) return;
        if (!accessor.getNBTData().hasKey("storage")) return;

        int maxEnergy = accessor.getNBTInteger("maxStorage");
        int energy = Math.min(accessor.getNBTInteger("storage"), maxEnergy);
        try {
            if (maxEnergy > 0 && currenttip.getEntries("EUEnergyStorage").isEmpty()) {
                if (config.get("advmachines.energybars")) {
                    currenttip.add(TTRenderEnergyBar.createEU(energy, maxEnergy), "EUEnergyStorage");
                } else {
                    currenttip.add(I18n.translate("hud.msg.stored") + TAB + ALIGNRIGHT +
                                   WHITE + NumberFormatter.format(energy) + RESET + " / " +
                                   WHITE + NumberFormatter.format(maxEnergy) + RESET + " EU",
                            "EUEnergyStorage");
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
            int storage = -1;
            int maxStorage = -1;

            if (AdvMachinesPlugin.TileAM2BaseGenerator.isInstance(te)) {
                storage = AdvMachinesPlugin.TileAM2BaseGenerator_stored.getInt(te);
                maxStorage = AdvMachinesPlugin.TileAM2BaseGenerator_maxStorage.getInt(null);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

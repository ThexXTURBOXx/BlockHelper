package mcp.mobius.waila.addons.ic2;

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

import static mcp.mobius.waila.addons.ic2.IC2Plugin.IEnergyStorage;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileBaseGenerator;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityElecMachine;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityElecMachine_maxEnergy;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerElecMachine implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerElecMachine();

    private HUDHandlerElecMachine() {
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
        if (!config.get("ic2.storage")) return;
        if (!accessor.getNBTData().hasKey("maxStorage")) return;
        if (!accessor.getNBTData().hasKey("energy")) return;

        int maxEnergy = accessor.getNBTInteger("maxStorage");
        int energy = Math.min(accessor.getNBTInteger("energy"), maxEnergy);
        try {
            if (maxEnergy > 0 && currenttip.getEntries("EUEnergyStorage").isEmpty()) {
                if (config.get("ic2.energybars")) {
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
            if (IEnergyStorage.isInstance(te)) return; // skip, handled elsewhere
            if (TileBaseGenerator.isInstance(te)) return; // skip, handled elsewhere

            int maxStorage = -1;

            if (TileEntityElecMachine.isInstance(te)) {
                maxStorage = TileEntityElecMachine_maxEnergy.getInt(te);
            }

            tag.setInteger("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass(), null);
        }
    }

}

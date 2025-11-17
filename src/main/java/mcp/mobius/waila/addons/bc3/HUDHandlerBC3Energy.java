package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.SpecialChars;
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
                    currenttip.add(
                            SpecialChars.getRenderString("waila.energy", energy + "", maxEnergy + "", "MJ"),
                            "MJEnergyStorage");
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
            if (BC3Plugin.TileEngine.isInstance(te)) {
                Object engine = BC3Plugin.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BC3Plugin.Engine_energy.getFloat(engine);
                    maxsto = BC3Plugin.Engine_maxEnergy.getInt(engine);
                }
            } else if (BC3Plugin.IPowerReceptor.isInstance(te)) {
                Object prov = BC3Plugin.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = (Float) BC3Plugin.IPowerProvider_getEnergyStored.invoke(prov);
                    maxsto = (Integer) BC3Plugin.IPowerProvider_getMaxEnergyStored.invoke(prov);
                }
            }

            tag.setInteger("MJEnergy", Math.round(energy));
            tag.setInteger("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

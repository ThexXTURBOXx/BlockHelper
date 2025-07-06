package mcp.mobius.waila.addons.bc2;

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
        if (!accessor.getNBTData().hasKey("MJEnergy")) return;

        int energy = accessor.getNBTInteger("MJEnergy");
        int maxEnergy = accessor.getNBTInteger("MJMaxStorage");
        try {
            if (maxEnergy > 0 && currenttip.getEntries("MJEnergyStorage").isEmpty()) {
                String storedStr = I18n.translate("hud.msg.stored");
                currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(energy, maxEnergy) +
                               RESET + " / " + WHITE + maxEnergy + RESET + " MJ", "MJEnergyStorage");
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
            if (BC2Plugin.TileEngine.isInstance(te)) {
                Object engine = BC2Plugin.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BC2Plugin.Engine_energy.getInt(engine);
                    maxsto = BC2Plugin.Engine_maxEnergy.getInt(engine);
                }
            } else if (BC2Plugin.IPowerReceptor.isInstance(te)) {
                Object prov = BC2Plugin.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = BC2Plugin.PowerProvider_energyStored.getInt(prov);
                    maxsto = BC2Plugin.PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            tag.setInteger("MJEnergy", energy);
            tag.setInteger("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}

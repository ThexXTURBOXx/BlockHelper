package mcp.mobius.waila.addons.buildcraft;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.LangUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerBCEnergy implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBCEnergy();

    private HUDHandlerBCEnergy() {
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
        if (!accessor.getNBTData().hasKey("Energy")) return;

        int energy = accessor.getNBTInteger("Energy");
        int maxEnergy = accessor.getNBTInteger("MaxStorage");
        try {
            if (maxEnergy > 0 && currenttip.getEntries("MJEnergyStorage").isEmpty()) {
                String storedStr = LangUtil.translateG("hud.msg.stored");
                currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(energy, maxEnergy) +
                               RESET + " / " + WHITE + maxEnergy + RESET + " MJ", "MJEnergyStorage");
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
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
            if (BCPlugin.TileEngine.isInstance(te)) {
                Object engine = BCPlugin.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BCPlugin.Engine_energy.getFloat(engine);
                    maxsto = BCPlugin.Engine_maxEnergy.getInt(engine);
                }
            } else if (BCPlugin.IPowerReceptor.isInstance(te)) {
                Object prov = BCPlugin.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = (Float) BCPlugin.IPowerProvider_getEnergyStored.invoke(prov);
                    maxsto = (Integer) BCPlugin.IPowerProvider_getMaxEnergyStored.invoke(prov);
                }
            }

            tag.setInteger("Energy", Math.round(energy));
            tag.setInteger("MaxStorage", maxsto);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}

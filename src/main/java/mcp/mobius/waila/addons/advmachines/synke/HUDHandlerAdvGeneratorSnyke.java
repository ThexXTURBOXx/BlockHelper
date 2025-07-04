package mcp.mobius.waila.addons.advmachines.synke;

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

public final class HUDHandlerAdvGeneratorSnyke implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvGeneratorSnyke();

    private HUDHandlerAdvGeneratorSnyke() {
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
        try {
            int storage = accessor.getNBTInteger("storage");
            int maxStorage = accessor.getNBTInteger("maxStorage");

            String storedStr = I18n.translate("hud.msg.stored");

            /* EU Storage */
            if (config.get("advmachines.storage")) {
                if (maxStorage > 0)
                    currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                                   RESET + " / " + WHITE + maxStorage + RESET + " EU");
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

            if (AdvMachinesSnykePlugin.TileAdvMachine.isInstance(te)) {
                storage = AdvMachinesSnykePlugin.TileAdvMachine_energy.getInt(te);
                maxStorage = AdvMachinesSnykePlugin.TileAdvMachine_maxEnergy.getInt(null);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}

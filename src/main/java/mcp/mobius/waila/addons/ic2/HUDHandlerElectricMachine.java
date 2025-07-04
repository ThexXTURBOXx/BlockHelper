package mcp.mobius.waila.addons.ic2;

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

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerElectricMachine implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerElectricMachine();

    private HUDHandlerElectricMachine() {
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
        if (config.get("ic2.storage"))
            try {
                int storage = accessor.getNBTInteger("energy");
                int maxStorage = accessor.getNBTInteger("maxStorage");

                String storedStr = I18n.translate("hud.msg.stored");

                if (maxStorage > 0)
                    currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                                   RESET + " / " + WHITE + maxStorage + RESET + " EU");
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
            if (IC2Plugin.IEnergyStorage.isInstance(te)) return; // skip, handled elsewhere
            if (IC2Plugin.TileBaseGenerator.isInstance(te)) return; // skip, handled elsewhere

            int maxStorage = -1;

            if (IC2Plugin.TileEntityElectricMachine.isInstance(te)) {
                maxStorage = IC2Plugin.TileEntityElectricMachine_maxEnergy.getInt(te);
            }

            tag.setInteger("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass(), null);
        }
    }

}

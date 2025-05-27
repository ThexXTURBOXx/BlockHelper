package mcp.mobius.waila.addons.ee3;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.YELLOW;

public final class HUDHandlerEMC implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerEMC();

    private HUDHandlerEMC() {
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
            /* EMC */
            if (config.get("ee3.emc")) {
                Object registry = EE3Plugin.EMCRegistry_instance.invoke(null);
                if (registry != null) {
                    Object entry = EE3Plugin.EMCRegistry_getEMCValue.invoke(registry,
                            accessor.getBlockID(), accessor.getMetadata());
                    if (entry != null)
                        currenttip.add(YELLOW + "EMC: " + GRAY + EE3Plugin.EMCEntry_getCost.invoke(entry));
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
    }

}

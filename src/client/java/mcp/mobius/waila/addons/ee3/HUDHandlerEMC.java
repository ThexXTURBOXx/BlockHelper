package mcp.mobius.waila.addons.ee3;

import java.util.Map;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

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
    @SuppressWarnings("unchecked")
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            /* EMC */
            if (config.get("ee3.emc")) {
                Object emcList = EE3Plugin.mod_EE3_emcList.get(null);
                Map<Integer, Map<Integer, Object>> emcMap = (Map<Integer, Map<Integer, Object>>)
                        EE3Plugin.EMCList_emcMap.get(emcList);
                if (emcMap != null) {
                    Map<Integer, Object> metaMap = emcMap.get(itemStack.itemID);
                    if (metaMap != null) {
                        boolean qm = false;
                        Object value = metaMap.get(itemStack.itemDamage);
                        if (value == null) {
                            value = metaMap.get(0);
                            qm = true;
                        }
                        if (value != null)
                            currenttip.add(YELLOW + "EMC: " + GRAY +
                                           EE3Plugin.EMCValue_getCostEMC.invoke(value) + (qm ? "?" : ""));
                    }
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

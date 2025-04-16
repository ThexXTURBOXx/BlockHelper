package mcp.mobius.waila.addons.projectred;

import mcp.mobius.waila.api.IFMPAccessor;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.LangUtil;
import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.item.ItemStack;

public final class HUDFMPWires implements IFMPProvider {

    public static final IFMPProvider INSTANCE = new HUDFMPWires();

    private HUDFMPWires() {
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
        if (!config.get("pr.showsignal")) return;

        int signal = ((NBTUtil.getNBTInteger(accessor.getNBTData(), "signal") & 0xff) + 16) / 17;
        if (currenttip.getEntries("signal").isEmpty()) {
            currenttip.add(LangUtil.translateG("hud.msg.power") + " : " + signal, "signal");
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
    }

}

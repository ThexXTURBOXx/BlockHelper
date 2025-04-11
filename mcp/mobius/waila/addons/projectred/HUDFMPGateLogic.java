package mcp.mobius.waila.addons.projectred;

import mcp.mobius.waila.api.IFMPAccessor;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.item.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDFMPGateLogic implements IFMPProvider {

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
        if (!config.get("pr.showdata")) return;

        int orient = 0;
        int subID = 0;
        int shape = 0;
        int pmax = 0;
        int val = 0;
        int max = 0;
        int dec = 0;
        int inc = 0;

        orient = NBTUtil.getNBTInteger(accessor.getNBTData(), "orient");
        subID = NBTUtil.getNBTInteger(accessor.getNBTData(), "subID");
        shape = NBTUtil.getNBTInteger(accessor.getNBTData(), "shape");

        if (subID == 17)
            pmax = NBTUtil.getNBTInteger(accessor.getNBTData(), "pmax");

        if (subID == 19) {
            val = NBTUtil.getNBTInteger(accessor.getNBTData(), "val");
            max = NBTUtil.getNBTInteger(accessor.getNBTData(), "max");
            dec = NBTUtil.getNBTInteger(accessor.getNBTData(), "dec");
            inc = NBTUtil.getNBTInteger(accessor.getNBTData(), "inc");
        }

        switch (subID) {
        case 10:
            currenttip.add(String.format("[Repeater]" + TAB + ALIGNRIGHT + WHITE + "%d" + GRAY + " ticks",
                    (int) Math.pow(2, shape)));
            break;

        case 15:
            if (shape == 0)
                currenttip.add("[Sensor]" + TAB + ALIGNRIGHT + WHITE + "Open");
            if (shape == 1)
                currenttip.add("[Sensor]" + TAB + ALIGNRIGHT + WHITE + "Half closed");
            if (shape == 2)
                currenttip.add("[Sensor]" + TAB + ALIGNRIGHT + WHITE + "Closed");
            break;

        case 17:
            currenttip.add(String.format("[Timer delay]" + TAB + ALIGNRIGHT + WHITE + "%d " + GRAY + "ms",
                    (pmax + 2) * 50));
            break;

        case 19:


            currenttip.add(String.format("[Counter value]" + TAB + ALIGNRIGHT + WHITE + "%d " + GRAY + "/ " + WHITE + "%d", val, max));
            currenttip.add(String.format("[Counter step]" + TAB + ALIGNRIGHT + WHITE + "-%d " + GRAY + "/ " + WHITE + "+%d", dec, inc));

        default:
            break;
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IFMPAccessor accessor, IPluginConfig config) {
    }

}

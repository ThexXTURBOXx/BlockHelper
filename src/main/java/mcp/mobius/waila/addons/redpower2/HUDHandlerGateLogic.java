package mcp.mobius.waila.addons.redpower2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.GEQ;
import static mcp.mobius.waila.api.SpecialChars.TAB;

public final class HUDHandlerGateLogic implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerGateLogic();

    static final int[] REPEATER_DELAYS = new int[]{1, 2, 3, 4, 8, 16, 32, 64, 128};

    private HUDHandlerGateLogic() {
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
        if (!config.get("pr.showdata")) return;

        String type = accessor.getNBTData().getString("id");
        int subId = accessor.getNBTInteger("sid");
        if (type == null) return;

        if (type.equals("RPLgSmp") && subId == 12) { // Repeater
            int tick = REPEATER_DELAYS[accessor.getNBTInteger("dm")];
            if (tick == 1)
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.tick"));
            else
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.ticks_format", tick));
        } else if (type.equals("RPLgPtr") && (subId == 0 || subId == 1)) { // Timer and Sequencer
            int intv = accessor.getNBTInteger("iv");
            if (subId == 0) intv += 2;
            String pmax = String.format("%.2f", intv / 20f);
            currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                           I18n.translate("hud.msg.seconds_format", pmax));
        } else if (type.equals("RPLgStor") && subId == 0) { // Counter
            int val = accessor.getNBTInteger("cnt");
            int max = accessor.getNBTInteger("max");
            int dec = accessor.getNBTInteger("dec");
            int inc = accessor.getNBTInteger("inc");
            currenttip.add(I18n.translate("hud.msg.value") + ":" + TAB + val + " / " + max);
            currenttip.add(I18n.translate("hud.msg.step") + ":" + TAB + "-" + dec + " / " + "+" + inc);
        } else if (type.equals("RPLgSmp") && subId == 16) { // Light Sensor
            int dm = accessor.getNBTInteger("dm");
            String open;
            if (dm == 0) open = GEQ + " 1";
            else if (dm == 1) open = GEQ + " 5";
            else if (dm == 2) open = GEQ + " 9";
            else if (dm == 3) open = GEQ + " 13";
            else open = I18n.translate("hud.msg.please_report");

            currenttip.add(I18n.translate("hud.msg.state") + ": " + open);
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

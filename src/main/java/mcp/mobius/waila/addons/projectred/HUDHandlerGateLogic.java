package mcp.mobius.waila.addons.projectred;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.GEQ;
import static mcp.mobius.waila.api.SpecialChars.TAB;

public final class HUDHandlerGateLogic implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerGateLogic();

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

        int type = accessor.getNBTInteger("type");
        int gateSettings = accessor.getNBTInteger("gateSettings");
        NBTTagCompound logic = accessor.getNBTData().getCompoundTag("logic");

        switch (type) {
        case 11: // Repeater
            int tick = (int) Math.pow(2, gateSettings);
            if (tick == 1)
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.tick"));
            else
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.ticks_format", tick));
            break;

        case 12: // Timer
        case 14: // Sequencer
            if (logic == null) break;
            String pmax = String.format("%.2f", accessor.getNBTInteger(logic, "intv") / 20f);
            currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                           I18n.translate("hud.msg.seconds_format", pmax));
            break;

        case 13: // Counter
            if (logic == null) break;
            int val = accessor.getNBTInteger(logic, "cur");
            int max = accessor.getNBTInteger(logic, "max");
            int dec = accessor.getNBTInteger(logic, "-");
            int inc = accessor.getNBTInteger(logic, "+");
            currenttip.add(I18n.translate("hud.msg.value") + ":" + TAB + val + " / " + max);
            currenttip.add(I18n.translate("hud.msg.step") + ":" + TAB + "-" + dec + " / " + "+" + inc);
            break;

        case 24: // Light Sensor
            String open;
            if (gateSettings == 0) open = GEQ + " 3";
            else if (gateSettings == 1) open = GEQ + " 6";
            else if (gateSettings == 2) open = GEQ + " 9";
            else if (gateSettings == 3) open = GEQ + " 12";
            else if (gateSettings == 4) open = GEQ + " 15";
            else if (gateSettings == 5) open = I18n.translate("hud.msg.sensor");
            else open = I18n.translate("hud.msg.please_report");

            currenttip.add(I18n.translate("hud.msg.state") + ": " + open);
            break;

        default:
            break;
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

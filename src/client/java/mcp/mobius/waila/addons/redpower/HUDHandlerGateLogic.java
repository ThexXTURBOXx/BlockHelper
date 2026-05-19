package mcp.mobius.waila.addons.redpower;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

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
        if (type == null) return;

        if (type.equals("IRRepeater")) {
            int tick = REPEATER_DELAYS[accessor.getNBTInteger("dm")];
            if (tick == 1)
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.tick"));
            else
                currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                               I18n.translate("hud.msg.ticks_format", tick));
        } else if (type.equals("Timer") || type.equals("Sequencer")) {
            int intv = accessor.getNBTInteger("iv");
            if (type.equals("Timer")) intv += 2;
            String pmax = String.format("%.2f", intv / 20f);
            currenttip.add(I18n.translate("hud.msg.delay") + ": " +
                           I18n.translate("hud.msg.seconds_format", pmax));
        } else if (type.equals("IRCounter")) {
            int val = accessor.getNBTInteger("cnt");
            int max = accessor.getNBTInteger("max");
            int dec = accessor.getNBTInteger("dec");
            int inc = accessor.getNBTInteger("inc");
            currenttip.add(I18n.translate("hud.msg.value") + ":" + TAB + val + " / " + max);
            currenttip.add(I18n.translate("hud.msg.step") + ":" + TAB + "-" + dec + " / " + "+" + inc);
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

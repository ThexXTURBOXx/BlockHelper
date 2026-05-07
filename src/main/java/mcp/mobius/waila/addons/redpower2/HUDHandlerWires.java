package mcp.mobius.waila.addons.redpower2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileCable;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileInsulatedWire;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileRedwire;

public final class HUDHandlerWires implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerWires();

    private HUDHandlerWires() {
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
        if (!config.get("pr.showsignal")) return;

        if (TileRedwire.isInstance(accessor.getTileEntity()) || TileInsulatedWire.isInstance(accessor.getTileEntity())) {
            int signal = RP2StrengthToVanilla(accessor.getNBTInteger("pwr"));
            if (currenttip.getEntries("strength").isEmpty()) {
                currenttip.add(I18n.translate("hud.msg.power") + ": " + signal, "strength");
            }
        } else if (TileCable.isInstance(accessor.getTileEntity())) {
            boolean added = false;
            byte[] signals = accessor.getNBTData().getByteArray("pwrs");
            for (int i = 0; i < signals.length; ++i) {
                int signal = RP2StrengthToVanilla(signals[i]);
                if (signal > 0) {
                    added = true;
                    currenttip.add(I18n.color(15 - i) + " " +
                                   I18n.translate("hud.msg.power") + ": " + signal, "strength");
                }
            }
            if (!added) currenttip.add(I18n.translate("hud.msg.power") + ": 0", "strength");
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag, IServerDataAccessor accessor,
                                 IPluginConfig config) {
    }

    public static int RP2StrengthToVanilla(int strength) {
        return ((strength & 0xff) + 16) / 17;
    }

}

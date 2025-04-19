package mcp.mobius.waila.addons.thermalexpansion;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;

public final class HUDHandlerTesseract implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerTesseract();

    private HUDHandlerTesseract() {
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

        TileEntity te = accessor.getTileEntity();

        if (!accessor.getNBTData().getBoolean("flag.active")) return;

        if (config.get("thermalexpansion.tesssendrecv")) {
            String send = I18n.translate("hud.msg.send") + " : ";
            String recv = I18n.translate("hud.msg.recv") + " : ";

            String type;
            if (ThermalExpansionPlugin.TileTesseractItem.isInstance(te))
                type = GREEN + I18n.translate("hud.msg.item") + " ";
            else if (ThermalExpansionPlugin.TileTesseractLiquid.isInstance(te))
                type = BLUE + I18n.translate("hud.msg.fluid") + " ";
            else if (ThermalExpansionPlugin.TileTesseractEnergy.isInstance(te))
                type = RED + I18n.translate("hud.msg.energ") + " ";
            else
                type = GRAY + "<" + I18n.translate("hud.msg.unknown") + ">";

            int mode = accessor.getNBTInteger("mode");
            if (mode != 1) currenttip.add(send + type);
            if (mode != 0) currenttip.add(recv + type);
        }

        if (config.get("thermalexpansion.tessfreq"))
            currenttip.add(I18n.translate("hud.msg.frequency") + " : " + accessor.getNBTInteger("frequency"));
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te != null)
            te.writeToNBT(tag);
    }

}

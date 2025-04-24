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

public final class HUDHandlerEnergyCell implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerEnergyCell();

    private HUDHandlerEnergyCell() {
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
        if (!config.get("thermalexpansion.energycell")) return;

        int energyReceive = accessor.getNBTInteger("Recv");
        int energySend = accessor.getNBTInteger("Send");

        currenttip.add(I18n.translate("hud.msg.in") + "/" + I18n.translate("hud.msg.out") + ": " +
                       energyReceive + " / " + energySend + " MJ/t");
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int recv = ThermalExpansionPlugin.TileEnergyCell_Recv.getInt(te);
            int send = ThermalExpansionPlugin.TileEnergyCell_Send.getInt(te);
            tag.setInteger("Recv", recv);
            tag.setInteger("Send", send);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}

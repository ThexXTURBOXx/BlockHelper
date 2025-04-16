package mcp.mobius.waila.addons.thermalexpansion;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
            String send = String.format("%s : ", LangUtil.translateG("hud.msg.send"));
            String recv = String.format("%s : ", LangUtil.translateG("hud.msg.recv"));

            String type = "<Unknown>";
            if (ThermalExpansionPlugin.TileTesseractItem.isInstance(te))
                type = String.format("\u00a7a%s ", LangUtil.translateG("hud.msg.item"));
            else if (ThermalExpansionPlugin.TileTesseractLiquid.isInstance(te))
                type = String.format("\u00a79%s ", LangUtil.translateG("hud.msg.fluid"));
            else if (ThermalExpansionPlugin.TileTesseractEnergy.isInstance(te))
                type = String.format("\u00a7c%s ", LangUtil.translateG("hud.msg.energ"));

            int mode = accessor.getNBTInteger("mode");
            if (mode != 1) currenttip.add(send + type);
            if (mode != 0) currenttip.add(recv + type);
        }

        if (config.get("thermalexpansion.tessfreq"))
            currenttip.add(String.format("%s : %d", LangUtil.translateG("hud.msg.frequency"),
                    accessor.getNBTInteger("frequency")));
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
    }

}

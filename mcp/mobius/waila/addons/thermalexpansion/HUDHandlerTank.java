package mcp.mobius.waila.addons.thermalexpansion;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.LiquidHelper;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;

public class HUDHandlerTank implements IDataProvider {

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("thermalexpansion.fluidtype")) return;

        try {
            LiquidStack liquid =
                    (LiquidStack) ThermalExpansionModule.TileTank_getTankFluid.invoke(accessor.getTileEntity());
            String name = currenttip.get(0);

            try {
                name += String.format(" < %s >", LiquidHelper.getLiquidName(liquid));
            } catch (NullPointerException f) {
                name += " " + LangUtil.translateG("hud.msg.empty");
            }

            currenttip.set(0, name);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("thermalexpansion.fluidamount")) {
                int amount = 0;
                if (accessor.getNBTData().hasKey("Amount"))
                    amount = accessor.getNBTInteger(accessor.getNBTData(), "Amount");

                Integer capacity =
                        (Integer) ThermalExpansionModule.TileTank_getTankCapacity.invoke(accessor.getTileEntity());

                currenttip.add(String.format("%d / %d mB", amount, capacity));
            }

            if (config.get("thermalexpansion.tankmode")) {
                Byte mode = (Byte) ThermalExpansionModule.TileTank_mode.get(accessor.getTileEntity());
                if (mode == 0)
                    currenttip.add(String.format("%s : \u00a7a%s", LangUtil.translateG("hud.msg.mode"),
                            LangUtil.translateG("hud.msg.input")));
                else if (mode == 1)
                    currenttip.add(String.format("%s : \u00a7c%s", LangUtil.translateG("hud.msg.mode"),
                            LangUtil.translateG("hud.msg.output")));
                else
                    currenttip.add(String.format("Mode : Unknown (%d)", mode));
            }


        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass().getName(), currenttip);
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        try {
            int amount = (Integer) ThermalExpansionModule.TileTank_getTankAmount.invoke(te);
            tag.setInteger("Amount", amount);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return tag;
    }

}

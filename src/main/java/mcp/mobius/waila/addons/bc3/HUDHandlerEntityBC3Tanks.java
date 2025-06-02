package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_amount;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_loadLiquidStackFromNBT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntityBC3Tanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityBC3Tanks();

    private HUDHandlerEntityBC3Tanks() {
    }

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tanktype")) {
                NBTTagCompound compound = accessor.getNBTData();
                Object stack = compound.hasKey("liquidstack")
                        ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                        : null;
                int capacity = accessor.getNBTInteger("liquidcapacity");

                if (capacity > 0) {
                    String name = currenttip.get(0);
                    name += " " + (stack == null
                            ? I18n.translate("hud.msg.empty")
                            : ("(" + LiquidHelper.getLiquidName(stack) + RESET + WHITE + ")"));
                    currenttip.set(0, name);
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            if (config.get("bc.tankamount")) {
                NBTTagCompound compound = accessor.getNBTData();
                Object stack = compound.hasKey("liquidstack")
                        ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                        : null;
                int liquidAmount = stack != null ? LiquidStack_amount.getInt(stack) : 0;
                int capacity = accessor.getNBTInteger("liquidcapacity");

                if (capacity > 0)
                    currenttip.add(liquidAmount + "/" + capacity + " mB");
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), currenttip);
        }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT(ent, tag);
    }

}

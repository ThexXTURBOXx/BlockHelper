package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntityForgeTanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityForgeTanks();

    private HUDHandlerEntityForgeTanks() {
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
        if (config.get("forge.tanktype")) {
            NBTTagCompound compound = accessor.getNBTData();
            LiquidStack stack = compound.hasKey("liquidstack")
                    ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
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
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("forge.tankamount")) {
            NBTTagCompound compound = accessor.getNBTData();
            LiquidStack stack = compound.hasKey("liquidstack")
                    ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                    : null;
            int liquidAmount = stack != null ? stack.amount : 0;
            int capacity = accessor.getNBTInteger("liquidcapacity");

            if (capacity > 0)
                currenttip.add(liquidAmount + "/" + capacity + " mB");
        }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT((ITankContainer) ent, tag);
    }

}

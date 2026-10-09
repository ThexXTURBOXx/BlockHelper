package mcp.mobius.waila.addons.bc3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.api.LiquidData;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidQuantity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidSlots;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidQty;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.newLiquidSlot;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static void writeToNBT(Object container, NBTTagCompound tag) throws Throwable {
        List<LiquidData> tanks = new ArrayList<LiquidData>();

        if (WailaRegistrar.instance().hasTankProviders(container)) {
            bigLoop:
            for (List<ITankProvider> providersList :
                    WailaRegistrar.instance().getTankProviders(container).values()) {
                for (ITankProvider provider : providersList) {
                    List<LiquidData> provided = provider.getTanks(
                            container, ServerDataAccessorCommon.INSTANCE, PluginConfig.instance());
                    if (provided == null) {
                        tanks = null;
                        break bigLoop;
                    } else {
                        tanks.addAll(provided);
                    }
                }
            }
        }

        if (tanks == null)
            tanks = new ArrayList<LiquidData>();
        else if (tanks.isEmpty())
            tanks.addAll(getTanksNaively(container));
        NBTTagList tags = new NBTTagList();
        for (LiquidData tank : tanks) {
            if (tank != null) tags.appendTag(tank.toNBT());
        }
        tag.setTag("WailaTanks", tags);
    }

    /**
     * We assume {@code container} to be some tank container.
     * Returns some liquid tank instance.
     */
    public static List<LiquidData> getTanksNaively(Object container) throws Throwable {
        // First, we collect the tanks...
        Set<Object> tankObjs = new LinkedHashSet<Object>();

        if (ILiquidContainer != null && ILiquidContainer.isInstance(container)) {
            boolean added = false;
            if (ILiquidContainer_getLiquidSlots != null) {
                Object[] slots = (Object[]) ILiquidContainer_getLiquidSlots.invoke(container);

                if (slots != null) {
                    for (Object slot : slots) {
                        if (slot != null) {
                            tankObjs.add(slot);
                            added = true;
                        }
                    }
                }
            }

            // Capacity not available here in 3.1.5
            if (!added) tankObjs.add(newLiquidSlot.newInstance(
                    ILiquidContainer_getLiquidId.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container)));
        }

        // ...then we "translate" to LiquidData
        List<LiquidData> ret = new LinkedList<LiquidData>();
        for (Object tank : tankObjs) {
            ret.add(toLiquidData(tank));
        }
        return ret;
    }

    public static LiquidData toLiquidData(Object tank) throws Throwable {
        if (tank instanceof LiquidData) return (LiquidData) tank;
        if (LiquidSlot != null && LiquidSlot.isInstance(tank)) {
            int id = (Integer) LiquidSlot_getLiquidId.invoke(tank);
            if (id <= 0) id = TTRenderLiquidBar.EMPTY_LIQUID;
            int amount = (Integer) LiquidSlot_getLiquidQty.invoke(tank);
            int capacity = (Integer) LiquidSlot_getCapacity.invoke(tank);
            return new LiquidData(id, amount, 0, capacity);
        }
        return null;
    }

    public static List<LiquidData> readLiquidData(NBTTagList tags) {
        List<LiquidData> ret = new ArrayList<LiquidData>(tags.tagCount());
        for (int i = 0; i < tags.tagCount(); ++i) {
            NBTBase base = tags.tagAt(i);
            if (base instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) base;
                ret.add(LiquidData.readFromNBT(tag));
            }
        }
        return ret;
    }

    public static List<LiquidData> getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        return readLiquidData(accessor.getNBTData().getTagList("WailaTanks"));
    }

    public static List<LiquidData> getLiquidData(IDataAccessor accessor, IPluginConfig config) throws Throwable {
        if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            return Collections.singletonList(new LiquidData(Block.waterStill.blockID,
                    (int) Math.round(Math.min(3, meta) * 333.3), 0, 1000));
        }

        return readLiquidData(accessor.getNBTData().getTagList("WailaTanks"));
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            if (data.getId() != TTRenderLiquidBar.EMPTY_LIQUID) {
                return bar
                        ? TTRenderLiquidBar.create(data)
                        : (data.getAmount() + "/" + data.getCapacity() + " mB");
            } else {
                return bar ? TTRenderLiquidBar.createEmpty(data.getCapacity()) : null;
            }
        }
        return null;
    }

    public static String findLiquidName(LiquidData liquidData) {
        try {
            ItemStack stack = liquidData.asItemStack();
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        return I18n.translate("hud.msg.unknown");
    }

}

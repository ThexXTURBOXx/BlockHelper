package mcp.mobius.waila.addons.bc3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getLiquid;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer_getTanks;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidDictionary_liquids;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_amount;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_isLiquidEqual;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemID;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemMeta;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.newLiquidStack;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static void writeToNBT(Object container, NBTTagCompound tag) throws Throwable {
        List<Object> tanks = new ArrayList<Object>();

        if (WailaRegistrar.instance().hasTankProviders(container)) {
            bigLoop:
            for (List<ITankProvider> providersList :
                    WailaRegistrar.instance().getTankProviders(container).values()) {
                for (ITankProvider provider : providersList) {
                    List<Object> provided = provider.getTanks(
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
            tanks = new ArrayList<Object>();
        else if (tanks.isEmpty())
            tanks.addAll(getTanksNaively(container));
        NBTTagList tags = new NBTTagList();
        for (Object tank : tanks) {
            Object stack = ILiquidTank_getLiquid.invoke(tank);
            int id = stack != null ? LiquidStack_itemID.getInt(stack) : TTRenderLiquidBar.EMPTY_LIQUID;
            int amount = stack != null ? LiquidStack_amount.getInt(stack) : 0;
            int meta = stack != null ? LiquidStack_itemMeta.getInt(stack) : 0;
            int capacity = (Integer) ILiquidTank_getCapacity.invoke(tank);
            tags.appendTag(new LiquidData(id, amount, meta, capacity).toNBT());
        }
        tag.setTag("WailaTanks", tags);
    }

    /**
     * We assume {@code container} to be some tank container.
     * Returns some liquid tank instance.
     */
    public static Set<Object> getTanksNaively(Object container) throws Throwable {
        Set<Object> ret = new LinkedHashSet<Object>();
        Object[] tanks = (Object[]) ITankContainer_getTanks.invoke(container);
        if (tanks != null) {
            for (Object tank : tanks) {
                if (tank != null) {
                    ret.add(tank);
                }
            }
        }
        return ret;
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
        if (ITankContainer.isInstance(accessor.getTileEntity())) {
            return readLiquidData(accessor.getNBTData().getTagList("WailaTanks"));
        } else if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            return Collections.singletonList(new LiquidData(Block.waterStill.blockID,
                    (int) Math.round(Math.min(3, meta) * 333.3), 0, 1000));
        }

        return Collections.singletonList(new LiquidData(TTRenderLiquidBar.EMPTY_LIQUID, 0, 0, 0));
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

    @SuppressWarnings("unchecked")
    public static String findLiquidName(LiquidData liquidData) {
        try {
            ItemStack stack = liquidData.asItemStack();
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        if (LiquidDictionary_liquids != null) {
            try {
                Object liquidStack = newLiquidStack.newInstance(liquidData.getId(),
                        liquidData.getAmount(), liquidData.getMeta());
                for (Map.Entry<String, Object> e :
                        ((Map<String, Object>) LiquidDictionary_liquids.get(null)).entrySet()) {
                    try {
                        if ((Boolean) LiquidStack_isLiquidEqual.invoke(liquidStack, e.getValue()))
                            return e.getKey();
                    } catch (Throwable ignored) {
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        return I18n.translate("hud.msg.unknown");
    }

}

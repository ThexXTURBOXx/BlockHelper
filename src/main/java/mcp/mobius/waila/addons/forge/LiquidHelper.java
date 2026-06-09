package mcp.mobius.waila.addons.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mcp.mobius.waila.addons.railcraft.RailcraftPlugin;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.api.LiquidData;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public final class LiquidHelper {

    private static final ForgeDirection[] DIRECTIONS = ForgeDirection.values();

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(ITankContainer container, NBTTagCompound tag) {
        List<ILiquidTank> tanks = new ArrayList<ILiquidTank>();

        if (WailaRegistrar.instance().hasTankProviders(container)) {
            bigLoop:
            for (List<ITankProvider> providersList :
                    WailaRegistrar.instance().getTankProviders(container).values()) {
                for (ITankProvider provider : providersList) {
                    List<ILiquidTank> provided = provider.getTanks(
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
            tanks = new ArrayList<ILiquidTank>();
        else if (tanks.isEmpty())
            tanks.addAll(getTanksNaively(container));
        NBTTagList tags = new NBTTagList();
        for (ILiquidTank tank : tanks) {
            tags.appendTag(new LiquidData(tank.getLiquid(), tank.getCapacity()).toNBT());
        }
        tag.setTag("WailaTanks", tags);
    }

    public static Set<ILiquidTank> getTanksNaively(ITankContainer container) {
        Set<ILiquidTank> ret = new LinkedHashSet<ILiquidTank>();
        for (ForgeDirection dir : DIRECTIONS) {
            ILiquidTank[] tanks = container.getTanks(dir);
            if (tanks != null) {
                for (ILiquidTank tank : tanks) {
                    if (tank != null) {
                        // Ugly hardcoded fix for ugly Railcraft code
                        try {
                            if (RailcraftPlugin.TankWrapper != null && RailcraftPlugin.TankWrapper.isInstance(tank)) {
                                tank = (ILiquidTank) RailcraftPlugin.TankWrapper_tank.get(tank);
                            }
                        } catch (Throwable t) {
                            WailaExceptionHandler.handleErr(t, "[Forge] Error trying to access a tank for display!",
                                    null);
                        }
                        ret.add(tank);
                    }
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

    public static List<LiquidData> getLiquidData(IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof ITankContainer) {
            return readLiquidData(accessor.getNBTData().getTagList("WailaTanks"));
        } else if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            LiquidStack stack = new LiquidStack(Block.waterStill, (int) Math.round(Math.min(3, meta) * 333.3));
            return Collections.singletonList(new LiquidData(stack, 1000));
        }

        return Collections.singletonList(new LiquidData(null, 0));
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            LiquidStack stack = data.getLiquidStack();
            if (stack != null) {
                return bar
                        ? TTRenderLiquidBar.create(stack, data.getCapacity())
                        : (stack.amount + "/" + data.getCapacity() + " mB");
            } else {
                return bar ? TTRenderLiquidBar.createEmpty(data.getCapacity()) : null;
            }
        }
        return null;
    }

}

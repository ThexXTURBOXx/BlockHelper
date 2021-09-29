package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LiquidSlot;
import buildcraft.api.PowerProvider;
import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.transport.TileGenericPipe;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(state.te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                // For some reason (ClassLoader issue?), we need to use reflection here...
                Float energyStored = getField(prov, "energyStored");
                Float maxEnergyStored = getField(prov, "maxEnergyStored");
                if (energyStored != null) {
                    info.add(energyStored + " MJ / " + maxEnergyStored + " MJ");
                }
            }
        } else if (iof(state.te, "buildcraft.api.power.IPowerReceptor")) {
            buildcraft.api.power.IPowerProvider prov =
                    ((buildcraft.api.power.IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                info.add(prov.getEnergyStored() + " MJ / " + prov.getMaxEnergyStored() + " MJ");
            }
        }
        if (iof(state.te, "buildcraft.api.ILiquidContainer")) {
            ILiquidContainer container = ((ILiquidContainer) state.te);
            Method m = getMethod(state.te, "getLiquidSlots");
            boolean flag = false;
            if (m != null) {
                try {
                    LiquidSlot[] slots = (LiquidSlot[]) m.invoke(state.te);
                    for (LiquidSlot slot : slots) {
                        int quantity = slot.getLiquidQty();
                        int capacity = Math.max(quantity, slot.getCapacity());
                        if (quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB"
                                    + formatLiquidName(getBc2LiquidName(slot)));
                        }
                    }
                    flag = true;
                } catch (IllegalAccessException ignored) {
                } catch (InvocationTargetException ignored) {
                }
            }
            if (!flag) {
                int quantity = container.getLiquidQuantity();
                int capacity = Math.max(quantity, container.getCapacity());
                if (quantity > 0) {
                    info.add(quantity + " mB / " + capacity + " mB"
                            + formatLiquidName(getBc2LiquidName(container)));
                }
            }
        } else if (iof(state.te, "buildcraft.api.liquids.ITankContainer")) {
            Method m = getMethod(state.te, "getTanks");
            if (m != null) {
                try {
                    ILiquidTank[] tanks = (ILiquidTank[]) m.invoke(state.te);
                    for (ILiquidTank tank : tanks) {
                        LiquidStack stack = tank.getLiquid();
                        int quantity = stack == null ? 0 : stack.amount;
                        int capacity = Math.max(quantity, tank.getCapacity());
                        if (quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB"
                                    + formatLiquidName(getBc3LiquidName(stack)));
                        }
                    }
                } catch (IllegalAccessException ignored) {
                } catch (InvocationTargetException ignored) {
                }
            }
        }
    }

    @Override
    public String getMod(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            return "BuildCraft";
        }
        return super.getMod(state);
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            TileGenericPipe pipe = (TileGenericPipe) state.te;
            if (pipe.pipe != null) {
                return new ItemStack(Item.itemsList[pipe.pipe.itemID], state.te.blockMetadata);
            }
        }
        return super.getItemStack(state);
    }

    public static String formatLiquidName(String liquidName) {
        return liquidName == null || liquidName.trim().isEmpty()
                ? "" : " of " + liquidName;
    }

    public static String getBc2LiquidName(Object liquidSlotOrContainer) {
        try {
            LiquidSlot slot = (LiquidSlot) liquidSlotOrContainer;
            ItemStack is = new ItemStack(slot.getLiquidId(), 1, 0);
            return is.getItem().getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        try {
            ILiquidContainer container = (ILiquidContainer) liquidSlotOrContainer;
            ItemStack is = new ItemStack(container.getLiquidId(), 1, 0);
            return is.getItem().getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static String getBc3LiquidName(Object liquidStack) {
        try {
            ItemStack is = ((LiquidStack) liquidStack).asItemStack();
            return is.getItem().getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        return null;
    }

}

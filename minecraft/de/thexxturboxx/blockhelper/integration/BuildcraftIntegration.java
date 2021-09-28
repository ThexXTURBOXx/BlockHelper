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
            if (engine != null && engine.energy > 0) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(state.te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                // For some reason (ClassLoader issue?), we need to use reflection here...
                Float energyStored = getField(prov, "energyStored");
                Float maxEnergyStored = getField(prov, "maxEnergyStored");
                if (energyStored != null && energyStored > 0) {
                    info.add(energyStored + " MJ / " + maxEnergyStored + " MJ");
                }
            }
        } else if (iof(state.te, "buildcraft.api.power.IPowerReceptor")) {
            buildcraft.api.power.IPowerProvider prov =
                    ((buildcraft.api.power.IPowerReceptor) state.te).getPowerProvider();
            if (prov != null && prov.getEnergyStored() > 0) {
                info.add(prov.getEnergyStored() + " MJ / " + prov.getMaxEnergyStored() + " MJ");
            }
        }
        if (iof(state.te, "buildcraft.api.ILiquidContainer")) {
            ILiquidContainer container = ((ILiquidContainer) state.te);
            Method m = getMethod(state.te, "getLiquidSlots");
            boolean flag = false;
            if (m != null) {
                LiquidSlot[] slots;
                try {
                    slots = (LiquidSlot[]) m.invoke(state.te);
                    for (LiquidSlot slot : slots) {
                        int quantity = slot.getLiquidQty();
                        int capacity = Math.max(quantity, slot.getCapacity());
                        if (quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB");
                        }
                        // TODO: Read liquid name from bucket from liquid ??? (BC3 uses BuildcraftAPI class)
                        // Do something here with API.liquids in the near future?
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
                    info.add(quantity + " mB / " + capacity + " mB");
                }
            }
        } else if (iof(state.te, "buildcraft.api.liquids.ITankContainer")) {
            Method m = getMethod(state.te, "getTanks");
            if (m != null) {
                ILiquidTank[] tanks;
                try {
                    tanks = (ILiquidTank[]) m.invoke(state.te);
                    for (ILiquidTank tank : tanks) {
                        LiquidStack stack = tank.getLiquid();
                        int quantity = stack == null ? 0 : stack.amount;
                        int capacity = Math.max(quantity, tank.getCapacity());
                        if (quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB");
                        }
                        // TODO: Read liquid name from bucket from liquid ???
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

}

package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public final class HUDHandlerFurnace implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFurnace();

    private HUDHandlerFurnace() {
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
        if (config.get("vanilla.furnace")) {
            NBTTagList items = accessor.getNBTData().getTagList("Items");
            String itemsText = "";
            ItemStack inputStack = null;
            ItemStack fuelStack = null;
            ItemStack outputStack = null;

            int smeltTime = accessor.getNBTInteger("CookTime");
            int fuelTime = accessor.getNBTInteger("BurnTime");
            int maxFuelTime = accessor.getNBTInteger("MaxFuel");
            // MaxFuel can be 0 when fuel is empty upon world load. This yields weird behavior here.
            // This new behavior is not much better, but at least prevents division by zero and other issues.
            if (maxFuelTime <= 0) maxFuelTime = fuelTime;

            double smeltProgress = (double) smeltTime / 200;
            double fuelProgress = (double) fuelTime / maxFuelTime;

            for (int i = 0; i < items.tagCount(); i++) {
                NBTBase itemTagBase = items.tagAt(i);
                if (!(itemTagBase instanceof NBTTagCompound)) continue;
                NBTTagCompound itemTag = (NBTTagCompound) itemTagBase;
                byte slot = itemTag.getByte("Slot");
                ItemStack stack = ItemStack.loadItemStackFromNBT(itemTag);

                if (slot == 0) {
                    inputStack = stack;
                } else if (slot == 1) {
                    fuelStack = stack;
                } else if (slot == 2) {
                    outputStack = stack;
                }
            }

            if (fuelProgress > 0) {
                String fuelBar = TTRenderBar.create(
                        String.format("Fuel %ds / %ds", (maxFuelTime - fuelTime) / 20, maxFuelTime / 20),
                        0xFFFF8800,
                        0xFFFFCC00,
                        1.0 - fuelProgress);
                currenttip.add(fuelBar);
            }

            if (smeltProgress > 0) {
                String cookBar = TTRenderBar.create(
                        String.format("Smelt %ds / %ds", smeltTime / 20, 200 / 20),
                        0xFFFF0000,
                        0xFF8B0000,
                        smeltProgress);
                currenttip.add(cookBar);
            }

            if (inputStack != null) {
                itemsText += TTRenderStack.create(inputStack);
            }

            if (fuelStack != null) {
                itemsText += TTRenderStack.create(fuelStack);
            }

            if (outputStack != null) {
                if (inputStack != null || fuelStack != null) {
                    itemsText += TTRenderProgressBar.create(0, 1);
                }
                itemsText += TTRenderStack.create(outputStack);
            }

            currenttip.add(itemsText);
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) te;
            tag.setInteger("MaxFuel", furnace.currentItemBurnTime);
        }
    }

}

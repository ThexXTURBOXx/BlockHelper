package mcp.mobius.waila.addons.projectzulu;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public final class HUDHandlerFlowerPot implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFlowerPot();

    private HUDHandlerFlowerPot() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return new ItemStack(Item.flowerPot.itemID, 1, 0);
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.flowerpot"))
            try {
                IInventory pot = (IInventory) accessor.getTileEntity();
                ItemStack flower = pot.getStackInSlot(0);
                if (flower != null)
                    currenttip.add(I18n.translate("hud.msg.flower") + ": " +
                                   DisplayUtil.itemDisplayNameShort(flower));
            } catch (Throwable ignored) {
            }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

}

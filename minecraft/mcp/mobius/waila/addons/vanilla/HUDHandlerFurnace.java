package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

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
        if (config.get("vanilla.furnace") && accessor.getBlockID() == Block.furnaceBurning.blockID) {
            int cookTime = accessor.getNBTData().getShort("CookTime");
            NBTTagList tag = accessor.getNBTData().getTagList("Items");

            ItemStack[] inv = new ItemStack[3];
            for (int i = 0; i < tag.tagCount(); ++i) {
                NBTBase subtagBase = tag.tagAt(i);
                if (!(subtagBase instanceof NBTTagCompound)) continue;
                NBTTagCompound subtag = (NBTTagCompound) subtagBase;
                ItemStack stack = ItemStack.loadItemStackFromNBT(subtag);
                inv[subtag.getByte("Slot")] = stack;
            }

            String renderStr = (inv[0] == null ? "" : getItemRenderString(inv[0]))
                               + (inv[1] == null ? "" : getItemRenderString(inv[1]))
                               + SpecialChars.getRenderString("waila.progress", cookTime, 200)
                               + getItemRenderString(inv[2]);

            currenttip.add(renderStr);
        }
    }

    private static String getItemRenderString(ItemStack stack) {
        boolean empty = stack == null;
        String id = (empty ? 0 : stack.getItem().itemID) + "";
        return SpecialChars.getRenderString("waila.stack",
                1, id, empty ? 1 : stack.stackSize, empty ? 0 : stack.getItemDamage(),
                !empty && stack.isItemEnchanted());
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

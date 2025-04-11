package mcp.mobius.waila.addons.vanillamc;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class HUDHandlerFurnace implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlockID() == Block.furnaceBurning.blockID) {
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
                               + SpecialChars.getRenderString("waila.progress", cookTime + "", "200")
                               + getItemRenderString(inv[2]);

            currenttip.add(renderStr);
        }

        return currenttip;
    }

    private static String getItemRenderString(ItemStack stack) {
        boolean empty = stack == null;
        String id = (empty ? 0 : stack.getItem().itemID) + "";
        return SpecialChars.getRenderString("waila.stack",
                "1", id, (empty ? 1 : stack.stackSize) + "", (empty ? 0 : stack.getItemDamage()) + "");
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }

    public static void register() {
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerFurnace(), TileEntityFurnace.class);
        ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerFurnace(), TileEntityFurnace.class);
    }
}

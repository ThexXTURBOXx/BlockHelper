package mcp.mobius.waila.addons.vanillamc;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
        int cookTime = accessor.getNBTData().getShort("CookTime");
        NBTTagList tag = accessor.getNBTData().getTagList("Items");

        String renderStr = "";
        {
            ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag.tagAt(0));
            String id = stack.getItem().itemID + "";
            renderStr += SpecialChars.getRenderString("waila.stack", "1", id, String.valueOf(stack.stackSize),
                    String.valueOf(stack.getItemDamage()));
        }
        {
            ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag.tagAt(1));
            String id = stack.getItem().itemID + "";
            renderStr += SpecialChars.getRenderString("waila.stack", "1", id, String.valueOf(stack.stackSize),
                    String.valueOf(stack.getItemDamage()));
        }

        renderStr += SpecialChars.getRenderString("waila.progress", String.valueOf(cookTime), String.valueOf(200));

        {
            ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag.tagAt(2));
            String id = stack.getItem().itemID + "";
            renderStr += SpecialChars.getRenderString("waila.stack", "1", id, String.valueOf(stack.stackSize),
                    String.valueOf(stack.getItemDamage()));
        }

        currenttip.add(renderStr);

        return currenttip;
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

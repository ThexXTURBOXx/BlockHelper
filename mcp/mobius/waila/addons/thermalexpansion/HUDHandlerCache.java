package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.InvocationTargetException;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerCache implements IDataProvider {

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("thermalexpansion.cache")) return;
        try {
            ItemStack storedItem = null;
            if (accessor.getNBTData().hasKey("Item"))
                storedItem = readItemStack(accessor.getNBTData().getCompoundTag("Item"));

            String name = currenttip.get(0);
            String color = "";
            if (name.startsWith("\u00a7"))
                color = name.substring(0, 2);

            if (storedItem != null) {
                String id = storedItem.getItem().itemID + "";
                name += String.format(" < " + SpecialChars.getRenderString("waila.stack", "1", id, "0",
                        String.valueOf(storedItem.getItemDamage())) + color + " %s >", storedItem.getDisplayName());
            } else
                name += " " + LangUtil.translateG("hud.msg.empty");

            currenttip.set(0, name);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("thermalexpansion.cache")) return;

        NBTTagCompound tag = accessor.getNBTData();
        ItemStack storedItem = null;
        if (tag.hasKey("Item"))
            storedItem = readItemStack(tag.getCompoundTag("Item"));

        int stored = 0;
        int maxStored = 0;
        if (tag.hasKey("Stored"))
            stored = tag.getInteger("Stored");
        if (tag.hasKey("MaxStored"))
            maxStored = tag.getInteger("MaxStored");

        if (storedItem != null) {
            currenttip.add("Stored: " + stored + "/" + maxStored);
        } else
            currenttip.add("Capacity: " + maxStored);
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
        try {
            tag.setInteger("MaxStored", (Integer) ThermalExpansionModule.TileCache_getMaxStored.invoke(te));
            tag.setInteger("Stored", (Integer) ThermalExpansionModule.TileCache_getStored.invoke(te));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ItemStack readItemStack(NBTTagCompound tag) {
        ItemStack is = new ItemStack(Item.itemsList[tag.getShort("id")]);
        is.stackSize = tag.getInteger("Count");
        is.setItemDamage(Math.max(0, tag.getShort("Damage")));
        if (tag.hasKey("tag")) {
            is.stackTagCompound = tag.getCompoundTag("tag");
        }

        return is;
    }

}

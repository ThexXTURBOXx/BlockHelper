package mcp.mobius.waila.addons.florasoma;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.florasoma.FloraSomaPlugin.FloraCropBlock;
import static mcp.mobius.waila.addons.florasoma.FloraSomaPlugin.FloraCropBlock_getCropItem;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerFloraSomaCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFloraSomaCrops();

    private HUDHandlerFloraSomaCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        try {
            Block b = accessor.getBlock();
            if (FloraCropBlock.isInstance(b)) {
                int meta = accessor.getMetadata();
                return new ItemStack((Integer) FloraCropBlock_getCropItem.invoke(b, meta), 1,
                        b.damageDropped(meta));
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            Block b = accessor.getBlock();
            if (FloraCropBlock.isInstance(b)) {
                int meta = accessor.getMetadata();
                currenttip.set(0, WHITE + new ItemStack((Integer) FloraCropBlock_getCropItem.invoke(b, meta), 1,
                        b.damageDropped(meta)).getDisplayName());
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
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

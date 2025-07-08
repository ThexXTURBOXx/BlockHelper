package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.ConstantRandom;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class StackDropFixer implements IDataProvider {

    public static final IDataProvider DEFAULT = new StackDropFixer();

    private final int metaOverride;

    private StackDropFixer() {
        this(-1);
    }

    private StackDropFixer(int metaOverride) {
        this.metaOverride = metaOverride;
    }

    public static StackDropFixer withMetaOverride(int metaOverride) {
        return new StackDropFixer(metaOverride);
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        Block b = accessor.getBlock();
        int meta = metaOverride < 0 ? accessor.getMetadata() : metaOverride;
        int id = b.idDropped(meta, ConstantRandom.INSTANCE, 0);
        return id == 0 ? null : new ItemStack(id, 1, b.damageDropped(meta));
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
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

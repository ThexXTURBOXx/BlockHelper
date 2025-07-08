package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.ConstantRandom;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_BlockHelper;

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
        int id = b.idDropped(meta, ConstantRandom.INSTANCE);
        return id == 0 || Item.itemsList[id] == null
                ? null : new ItemStack(id, 1, mod_BlockHelper.Accessor.damageDropped(b, meta));
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

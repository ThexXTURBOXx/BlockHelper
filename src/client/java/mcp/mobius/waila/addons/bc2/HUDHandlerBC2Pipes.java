package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.Pipe_itemID;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.TileGenericPipe_pipe;

public final class HUDHandlerBC2Pipes implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC2Pipes();

    private HUDHandlerBC2Pipes() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        try {
            Object pipe = TileGenericPipe_pipe.get(accessor.getTileEntity());
            return new ItemStack(Pipe_itemID.getInt(pipe), 1, accessor.getMetadata());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }

        return null;
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

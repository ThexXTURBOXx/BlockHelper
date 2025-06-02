package mcp.mobius.waila.api;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

/**
 * Callback class interface used to provide Block/TileEntity tooltip information to Waila.</br>
 * All methods in this interface shouldn't be called by the implementing mod. An instance of the class is to be
 * registered to Waila via the {@link IRegistrar} instance provided in the original registration callback method
 * (cf. {@link IRegistrar} documentation for more information).
 */
public interface IDataProvider {

    /**
     * Server-side callback to provide a custom synchronization NBTTagCompound.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerNBTProvider} server
     * and client side.
     *
     * @param te  The TileEntity targeted for synchronization.
     * @param tag Current synchronization tag (might have been processed by other providers and might be processed
     *            by other providers).
     */
    void appendServerData(TileEntity te, NBTTagCompound tag,
                          IServerDataAccessor accessor, IPluginConfig config);

}

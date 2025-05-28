package mcp.mobius.waila.api;

import net.minecraft.src.mod_BlockHelper;

/**
 * Main registration interface. An instance will be provided to registered plugins automatically.
 * For more info, see {@link mod_BlockHelper#registerPlugin(IWailaPlugin)}.</br>
 * If not specified otherwise, all the registration methods taking a class can take classes as well as interfaces.
 * Waila will do a lookup using instanceof on the registered classes, meaning that if all your targets inherit one
 * interface, you only need to specify it to cover the whole hierarchy.</br>
 * For the registration of blocks, both Blocks and TileEntities are accepted.<p>
 * For the configuration keys:</br>
 * modName refers to a String used for display in Waila's config panel.</br>
 * keyName refers to a unique key used internally for config query (cf. {@link IPluginConfig}).</br>
 * Those keys are shared across Waila, keep them unique!</br>
 */
public interface IRegistrar {

    /* Add a config option in the section modName with access key keyName */
    void addSyncedConfig(String modName, String keyName);

    void addSyncedConfig(String modName, String keyName, boolean defValue);

    /* Registering an NBT Provider provides a way to override the default "writeToNBT" way of doing things. */
    void registerNBTProvider(IDataProvider dataProvider, Class<?> block);

    /* Registering an NBT Provider provides a way to override the default "writeToNBT" way of doing things. */
    void registerNBTProvider(IEntityProvider dataProvider, Class<?> entity);

}

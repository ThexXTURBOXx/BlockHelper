package mcp.mobius.waila.api;

/**
 * Main registration interface. An instance will be provided to registered plugins automatically.
 * For more info, see {@link mcp.mobius.waila.mod_BlockHelper#registerPlugin(IWailaPlugin)}.</br>
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

    /* Add a config option in the section modName with translationKey for display text and access key keyName */
    void addConfig(String modName, String keyName, String translationKey);

    void addConfig(String modName, String keyName, String translationKey, boolean defValue);

    void addSyncedConfig(String modName, String keyName, String translationKey);

    void addSyncedConfig(String modName, String keyName, String translationKey, boolean defValue);

    void addConfig(String modName, String keyName);

    void addConfig(String modName, String keyName, boolean defValue);

    void addSyncedConfig(String modName, String keyName);

    void addSyncedConfig(String modName, String keyName, boolean defValue);

    /* Register a stack overrider for the given blockID */
    void registerStackProvider(IDataProvider dataProvider, Class<?> block);

    /* Same thing, but works on a class hierarchy instead */
    void registerHeadProvider(IDataProvider dataProvider, Class<?> block);

    void registerBodyProvider(IDataProvider dataProvider, Class<?> block);

    void registerTailProvider(IDataProvider dataProvider, Class<?> block);

    /* Registering an NBT Provider provides a way to override the default "writeToNBT" way of doing things. */
    void registerNBTProvider(IDataProvider dataProvider, Class<?> block);

    /* Register a stack overrider for the given entity */
    void registerStackProvider(IEntityProvider dataProvider, Class<?> entity);

    /* Entity text registration methods */
    void registerHeadProvider(IEntityProvider dataProvider, Class<?> entity);

    void registerBodyProvider(IEntityProvider dataProvider, Class<?> entity);

    void registerTailProvider(IEntityProvider dataProvider, Class<?> entity);

    void registerOverrideEntityProvider(IEntityProvider dataProvider, Class<?> entity);

    /* Registering an NBT Provider provides a way to override the default "writeToNBT" way of doing things. */
    void registerNBTProvider(IEntityProvider dataProvider, Class<?> entity);

    /* FMP Providers */
    void registerHeadProvider(IFMPProvider dataProvider, String name);

    void registerBodyProvider(IFMPProvider dataProvider, String name);

    void registerTailProvider(IFMPProvider dataProvider, String name);

    /* The block decorators */
    void registerDecorator(IBlockDecorator decorator, Class<?> block);

    void registerDecorator(IFMPDecorator decorator, String name);

    /* Register a crop info provider for the given block */
    void registerCropProvider(ICropProvider cropProvider, Class<?> block);

    /* Register a tank info provider for the given object */
    void registerTankProvider(ITankProvider tankProvider, Class<?> object);

    void registerTooltipRenderer(String name, ITooltipRenderer renderer);

}

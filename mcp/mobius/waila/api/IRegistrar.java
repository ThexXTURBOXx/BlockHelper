package mcp.mobius.waila.api;

/**
 * Main registration interface. An instance will be provided to a method specified in an IMC msg formatted as
 * follows:<br>
 * FMLInterModComms.sendMessage("Waila", "register", "fully.qualified.path.to.registration.method");<br>
 * The registration method need to follow this signature<br>
 * public static void callbackRegister({@link IRegistrar} registrar)<p>
 * If not specified otherwise, all the registration methods taking a class can take classes as well as interfaces.
 * Waila will do a lookup using instanceof on the registered classes, meaning that if all your targets inherit one
 * interface, you only need
 * to specify it to cover the whole hierarchy.<br>
 * For the registration of blocks, both Blocks and TileEntities are accepted.<p>
 * For the configuration keys :<br>
 * modname refers to a String used for display in Waila's config panel.<br>
 * keyname refers to a unique key used internally for config query (cf {@link IPluginConfig}). Those keys are
 * shared across Waila, keep them unique !<br>
 *
 * @author ProfMobius
 */
public interface IRegistrar {

    /* Add a config option in the section modname with displayed text configtext and access key keyname */
    void addConfig(String modname, String keyname, String configtext);

    void addConfig(String modname, String keyname, String configtext, boolean defvalue);

    void addSyncedConfig(String modname, String keyname, String configtext);

    void addSyncedConfig(String modname, String keyname, String configtext, boolean defvalue);

    void addConfig(String modname, String keyname);

    void addConfig(String modname, String keyname, boolean defvalue);

    void addSyncedConfig(String modname, String keyname);

    void addSyncedConfig(String modname, String keyname, boolean defvalue);

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

    void registerTooltipRenderer(String name, ITooltipRenderer renderer);

}

package mcp.mobius.waila.api;

/**
 * Main interface used for Waila plugins. Provides a valid instance of {@link IRegistrar}.
 */
public interface IWailaPlugin {

    /**
     * @param registrar - An instance of {@link IRegistrar} to register your providers with.
     */
    void registerCommon(IRegistrar registrar);

    /**
     * @param registrar - An instance of {@link IRegistrar} to register your providers with.
     */
    void registerClient(IRegistrar registrar);

}

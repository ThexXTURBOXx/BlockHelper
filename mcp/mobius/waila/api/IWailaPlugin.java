package mcp.mobius.waila.api;

/**
 * Main interface used for Waila plugins.
 * An instance of {@link IRegistrar} will be provided to registered plugins automatically.
 * For more info, see {@link mcp.mobius.waila.mod_BlockHelper#registerPlugin(IWailaPlugin)}.</br>
 * The following call order is guaranteed:
 * {@link #shouldRegister()} => if it returns {@code true}:
 * {@link #registerCommon(IRegistrar)}, then lastly {@link #registerClient(IRegistrar)}.
 */
public interface IWailaPlugin {

    /**
     * Determines whether this plugin should be registered at all (checks for mod prerequisites etc.).
     * If this function returns {@code false}, both register functions are not called at all.
     *
     * @return whether this plugin should be registered.
     */
    boolean shouldRegister();

    /**
     * Registration code for both client and server (synced configs, NBT providers, etc.).
     *
     * @param registrar - An instance of {@link IRegistrar} to register your providers with.
     */
    void registerCommon(IRegistrar registrar);

    /**
     * Registration code for both client and server (client-only configs, tooltip modifiers, etc.).
     *
     * @param registrar - An instance of {@link IRegistrar} to register your providers with.
     */
    void registerClient(IRegistrar registrar);

}

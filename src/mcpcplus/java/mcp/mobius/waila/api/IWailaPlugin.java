package mcp.mobius.waila.api;

import cpw.mods.fml.common.Side;
import net.minecraft.server.mod_BlockHelper;

/**
 * Main interface used for Waila plugins.
 * An instance of {@link IRegistrar} will be provided to registered plugins automatically.
 * For more info, see {@link mod_BlockHelper#registerPlugin(IWailaPlugin)}.</br>
 * The following call order is guaranteed:
 * {@link #shouldRegister()} => if it returns {@code true}:
 * {@link #register(IRegistrar, Side)}.
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
     * Registration code for the plugin. The current side is passed as a parameter.
     * Some handlers should be registered on both client and server (synced configs, NBT providers, etc.),
     * whilst for others it is sufficient to register them only client-side (client-only configs,
     * tooltip modifiers, etc.). There is (usually) no harm in registering those on both sides, though!
     *
     * @param registrar An instance of {@link IRegistrar} to register your providers with.
     * @param side      The side that the plugin is registered on currently.
     */
    void register(IRegistrar registrar, Side side);

}

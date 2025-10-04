package mcp.mobius.waila.api;

import net.minecraft.entity.Entity;

/**
 * UNSTABLE (!!!) callback class interface used to provide Entity tooltip information to Waila.</br>
 * All methods in this interface shouldn't be called by the implementing mod. An instance of the class is to be
 * registered to Waila via the {@link IRegistrar} instance provided in the original registration callback method
 * (cf. {@link IRegistrar} documentation for more information).<br/>
 * Functions may be added at any point to this interface, which inherently asks you to update your code!
 */
public interface IAdvEntityProvider extends IEntityProvider {

    /**
     * Client-side callback to determine if there is extended information to display for the Body section.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerBodyProvider} client
     * side.</br>
     * If this returns {@code true}, either a "Hold <KEY> for more info" prompt will be displayed or
     * {@link #modifyAdvancedBody} will be called if the configured key is being held.
     *
     * @param entity   Current Entity scanned.
     * @param accessor Contains most of the relevant information about the current environment.
     * @param config   Current configuration of Waila.
     * @return {@code true} if advanced body information is available, {@code false} otherwise.
     */
    boolean hasAdvancedBody(Entity entity, IEntityAccessor accessor, IPluginConfig config);

    /**
     * Client-side callback used to modify extended information to display for the Body section.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerBodyProvider} client
     * side AND {@link #hasAdvancedBody} returns true AND the player is holding the configured key for advanced
     * tooltips. Otherwise, this function is not being called.
     *
     * @param entity     Current Entity scanned.
     * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be
     *                   processed by other providers).
     * @param accessor   Contains most of the relevant information about the current environment.
     * @param config     Current configuration of Waila.
     */
    void modifyAdvancedBody(Entity entity, ITaggedList<String, String> currenttip,
                            IEntityAccessor accessor, IPluginConfig config);

}

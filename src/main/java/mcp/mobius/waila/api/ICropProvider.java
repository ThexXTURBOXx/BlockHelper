package mcp.mobius.waila.api;

import java.util.List;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import net.minecraft.item.ItemStack;

/**
 * Callback class interface used to provide crop growth information to Waila.</br>
 * All methods in this interface shouldn't be called by the implementing mod. An instance of the class is to be
 * registered to Waila via the {@link IRegistrar} instance provided in the original registration callback method
 * (cf. {@link IRegistrar} documentation for more information).
 * A default implementation is available as part of {@link DefaultCropProvider},
 * which can either be extended used directly - if applicable.
 */
public interface ICropProvider {

    /**
     * Prefix for growth stages. Should be translated using e.g., {@link mcp.mobius.waila.utils.I18n}!
     */
    String GROWTH_STATE = "hud.msg.growth";

    /**
     * Prefix for mature crops. Should be translated using e.g., {@link mcp.mobius.waila.utils.I18n}!
     */
    String MATURE = "hud.msg.mature";

    /**
     * Prefix for ripe crops. Most crops do not ripen and should use {@link #MATURE} instead!
     * Should be translated using e.g., {@link mcp.mobius.waila.utils.I18n}!
     */
    String RIPE = "hud.msg.ripe";

    /**
     * Client-side callback used to provide or override current crop behavior.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerCropProvider} client side.
     *
     * @param itemStack Current block scanned, in ItemStack form.
     * @param accessor  Contains most of the relevant information about the current environment.
     * @param config    Current configuration of Waila.
     * @return The current growth details for the tooltip.
     */
    List<String> getGrowthDetails(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config);

}

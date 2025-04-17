package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;

/**
 * Callback class interface used to provide FMP tooltip information to Waila.<br>
 * All methods in this interface shouldn't to be called by the implementing mod. An instance of the class is to be
 * registered to Waila via the {@link IRegistrar} instance provided in the original registration callback method
 * (cf. {@link IRegistrar} documentation for more information).
 *
 * @author ProfMobius
 */
public interface IFMPProvider {

    /**
     * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerHeadProvider} client
     * side.
     *
     * @param itemStack  Current block scanned, in ItemStack form.
     * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be
     *                   processed by other providers).
     * @param accessor   Contains most of the relevant information about the current environment.
     * @param config     Current configuration of Waila.
     */
    void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                    IFMPAccessor accessor, IPluginConfig config);

    /**
     * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerBodyProvider} client
     * side.
     *
     * @param itemStack  Current block scanned, in ItemStack form.
     * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be
     *                   processed by other providers).
     * @param accessor   Contains most of the relevant information about the current environment.
     * @param config     Current configuration of Waila.
     */
    void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                    IFMPAccessor accessor, IPluginConfig config);

    /**
     * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerTailProvider} client
     * side.
     *
     * @param itemStack  Current block scanned, in ItemStack form.
     * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be
     *                   processed by other providers).
     * @param accessor   Contains most of the relevant information about the current environment.
     * @param config     Current configuration of Waila.
     */
    void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                    IFMPAccessor accessor, IPluginConfig config);

}

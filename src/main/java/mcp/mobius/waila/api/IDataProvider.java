package mcp.mobius.waila.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Callback class interface used to provide Block/TileEntity tooltip information to Waila.</br>
 * All methods in this interface shouldn't be called by the implementing mod. An instance of the class is to be
 * registered to Waila via the {@link IRegistrar} instance provided in the original registration callback method
 * (cf. {@link IRegistrar} documentation for more information).<br/>
 * If you want slightly more possibilities, use the UNSTABLE (!!!) {@link IDataProvider} interface.
 */
public interface IDataProvider {

    /**
     * Client-side callback used to override the default Waila lookup system.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerStackProvider}.</br>
     *
     * @param accessor Contains most of the relevant information about the current environment.
     * @param config   Current configuration of Waila.
     * @return null if override is not required, an ItemStack otherwise.
     */
    ItemStack getStack(IDataAccessor accessor, IPluginConfig config);

    /**
     * Client-side callback used to modify the lines of the three sections of the tooltip (Head, Body, Tail).</br>
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
                    IDataAccessor accessor, IPluginConfig config);

    /**
     * Client-side callback used to modify the lines of the three sections of the tooltip (Head, Body, Tail).</br>
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
                    IDataAccessor accessor, IPluginConfig config);

    /**
     * Client-side callback used to modify the lines of the three sections of the tooltip (Head, Body, Tail).</br>
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
                    IDataAccessor accessor, IPluginConfig config);

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

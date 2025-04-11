package mcp.mobius.waila.handlers;

import java.util.List;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IConfigHandler;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.impl.DataAccessorFMP;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerFMP implements IDataProvider {

    @Override
    public ItemStack getWailaStack(IDataAccessor accessor, IConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (ModuleRegistrar.instance().hasHeadFMPProviders(id)) {
                DataAccessorFMP.instance.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        ModuleRegistrar.instance().getHeadFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        currenttip = provider.getWailaHead(itemStack, currenttip, DataAccessorFMP.instance, config);
                }
            }
        }

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (ModuleRegistrar.instance().hasBodyFMPProviders(id)) {
                DataAccessorFMP.instance.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        ModuleRegistrar.instance().getBodyFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        currenttip = provider.getWailaBody(itemStack, currenttip, DataAccessorFMP.instance, config);
                }
            }
        }

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IDataAccessor accessor, IConfigHandler config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (ModuleRegistrar.instance().hasTailFMPProviders(id)) {
                DataAccessorFMP.instance.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        ModuleRegistrar.instance().getTailFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        currenttip = provider.getWailaTail(itemStack, currenttip, DataAccessorFMP.instance, config);
                }
            }
        }

        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
        return tag;
    }

    public static void register() {
        Class<?> BlockMultipart;
        try {
            BlockMultipart = Class.forName("codechicken.multipart.BlockMultipart");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Class not found. ", e);
            return;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Unhandled exception.", t);
            return;
        }

        ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerFMP(), BlockMultipart);
        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerFMP(), BlockMultipart);
        ModuleRegistrar.instance().registerTailProvider(new HUDHandlerFMP(), BlockMultipart);
        ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerFMP(), BlockMultipart);

        mod_BlockHelper.LOG.log(Level.INFO, "Forge Multipart found and dedicated handler registered");

    }
}

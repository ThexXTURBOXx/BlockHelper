package mcp.mobius.waila.addons.core;

import java.util.List;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.DataAccessorFMP;
import mcp.mobius.waila.api.impl.WailaRegistrar;
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
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (WailaRegistrar.instance().hasHeadFMPProviders(id)) {
                DataAccessorFMP.INSTANCE.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        WailaRegistrar.instance().getHeadFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        provider.modifyHead(itemStack, currenttip, DataAccessorFMP.INSTANCE, config);
                }
            }
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (WailaRegistrar.instance().hasBodyFMPProviders(id)) {
                DataAccessorFMP.INSTANCE.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        WailaRegistrar.instance().getBodyFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        provider.modifyBody(itemStack, currenttip, DataAccessorFMP.INSTANCE, config);
                }
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (WailaRegistrar.instance().hasTailFMPProviders(id)) {
                DataAccessorFMP.INSTANCE.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id);

                for (List<IFMPProvider> providersList :
                        WailaRegistrar.instance().getTailFMPProviders(id).values()) {
                    for (IFMPProvider provider : providersList)
                        provider.modifyTail(itemStack, currenttip, DataAccessorFMP.INSTANCE, config);
                }
            }
        }
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
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

        WailaRegistrar.instance().registerHeadProvider(new HUDHandlerFMP(), BlockMultipart);
        WailaRegistrar.instance().registerBodyProvider(new HUDHandlerFMP(), BlockMultipart);
        WailaRegistrar.instance().registerTailProvider(new HUDHandlerFMP(), BlockMultipart);
        WailaRegistrar.instance().registerNBTProvider(new HUDHandlerFMP(), BlockMultipart);

        mod_BlockHelper.LOG.log(Level.INFO, "Forge Multipart found and dedicated handler registered");

    }
}

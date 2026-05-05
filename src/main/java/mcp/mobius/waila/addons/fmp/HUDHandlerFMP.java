package mcp.mobius.waila.addons.fmp;

import java.util.List;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.DataAccessorFMP;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public final class HUDHandlerFMP implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFMP();

    private HUDHandlerFMP() {
    }

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
        DataAccessorFMP.INSTANCE.clear();
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
        DataAccessorFMP.INSTANCE.clear();
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
        DataAccessorFMP.INSTANCE.clear();
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

}

package mcp.mobius.waila.handlers;

import java.util.List;
import java.util.logging.Level;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IFMPDecorator;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.impl.DataAccessorFMP;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class DecoratorFMP implements IBlockDecorator {

    @Override
    public void decorateBlock(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        NBTTagList list = accessor.getNBTData().getTagList("parts");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase subtagBase = list.tagAt(i);
            if (!(subtagBase instanceof NBTTagCompound)) continue;
            NBTTagCompound subtag = (NBTTagCompound) subtagBase;
            String id = subtag.getString("id");

            if (WailaRegistrar.instance().hasFMPDecorator(id)) {
                DataAccessorFMP.INSTANCE.set(accessor.getWorld(), accessor.getPlayer(), accessor.getPosition(),
                        subtag, id, accessor.getRenderingPosition(), accessor.getPartialFrame());

                for (List<IFMPDecorator> providersList : WailaRegistrar.instance().getFMPDecorators(id).values())
                    for (IFMPDecorator provider : providersList)
                        provider.decorateBlock(itemStack, DataAccessorFMP.INSTANCE, config);
            }
        }
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

        WailaRegistrar.instance().registerDecorator(new DecoratorFMP(), BlockMultipart);
    }

}

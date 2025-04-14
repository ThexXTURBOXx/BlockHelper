package mcp.mobius.waila.addons.core;

import java.util.List;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IFMPDecorator;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.impl.DataAccessorFMP;
import mcp.mobius.waila.api.impl.WailaRegistrar;
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

}

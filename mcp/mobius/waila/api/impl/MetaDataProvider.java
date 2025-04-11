package mcp.mobius.waila.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.cbcore.Layout;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.network.Packet0x01TERequest;
import mcp.mobius.waila.network.Packet0x03EntRequest;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MetaDataProvider {

    private final Map<Integer, List<IDataProvider>> headBlockProviders = new TreeMap<Integer,
            List<IDataProvider>>();
    private final Map<Integer, List<IDataProvider>> bodyBlockProviders = new TreeMap<Integer,
            List<IDataProvider>>();
    private final Map<Integer, List<IDataProvider>> tailBlockProviders = new TreeMap<Integer,
            List<IDataProvider>>();

    private final Map<Integer, List<IEntityProvider>> headEntityProviders = new TreeMap<Integer,
            List<IEntityProvider>>();
    private final Map<Integer, List<IEntityProvider>> bodyEntityProviders = new TreeMap<Integer,
            List<IEntityProvider>>();
    private final Map<Integer, List<IEntityProvider>> tailEntityProviders = new TreeMap<Integer,
            List<IEntityProvider>>();

    private final Class<?> prevBlock = null;
    private final Class<?> prevTile = null;

    public ItemStack identifyBlockHighlight(World world, EntityPlayer player, MovingObjectPosition mop,
                                            DataAccessorCommon accessor) {
        Block block = accessor.getBlock();
        int blockID = accessor.getBlockID();

        if (ModuleRegistrar.instance().hasStackProviders(block)) {
            for (List<IDataProvider> providerList : ModuleRegistrar.instance().getStackProviders(block).values()) {
                for (IDataProvider dataProvider : providerList) {
                    try {
                        ItemStack retval = dataProvider.getWailaStack(accessor, ConfigHandler.instance());
                        if (retval != null)
                            return retval;
                    } catch (Throwable e) {
                        WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), null);
                    }
                }
            }
        }
        return null;
    }

    public ITaggedList<String, String> handleBlockTextData(ItemStack itemStack, World world, EntityPlayer player,
                                                           MovingObjectPosition mop, DataAccessorCommon accessor,
                                                           ITaggedList<String, String> currenttip, Layout layout) {
        Block block = accessor.getBlock();

        if (accessor.getTileEntity() != null && mod_BlockHelper.INSTANCE.serverPresent && accessor.isTimeElapsed(250) && ConfigHandler.instance().showTooltip()) {
            accessor.resetTimer();
            HashSet<String> keys = new HashSet<String>();
            if (ModuleRegistrar.instance().hasNBTProviders(block) || ModuleRegistrar.instance().hasNBTProviders(accessor.getTileEntity()))
                WailaPacketHandler.sendPacketToServer(new Packet0x01TERequest(accessor.getTileEntity(), keys));

        } else if (accessor.getTileEntity() != null && !mod_BlockHelper.INSTANCE.serverPresent && accessor.isTimeElapsed(250) && ConfigHandler.instance().showTooltip()) {

            try {
                NBTTagCompound tag = new NBTTagCompound();
                accessor.getTileEntity().writeToNBT(tag);
                accessor.setNBTData(tag);
            } catch (Exception e) {
                WailaExceptionHandler.handleErr(e, this.getClass().getName(), null);
            }
        }

        headBlockProviders.clear();
        bodyBlockProviders.clear();
        tailBlockProviders.clear();

        /* Lookup by class (for blocks)*/
        if (layout == Layout.HEADER && ModuleRegistrar.instance().hasHeadProviders(block))
            headBlockProviders.putAll(ModuleRegistrar.instance().getHeadProviders(block));

        else if (layout == Layout.BODY && ModuleRegistrar.instance().hasBodyProviders(block))
            bodyBlockProviders.putAll(ModuleRegistrar.instance().getBodyProviders(block));

        else if (layout == Layout.FOOTER && ModuleRegistrar.instance().hasTailProviders(block))
            tailBlockProviders.putAll(ModuleRegistrar.instance().getTailProviders(block));


        /* Lookup by class (for tileentities)*/
        if (layout == Layout.HEADER && ModuleRegistrar.instance().hasHeadProviders(accessor.getTileEntity()))
            headBlockProviders.putAll(ModuleRegistrar.instance().getHeadProviders(accessor.getTileEntity()));

        else if (layout == Layout.BODY && ModuleRegistrar.instance().hasBodyProviders(accessor.getTileEntity()))
            bodyBlockProviders.putAll(ModuleRegistrar.instance().getBodyProviders(accessor.getTileEntity()));

        else if (layout == Layout.FOOTER && ModuleRegistrar.instance().hasTailProviders(accessor.getTileEntity()))
            tailBlockProviders.putAll(ModuleRegistrar.instance().getTailProviders(accessor.getTileEntity()));

        /* Apply all collected providers */
        if (layout == Layout.HEADER)
            for (List<IDataProvider> providersList : headBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaHead(itemStack, currenttip, accessor,
                                ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }

        if (layout == Layout.BODY)
            for (List<IDataProvider> providersList : bodyBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaBody(itemStack, currenttip, accessor,
                                ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }
        if (layout == Layout.FOOTER)
            for (List<IDataProvider> providersList : tailBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaTail(itemStack, currenttip, accessor,
                                ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }
        return currenttip;
    }

    public ITaggedList<String, String> handleEntityTextData(Entity entity, World world, EntityPlayer player,
                                                            MovingObjectPosition mop, DataAccessorCommon accessor,
                                                            ITaggedList<String, String> currenttip, Layout layout) {

        if (accessor.getEntity() != null && mod_BlockHelper.INSTANCE.serverPresent && accessor.isTimeElapsed(250)) {
            accessor.resetTimer();
            HashSet<String> keys = new HashSet<String>();
            if (ModuleRegistrar.instance().hasNBTEntityProviders(accessor.getEntity()))
                WailaPacketHandler.sendPacketToServer(new Packet0x03EntRequest(accessor.getEntity(), keys));
        } else if (accessor.getEntity() != null && !mod_BlockHelper.INSTANCE.serverPresent && accessor.isTimeElapsed(250)) {

            try {
                NBTTagCompound tag = new NBTTagCompound();
                accessor.getEntity().writeToNBT(tag);
                accessor.remoteNbt = tag;
            } catch (Exception e) {
                WailaExceptionHandler.handleErr(e, this.getClass().getName(), null);
            }
        }

        headEntityProviders.clear();
        bodyEntityProviders.clear();
        tailEntityProviders.clear();

        /* Lookup by class (for entities)*/
        if (layout == Layout.HEADER && ModuleRegistrar.instance().hasHeadEntityProviders(entity))
            headEntityProviders.putAll(ModuleRegistrar.instance().getHeadEntityProviders(entity));

        else if (layout == Layout.BODY && ModuleRegistrar.instance().hasBodyEntityProviders(entity))
            bodyEntityProviders.putAll(ModuleRegistrar.instance().getBodyEntityProviders(entity));

        else if (layout == Layout.FOOTER && ModuleRegistrar.instance().hasTailEntityProviders(entity))
            tailEntityProviders.putAll(ModuleRegistrar.instance().getTailEntityProviders(entity));

        /* Apply all collected providers */
        if (layout == Layout.HEADER)
            for (List<IEntityProvider> providersList : headEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaHead(entity, currenttip, accessor, ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }

        if (layout == Layout.BODY)
            for (List<IEntityProvider> providersList : bodyEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaBody(entity, currenttip, accessor, ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }

        if (layout == Layout.FOOTER)
            for (List<IEntityProvider> providersList : tailEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        currenttip = dataProvider.getWailaTail(entity, currenttip, accessor, ConfigHandler.instance());
                    } catch (Throwable e) {
                        currenttip = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), currenttip);
                    }
            }

        return currenttip;
    }
}

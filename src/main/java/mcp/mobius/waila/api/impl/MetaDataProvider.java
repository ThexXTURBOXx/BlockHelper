package mcp.mobius.waila.api.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.network.Packet0x01TileRequest;
import mcp.mobius.waila.network.Packet0x02EntRequest;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;

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

    public void handleBlockTextData(ItemStack itemStack, DataAccessorCommon accessor,
                                    ITaggedList<String, String> currenttip, TooltipPosition tooltipPosition) {
        Block block = accessor.getBlock();
        WailaRegistrar registrar = WailaRegistrar.instance();

        if (accessor.getTileEntity() != null && accessor.isTimeElapsed(250) && PluginConfig.instance().showTooltip()) {
            if (mod_BlockHelper.INSTANCE.serverPresent) {
                accessor.resetTimer();
                if (registrar.hasNBTProviders(block) || registrar.hasNBTProviders(accessor.getTileEntity()))
                    WailaPacketHandler.sendPacketToServer(new Packet0x01TileRequest(accessor.getTileEntity()));
            } else {
                try {
                    NBTTagCompound tag = new NBTTagCompound();
                    accessor.getTileEntity().writeToNBT(tag);
                    accessor.setNBTData(tag);
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
                }
            }
        }

        headBlockProviders.clear();
        bodyBlockProviders.clear();
        tailBlockProviders.clear();

        /* Lookup by class (for blocks)*/
        if (tooltipPosition == TooltipPosition.HEADER && registrar.hasHeadProviders(block))
            headBlockProviders.putAll(registrar.getHeadProviders(block));

        else if (tooltipPosition == TooltipPosition.BODY && registrar.hasBodyProviders(block))
            bodyBlockProviders.putAll(registrar.getBodyProviders(block));

        else if (tooltipPosition == TooltipPosition.FOOTER && registrar.hasTailProviders(block))
            tailBlockProviders.putAll(registrar.getTailProviders(block));


        /* Lookup by class (for tileentities)*/
        if (tooltipPosition == TooltipPosition.HEADER && registrar.hasHeadProviders(accessor.getTileEntity()))
            headBlockProviders.putAll(registrar.getHeadProviders(accessor.getTileEntity()));

        else if (tooltipPosition == TooltipPosition.BODY && registrar.hasBodyProviders(accessor.getTileEntity()))
            bodyBlockProviders.putAll(registrar.getBodyProviders(accessor.getTileEntity()));

        else if (tooltipPosition == TooltipPosition.FOOTER && registrar.hasTailProviders(accessor.getTileEntity()))
            tailBlockProviders.putAll(registrar.getTailProviders(accessor.getTileEntity()));

        /* Apply all collected providers */
        if (tooltipPosition == TooltipPosition.HEADER)
            for (List<IDataProvider> providersList : headBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyHead(itemStack, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }

        if (tooltipPosition == TooltipPosition.BODY)
            for (List<IDataProvider> providersList : bodyBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyBody(itemStack, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }
        if (tooltipPosition == TooltipPosition.FOOTER)
            for (List<IDataProvider> providersList : tailBlockProviders.values()) {
                for (IDataProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyTail(itemStack, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }
    }

    public void handleEntityTextData(Entity entity, DataAccessorCommon accessor,
                                     ITaggedList<String, String> currenttip, TooltipPosition tooltipPosition) {
        WailaRegistrar registrar = WailaRegistrar.instance();

        if (accessor.getEntity() != null && accessor.isTimeElapsed(250) && PluginConfig.instance().showTooltip()) {
            if (mod_BlockHelper.INSTANCE.serverPresent) {
                accessor.resetTimer();
                if (registrar.hasNBTEntityProviders(accessor.getEntity()))
                    WailaPacketHandler.sendPacketToServer(new Packet0x02EntRequest(accessor.getEntity()));
            } else {
                try {
                    NBTTagCompound tag = new NBTTagCompound();
                    accessor.getEntity().writeToNBT(tag);
                    accessor.setNBTData(tag);
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), currenttip);
                }
            }
        }

        headEntityProviders.clear();
        bodyEntityProviders.clear();
        tailEntityProviders.clear();

        /* Lookup by class (for entities)*/
        if (tooltipPosition == TooltipPosition.HEADER && registrar.hasHeadEntityProviders(entity))
            headEntityProviders.putAll(registrar.getHeadEntityProviders(entity));

        else if (tooltipPosition == TooltipPosition.BODY && registrar.hasBodyEntityProviders(entity))
            bodyEntityProviders.putAll(registrar.getBodyEntityProviders(entity));

        else if (tooltipPosition == TooltipPosition.FOOTER && registrar.hasTailEntityProviders(entity))
            tailEntityProviders.putAll(registrar.getTailEntityProviders(entity));

        /* Apply all collected providers */
        if (tooltipPosition == TooltipPosition.HEADER)
            for (List<IEntityProvider> providersList : headEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyHead(entity, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }

        if (tooltipPosition == TooltipPosition.BODY)
            for (List<IEntityProvider> providersList : bodyEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyBody(entity, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }

        if (tooltipPosition == TooltipPosition.FOOTER)
            for (List<IEntityProvider> providersList : tailEntityProviders.values()) {
                for (IEntityProvider dataProvider : providersList)
                    try {
                        dataProvider.modifyTail(entity, currenttip, accessor, PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, dataProvider.getClass(), currenttip);
                    }
            }
    }

}

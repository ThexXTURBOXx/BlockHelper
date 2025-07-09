package mcp.mobius.waila.overlay;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.event.ClientFirstTickInWorldEvent;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.TipList;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class WailaTickHandler implements ITickHandler {

    private Tooltip tooltip;
    private final MetaDataProvider handler = new MetaDataProvider();
    private final ITaggedList<String, String> currenttip = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipHead = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipBody = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipTail = new TipList<String, String>();
    private boolean firstTick = true;

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> enumSet, Object... objects) {
        if (enumSet.contains(TickType.RENDER))
            OverlayRenderer.renderOverlay(tooltip);

        if (enumSet.contains(TickType.CLIENT))
            clientTick();
    }

    private void clientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;

        if (world == null || player == null) {
            resetAll();
            return;
        }

        if (firstTick) {
            ModIdentification.init();
            FixDetector.detectFixes(mc);
            mod_BlockHelper.UPDATER.notifyUpdater(mc);
            MinecraftForge.EVENT_BUS.post(new ClientFirstTickInWorldEvent(mc));
            firstTick = false;
        }

        RayTracing.instance().fire();
        MovingObjectPosition target = RayTracing.instance().getTarget();

        if (target != null && target.typeOfHit == EnumMovingObjectType.TILE) {
            DataAccessorCommon accessor = DataAccessorCommon.INSTANCE;
            accessor.set(world, player, target);
            ItemStack targetStack = RayTracing.instance().getTargetStack();    // Here we get either the proper
            // stack or the override

            if (targetStack != null) {
                this.currenttip.clear();
                this.currenttipHead.clear();
                this.currenttipBody.clear();
                this.currenttipTail.clear();

                handler.handleBlockTextData(targetStack, accessor, currenttipHead, TooltipPosition.HEADER);
                handler.handleBlockTextData(targetStack, accessor, currenttipBody, TooltipPosition.BODY);
                handler.handleBlockTextData(targetStack, accessor, currenttipTail, TooltipPosition.FOOTER);

                if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHIFTBLOCK, false) && !currenttipBody.isEmpty() && !accessor.getPlayer().isSneaking()) {
                    currenttipBody.clear();
                    currenttipBody.add(ITALIC + "Press shift for more data");
                }

                this.currenttip.addAll(this.currenttipHead);
                this.currenttip.addAll(this.currenttipBody);
                this.currenttip.addAll(this.currenttipTail);

                this.tooltip = new Tooltip(this.currenttip, targetStack, true);
            }
        } else if (target != null && target.typeOfHit == EnumMovingObjectType.ENTITY) {
            DataAccessorCommon accessor = DataAccessorCommon.INSTANCE;
            accessor.set(world, player, target);

            // This needs to be replaced by the override check
            Entity targetEnt = RayTracing.instance().getTargetEntity();

            if (targetEnt != null) {
                this.currenttip.clear();
                this.currenttipHead.clear();
                this.currenttipBody.clear();
                this.currenttipTail.clear();

                handler.handleEntityTextData(targetEnt, accessor, currenttipHead, TooltipPosition.HEADER);
                handler.handleEntityTextData(targetEnt, accessor, currenttipBody, TooltipPosition.BODY);
                handler.handleEntityTextData(targetEnt, accessor, currenttipTail, TooltipPosition.FOOTER);

                if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHIFTENTS, false) && !currenttipBody.isEmpty() && !accessor.getPlayer().isSneaking()) {
                    currenttipBody.clear();
                    currenttipBody.add(ITALIC + "Press shift for more data");
                }

                this.currenttip.addAll(this.currenttipHead);
                this.currenttip.addAll(this.currenttipBody);
                this.currenttip.addAll(this.currenttipTail);

                this.tooltip = new Tooltip(this.currenttip, RayTracing.instance().getTargetStack());
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return mod_BlockHelper.MOD_ID + ":WailaTickHandler";
    }

    @ForgeSubscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) resetAll();
    }

    private void resetAll() {
        this.tooltip = null;
        RayTracing.instance().clear();
        DataAccessorCommon.INSTANCE.clear();
    }

}

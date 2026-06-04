package mcp.mobius.waila.overlay;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.MovingObjectType;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.event.ClientFirstTickInWorldEvent;
import mcp.mobius.waila.api.event.WailaEventRegistrar;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.TipList;
import mcp.mobius.waila.network.Packet0x00ServerPing;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import mcp.mobius.waila.utils.ModIdentification;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class WailaTickHandler {

    private Tooltip tooltip;
    private final MetaDataProvider handler = new MetaDataProvider();
    private final ITaggedList<String, String> currenttip = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipHead = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipBody = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipTail = new TipList<String, String>();
    private boolean firstTick = true;
    private World lastWorld = null;

    public void onTickInGame(Minecraft mc) {
        resetAllWhenNeeded(mc);

        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;

        if (firstTick && world != null && player != null) {
            ModIdentification.init();
            FixDetector.detectFixes(mc);
            mod_BlockHelper.UPDATER.notifyUpdater(mc);
            WailaEventRegistrar.postClientFirstTickInWorld(new ClientFirstTickInWorldEvent(mc));
            firstTick = false;
        }

        if (OverlayRenderer.shouldHideOverlay()) return;

        RayTracing.instance().fire();
        MovingObjectPosition target = RayTracing.instance().getTarget();

        if (!mc.isMultiplayerWorld())
            mod_BlockHelper.INSTANCE.serverPresent = true;

        if (target != null && target.typeOfHit == MovingObjectType.TILE) {
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
        } else if (target != null && target.typeOfHit == MovingObjectType.ENTITY) {
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

        OverlayRenderer.renderOverlay(tooltip);
    }

    private void resetAllWhenNeeded(Minecraft mc) {
        World world = mc.theWorld;
        if (this.lastWorld != world) {
            this.lastWorld = world;
            if (world == null) {
                resetAll();
            }
        }
    }

    private void resetAll() {
        this.tooltip = null;
        RayTracing.instance().clear();
        DataAccessorCommon.INSTANCE.clear();
        Packet0x00ServerPing.resetClient();
    }

}

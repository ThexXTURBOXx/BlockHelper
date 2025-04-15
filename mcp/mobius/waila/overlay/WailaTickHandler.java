package mcp.mobius.waila.overlay;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.TipList;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class WailaTickHandler {

    public Tooltip tooltip;
    public final MetaDataProvider handler = new MetaDataProvider();
    private final ITaggedList<String, String> currenttip = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipHead = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipBody = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipTail = new TipList<String, String>();
    private boolean firstTick = true;

    public void onTickInGame(Minecraft mc) {
        if (firstTick && mc.theWorld != null && mc.thePlayer != null) {
            FixDetector.detectFixes(mc);
            mod_BlockHelper.UPDATER.notifyUpdater(mc);
            firstTick = false;
        }

        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;
        if (world != null && player != null) {
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


                    //this.identifiedHighlight = handler.identifyHighlight(world, player, target);
                    handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipHead, TooltipPosition.HEADER);
                    handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipBody, TooltipPosition.BODY);
                    handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipTail, TooltipPosition.FOOTER);

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

                Entity targetEnt = RayTracing.instance().getTargetEntity(); // This need to be replaced by the
                // override check.

                if (targetEnt != null) {
                    this.currenttip.clear();
                    this.currenttipHead.clear();
                    this.currenttipBody.clear();
                    this.currenttipTail.clear();

                    handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipHead, TooltipPosition.HEADER);
                    handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipBody, TooltipPosition.BODY);
                    handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipTail, TooltipPosition.FOOTER);

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

        OverlayRenderer.renderOverlay();
    }

}

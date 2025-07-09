package mcp.mobius.waila.overlay;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import forge.Configuration;
import java.util.EnumSet;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.event.ClientFirstTickInWorldEvent;
import mcp.mobius.waila.api.event.WailaEventRegistrar;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.TipList;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class WailaTickHandler implements ITickHandler {

    private Tooltip tooltip;
    private final MetaDataProvider handler = new MetaDataProvider();
    private final ITaggedList<String, String> currenttip = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipHead = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipBody = new TipList<String, String>();
    private final ITaggedList<String, String> currenttipTail = new TipList<String, String>();
    private boolean firstTick = true;
    private World lastWorld = null;

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> enumSet, Object... objects) {
        World world = ModLoader.getMinecraftInstance().theWorld;
        if (this.lastWorld != world) {
            this.lastWorld = world;
            resetAll();
        }

        if (enumSet.contains(TickType.RENDER))
            OverlayRenderer.renderOverlay(tooltip);

        if (enumSet.contains(TickType.GAME))
            clientTick();
    }

    private void clientTick() {
        Minecraft mc = ModLoader.getMinecraftInstance();
        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;

        if (firstTick) {
            ModIdentification.init();
            FixDetector.detectFixes(mc);
            mod_BlockHelper.UPDATER.notifyUpdater(mc);
            WailaEventRegistrar.postClientFirstTickInWorld(new ClientFirstTickInWorldEvent(mc));
            firstTick = false;
        }

        if (!mc.isMultiplayerWorld())
            mod_BlockHelper.INSTANCE.serverPresent = true;

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
        return EnumSet.of(TickType.GAME, TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return mod_BlockHelper.MOD_ID + ":WailaTickHandler";
    }

    private void resetAll() {
        this.tooltip = null;
        RayTracing.instance().clear();
        DataAccessorCommon.INSTANCE.clear();
    }

}

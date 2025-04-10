package mcp.mobius.waila.overlay;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.api.impl.TipList;
import mcp.mobius.waila.cbcore.Layout;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class WailaTickHandler implements ITickHandler {

    //public static LangUtil lang = LangUtil.loadLangDir("waila");

    private boolean firstTick = true;
    public ItemStack identifiedHighlight = new ItemStack(Block.dirt);
    private ITaggedList<String, String> currenttip = new TipList<String, String>();
    private ITaggedList<String, String> currenttipHead = new TipList<String, String>();
    private ITaggedList<String, String> currenttipBody = new TipList<String, String>();
    private ITaggedList<String, String> currenttipTail = new TipList<String, String>();
    public Tooltip tooltip = null;
    public MetaDataProvider handler = new MetaDataProvider();
    private final Minecraft mc = Minecraft.getMinecraft();

    private static WailaTickHandler _instance;

    private WailaTickHandler() {
    }

    public static WailaTickHandler instance() {
        if (_instance == null)
            _instance = new WailaTickHandler();
        return _instance;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.contains(TickType.RENDER)) {
            OverlayRenderer.renderOverlay();
        }

        if (!type.contains(TickType.CLIENT)) return;

        if (firstTick && mc.theWorld != null && mc.thePlayer != null) {
            FixDetector.detectFixes(mc);
            BlockHelperUpdater.notifyUpdater(mc);
            firstTick = false;
        }

        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;
        if (world != null && player != null) {
            RayTracing.instance().fire();
            MovingObjectPosition target = RayTracing.instance().getTarget();

            if (target != null && target.typeOfHit == EnumMovingObjectType.TILE) {
                DataAccessorCommon accessor = DataAccessorCommon.instance;
                accessor.set(world, player, target);
                ItemStack targetStack = RayTracing.instance().getTargetStack();    // Here we get either the proper
                // stack or the override

                if (targetStack != null) {
                    this.currenttip = new TipList<String, String>();
                    this.currenttipHead = new TipList<String, String>();
                    this.currenttipBody = new TipList<String, String>();
                    this.currenttipTail = new TipList<String, String>();


                    //this.identifiedHighlight = handler.identifyHighlight(world, player, target);
                    this.currenttipHead = handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipHead, Layout.HEADER);
                    this.currenttipBody = handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipBody, Layout.BODY);
                    this.currenttipTail = handler.handleBlockTextData(targetStack, world, player, target, accessor,
                            currenttipTail, Layout.FOOTER);

                    if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL,
                            Constants.CFG_WAILA_SHIFTBLOCK, false) && !currenttipBody.isEmpty() && !accessor.getPlayer().isSneaking()) {
                        currenttipBody.clear();
                        currenttipBody.add(ITALIC + "Press shift for more data");
                    }

                    this.currenttip.addAll(this.currenttipHead);
                    this.currenttip.addAll(this.currenttipBody);
                    this.currenttip.addAll(this.currenttipTail);

                    this.tooltip = new Tooltip(this.currenttip, targetStack);
                }
            } else if (target != null && target.typeOfHit == EnumMovingObjectType.ENTITY) {
                DataAccessorCommon accessor = DataAccessorCommon.instance;
                accessor.set(world, player, target);

                Entity targetEnt = RayTracing.instance().getTargetEntity(); // This need to be replaced by the
                // override check.

                if (targetEnt != null) {
                    this.currenttip = new TipList<String, String>();
                    this.currenttipHead = new TipList<String, String>();
                    this.currenttipBody = new TipList<String, String>();
                    this.currenttipTail = new TipList<String, String>();

                    this.currenttipHead = handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipHead, Layout.HEADER);
                    this.currenttipBody = handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipBody, Layout.BODY);
                    this.currenttipTail = handler.handleEntityTextData(targetEnt, world, player, target, accessor,
                            currenttipTail, Layout.FOOTER);

                    if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL,
                            Constants.CFG_WAILA_SHIFTENTS, false) && !currenttipBody.isEmpty() && !accessor.getPlayer().isSneaking()) {
                        currenttipBody.clear();
                        currenttipBody.add(ITALIC + "Press shift for more data");
                    }

                    this.currenttip.addAll(this.currenttipHead);
                    this.currenttip.addAll(this.currenttipBody);
                    this.currenttip.addAll(this.currenttipTail);

                    this.tooltip = new Tooltip(this.currenttip, false);
                }
            }
        }

    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "Waila Tick Handler";
    }
}

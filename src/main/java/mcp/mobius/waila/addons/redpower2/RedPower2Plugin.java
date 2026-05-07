package mcp.mobius.waila.addons.redpower2;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public final class RedPower2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RedPower2Plugin();

    static Class<?> RedPowerBase = null;
    static Field RedPowerBase_blockMicro = null;

    static Class<?> RedPowerLogic = null;

    static Class<?> RedPowerMachine = null;

    static Class<?> RedPowerWiring = null;

    static Class<?> RedPowerWorld = null;
    static Field RedPowerWorld_itemSeeds = null;

    static Class<?> CoreLib = null;
    static Method CoreLib_retraceBlock = null;
    static Method CoreLib_getTileEntity = null;

    static Class<?> CoverLib = null;
    static Method CoverLib_convertCoverPlate = null;

    static Class<?> TileCoverable = null;
    static Method TileCoverable_getCover = null;
    static Method TileCoverable_getCoverMask = null;

    static Class<?> TileExtended = null;
    static Method TileExtended_getBlockID = null;
    static Method TileExtended_getExtendedID = null;
    static Method TileExtended_addHarvestContents = null;

    static Class<?> TileLogic = null;
    static Field TileLogic_Rotation = null;
    static Field TileLogic_Cover = null;
    static Field TileLogic_SubId = null;

    static Class<?> TilePipe = null;

    static Class<?> TileTube = null;

    static Class<?> TileWiring = null;
    static Field TileWiring_ConSides = null;
    static Field TileWiring_CenterPost = null;
    static Field TileWiring_Metadata = null;

    static Class<?> BlockLogic = null;

    static Class<?> TileRedwire = null;

    static Class<?> TileInsulatedWire = null;

    static Class<?> TileCable = null;

    static Class<?> BlockCustomCrops = null;

    private RedPower2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("com.eloraam.redpower.RedPowerCore");
            RedPowerBase = AccessHelper.getClass("com.eloraam.redpower.RedPowerBase");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Mod core found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Mod core not found.");
            return false;
        }

        try {
            RedPowerLogic = AccessHelper.getClass("com.eloraam.redpower.RedPowerLogic");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Logic module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Logic module not found.");
        }

        try {
            RedPowerMachine = AccessHelper.getClass("com.eloraam.redpower.RedPowerMachine");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Machine module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Machine module not found.");
        }

        try {
            RedPowerWiring = AccessHelper.getClass("com.eloraam.redpower.RedPowerWiring");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Wiring module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Wiring module not found.");
        }

        try {
            RedPowerWorld = AccessHelper.getClass("com.eloraam.redpower.RedPowerWorld");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] World module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] World module not found.");
        }

        return true;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            RedPowerBase_blockMicro = AccessHelper.getField(RedPowerBase, "blockMicro");

            CoreLib = AccessHelper.getClass("com.eloraam.redpower.core.CoreLib");
            CoreLib_retraceBlock = AccessHelper.getMethod(CoreLib,
                    new Class[]{World.class, EntityLiving.class, int.class, int.class, int.class},
                    "retraceBlock");
            CoreLib_getTileEntity = AccessHelper.getMethod(CoreLib,
                    new Class[]{IBlockAccess.class, int.class, int.class, int.class, Class.class},
                    "getTileEntity");

            CoverLib = AccessHelper.getClass("com.eloraam.redpower.core.CoverLib");
            CoverLib_convertCoverPlate = AccessHelper.getMethod(CoverLib, new Class[]{int.class, int.class},
                    "convertCoverPlate");

            TileCoverable = AccessHelper.getClass("com.eloraam.redpower.core.TileCoverable");
            TileCoverable_getCover = AccessHelper.getMethod(TileCoverable, new Class[]{int.class},
                    "getCover");
            TileCoverable_getCoverMask = AccessHelper.getMethod(TileCoverable, new Class[0], "getCoverMask");

            TileExtended = AccessHelper.getClass("com.eloraam.redpower.core.TileExtended");
            TileExtended_getBlockID = AccessHelper.getMethod(TileExtended, new Class[0], "getBlockID");
            TileExtended_getExtendedID = AccessHelper.getMethod(TileExtended, new Class[0], "getExtendedID");
            TileExtended_addHarvestContents = AccessHelper.getMethod(TileExtended, new Class[]{ArrayList.class},
                    "addHarvestContents");

            if (RedPowerLogic != null) {
                TileLogic = AccessHelper.getClass("com.eloraam.redpower.logic.TileLogic");
                TileLogic_Rotation = AccessHelper.getField(TileLogic, "Rotation");
                TileLogic_Cover = AccessHelper.getField(TileLogic, "Cover");
                TileLogic_SubId = AccessHelper.getField(TileLogic, "SubId");
            }

            if (RedPowerMachine != null) {
                TilePipe = AccessHelper.getClass("com.eloraam.redpower.machine.TilePipe");

                TileTube = AccessHelper.getClass("com.eloraam.redpower.machine.TileTube");
            }

            if (RedPowerWiring != null) {
                TileWiring = AccessHelper.getClass("com.eloraam.redpower.wiring.TileWiring");
                TileWiring_ConSides = AccessHelper.getField(TileWiring, "ConSides");
                TileWiring_CenterPost = AccessHelper.getField(TileWiring, "CenterPost");
                TileWiring_Metadata = AccessHelper.getField(TileWiring, "Metadata");
            }

            registrar.registerStackProvider(HUDHandlerMicroBlocks.INSTANCE, TileExtended);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading microblock hooks.", t);
        }

        try {
            if (RedPowerLogic != null) {
                BlockLogic = AccessHelper.getClass("com.eloraam.redpower.logic.BlockLogic");

                registrar.addConfig("RedPower 2", "pr.showio");
                registrar.addConfig("RedPower 2", "pr.showdata");

                registrar.registerDecorator(HUDDecoratorGateLogic.INSTANCE, BlockLogic);

                registrar.registerBodyProvider(HUDHandlerGateLogic.INSTANCE, BlockLogic);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading gate hooks.", t);
        }

        try {
            if (RedPowerWiring != null) {
                TileRedwire = AccessHelper.getClass("com.eloraam.redpower.wiring.TileRedwire");
                TileInsulatedWire = AccessHelper.getClass("com.eloraam.redpower.wiring.TileInsulatedWire");
                TileCable = AccessHelper.getClass("com.eloraam.redpower.wiring.TileCable");

                registrar.addConfig("RedPower 2", "pr.showsignal");

                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileRedwire);
                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileInsulatedWire);
                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileCable);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading wire hooks.", t);
        }

        try {
            if (RedPowerWorld != null) {
                RedPowerWorld_itemSeeds = AccessHelper.getField(RedPowerWorld, "itemSeeds");

                BlockCustomCrops = AccessHelper.getClass("com.eloraam.redpower.world.BlockCustomCrops");

                registrar.registerStackProvider(HUDHandlerCrops.INSTANCE, BlockCustomCrops);

                registrar.registerCropProvider(new DefaultCropProvider(4, 5), BlockCustomCrops);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading crop hooks.", t);
        }
    }

}

package mcp.mobius.waila.addons.redpower2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

public final class RedPower2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RedPower2Plugin();

    static Class<?> RedPowerBase = null;
    static Field RedPowerBase_blockMicro = null;

    static Class<?> mod_RedPowerLogic = null;

    static Class<?> mod_RedPowerMachine = null;

    static Class<?> mod_RedPowerWiring = null;

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

    static Class<?> TileMultipart = null;
    static Method TileMultipart_addHarvestContents = null;

    static Class<?> TileLogic = null;
    static Field TileLogic_Rotation = null;
    static Field TileLogic_Cover = null;

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
            AccessHelper.getClass("mod_RedPowerCore");
            RedPowerBase = AccessHelper.getClass("RedPowerBase");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Mod core found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Mod core not found.");
            return false;
        }

        try {
            mod_RedPowerLogic = AccessHelper.getClass("mod_RedPowerLogic");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Logic module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Logic module not found.");
        }

        try {
            mod_RedPowerMachine = AccessHelper.getClass("mod_RedPowerMachine");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Machine module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Machine module not found.");
        }

        try {
            mod_RedPowerWiring = AccessHelper.getClass("mod_RedPowerWiring");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Wiring module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] Wiring module not found.");
        }

        try {
            AccessHelper.getClass("mod_RedPowerWorld");
            RedPowerWorld = AccessHelper.getClass("RedPowerWorld");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] World module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower 2] World module not found.");
        }

        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            RedPowerBase_blockMicro = AccessHelper.getField(RedPowerBase, "blockMicro");

            CoreLib = AccessHelper.getClass("eloraam.core.CoreLib");
            CoreLib_retraceBlock = AccessHelper.getMethod(CoreLib,
                    new Class[]{World.class, EntityPlayer.class, int.class, int.class, int.class},
                    "retraceBlock");
            CoreLib_getTileEntity = AccessHelper.getMethod(CoreLib,
                    new Class[]{IBlockAccess.class, int.class, int.class, int.class, Class.class},
                    "getTileEntity");

            CoverLib = AccessHelper.getClass("eloraam.core.CoverLib");
            CoverLib_convertCoverPlate = AccessHelper.getMethod(CoverLib, new Class[]{int.class, int.class},
                    "convertCoverPlate");

            TileCoverable = AccessHelper.getClass("eloraam.core.TileCoverable");
            TileCoverable_getCover = AccessHelper.getMethod(TileCoverable, new Class[]{int.class}, "getCover");
            TileCoverable_getCoverMask = AccessHelper.getMethod(TileCoverable, new Class[0], "getCoverMask");

            TileExtended = AccessHelper.getClass("eloraam.core.TileExtended");
            TileExtended_getBlockID = AccessHelper.getMethod(TileExtended, new Class[0], "getBlockID");
            TileExtended_getExtendedID = AccessHelper.getMethod(TileExtended, new Class[0], "getExtendedID");

            TileMultipart = AccessHelper.getClass("eloraam.core.TileMultipart");
            TileMultipart_addHarvestContents = AccessHelper.getMethod(TileMultipart, new Class[]{ArrayList.class},
                    "addHarvestContents");

            if (mod_RedPowerLogic != null) {
                TileLogic = AccessHelper.getClass("eloraam.logic.TileLogic");
                TileLogic_Rotation = AccessHelper.getField(TileLogic, "Rotation");
                TileLogic_Cover = AccessHelper.getField(TileLogic, "Cover");
            }

            if (mod_RedPowerMachine != null) {
                TileTube = AccessHelper.getClass("eloraam.machine.TileTube");
            }

            if (mod_RedPowerWiring != null) {
                TileWiring = AccessHelper.getClass("eloraam.wiring.TileWiring");
                TileWiring_ConSides = AccessHelper.getField(TileWiring, "ConSides");
                TileWiring_CenterPost = AccessHelper.getField(TileWiring, "CenterPost");
                TileWiring_Metadata = AccessHelper.getField(TileWiring, "Metadata");
            }

            registrar.registerStackProvider(HUDHandlerMicroBlocks.INSTANCE, TileExtended);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading microblock hooks.", t);
        }

        try {
            if (mod_RedPowerLogic != null) {
                BlockLogic = AccessHelper.getClass("eloraam.logic.BlockLogic");

                registrar.addConfig("RedPower 2", "pr.showio");
                registrar.addConfig("RedPower 2", "pr.showdata");

                registrar.registerDecorator(HUDDecoratorGateLogic.INSTANCE, BlockLogic);

                registrar.registerBodyProvider(HUDHandlerGateLogic.INSTANCE, BlockLogic);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading gate hooks.", t);
        }

        try {
            if (mod_RedPowerWiring != null) {
                TileRedwire = AccessHelper.getClass("eloraam.wiring.TileRedwire");
                TileInsulatedWire = AccessHelper.getClass("eloraam.wiring.TileInsulatedWire");
                TileCable = AccessHelper.getClass("eloraam.wiring.TileCable");

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

                BlockCustomCrops = AccessHelper.getClass("eloraam.world.BlockCustomCrops");

                registrar.registerStackProvider(HUDHandlerCrops.INSTANCE, BlockCustomCrops);

                registrar.registerCropProvider(new DefaultCropProvider(4, 5), BlockCustomCrops);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading crop hooks.", t);
        }
    }

}

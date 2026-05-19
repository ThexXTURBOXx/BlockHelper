package mcp.mobius.waila.addons.redpower;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class RedPowerPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RedPowerPlugin();

    static Class<?> mod_RedPowerLogic = null;

    static Class<?> mod_RedPowerWiring = null;

    static Class<?> RedPowerWiring = null;
    static Field RedPowerWiring_blockWiring = null;

    static Class<?> CoverLib = null;
    static Method CoverLib_convertCoverPlate = null;

    static Class<?> TileCoverable = null;
    static Method TileCoverable_getCover = null;
    static Method TileCoverable_getCoverMask = null;

    static Class<?> TileExtended = null;
    static Method TileExtended_getExtendedID = null;

    static Class<?> BlockMultipart = null;

    static Class<?> TileLogic = null;
    static Field TileLogic_Rotation = null;
    static Method TileLogic_getBlockID = null;

    static Class<?> TileWiring = null;
    static Field TileWiring_ConSides = null;
    static Field TileWiring_CenterPost = null;
    static Field TileWiring_Metadata = null;

    static Class<?> BlockLogic = null;

    static Class<?> TileRedwire = null;

    static Class<?> TileInsulatedWire = null;

    static Class<?> TileCable = null;

    private RedPowerPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_RedPowerCore");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Mod core found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Mod core not found.");
            return false;
        }

        try {
            mod_RedPowerLogic = AccessHelper.getClass("mod_RedPowerLogic");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Logic module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Logic module not found.");
        }

        try {
            mod_RedPowerWiring = AccessHelper.getClass("mod_RedPowerWiring");
            RedPowerWiring = AccessHelper.getClass("RedPowerWiring");
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Wiring module found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[RedPower] Wiring module not found.");
        }

        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            CoverLib = AccessHelper.getClass("eloraam.core.CoverLib");
            CoverLib_convertCoverPlate = AccessHelper.getMethod(CoverLib, new Class[]{int.class, int.class},
                    "convertCoverPlate");

            TileCoverable = AccessHelper.getClass("eloraam.core.TileCoverable");
            TileCoverable_getCover = AccessHelper.getMethod(TileCoverable, new Class[]{int.class}, "getCover");
            TileCoverable_getCoverMask = AccessHelper.getMethod(TileCoverable, new Class[0], "getCoverMask");

            TileExtended = AccessHelper.getClass("eloraam.core.TileExtended");
            TileExtended_getExtendedID = AccessHelper.getMethod(TileExtended, new Class[0], "getExtendedID");

            BlockMultipart = AccessHelper.getClass("eloraam.core.BlockMultipart");

            if (mod_RedPowerLogic != null) {
                TileLogic = AccessHelper.getClass("eloraam.intred.TileLogic");
                TileLogic_Rotation = AccessHelper.getField(TileLogic, "Rotation");
                TileLogic_getBlockID = AccessHelper.getMethod(TileLogic, new Class[0], "getBlockID");
            }

            if (mod_RedPowerWiring != null) {
                RedPowerWiring_blockWiring = AccessHelper.getField(RedPowerWiring, "blockWiring");

                TileWiring = AccessHelper.getClass("eloraam.wiring.TileWiring");
                TileWiring_ConSides = AccessHelper.getField(TileWiring, "ConSides");
                TileWiring_CenterPost = AccessHelper.getField(TileWiring, "CenterPost");
                TileWiring_Metadata = AccessHelper.getField(TileWiring, "Metadata");
            }

            registrar.registerStackProvider(HUDHandlerMicroBlocks.INSTANCE, TileExtended);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower] Error while loading microblock hooks.", t);
        }

        try {
            if (mod_RedPowerLogic != null) {
                BlockLogic = AccessHelper.getClass("eloraam.intred.BlockLogic");

                registrar.addConfig("RedPower", "pr.showio");
                registrar.addConfig("RedPower", "pr.showdata");

                registrar.registerDecorator(HUDDecoratorGateLogic.INSTANCE, BlockLogic);

                registrar.registerBodyProvider(HUDHandlerGateLogic.INSTANCE, BlockLogic);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower] Error while loading gate hooks.", t);
        }

        try {
            if (mod_RedPowerWiring != null) {
                TileRedwire = AccessHelper.getClass("eloraam.wiring.TileRedwire");
                TileInsulatedWire = AccessHelper.getClass("eloraam.wiring.TileInsulatedWire");
                TileCable = AccessHelper.getClass("eloraam.wiring.TileCable");

                registrar.addConfig("RedPower", "pr.showsignal");

                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileRedwire);
                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileInsulatedWire);
                registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileCable);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower] Error while loading wire hooks.", t);
        }
    }

}

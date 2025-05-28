package mcp.mobius.waila.addons.redpower2;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

public final class RedPower2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RedPower2Plugin();

    public static Class<?> RedPowerBase = null;
    public static Field RedPowerBase_blockMicro = null;

    public static Class<?> mod_RedPowerLogic = null;

    public static Class<?> mod_RedPowerMachine = null;

    public static Class<?> mod_RedPowerWiring = null;

    public static Class<?> CoreLib = null;
    public static Method CoreLib_retraceBlock = null;
    public static Method CoreLib_getTileEntity = null;

    public static Class<?> CoverLib = null;
    public static Method CoverLib_convertCoverPlate = null;

    public static Class<?> TileCoverable = null;
    public static Method TileCoverable_getCover = null;
    public static Method TileCoverable_getCoverMask = null;

    public static Class<?> TileExtended = null;
    public static Method TileExtended_getBlockID = null;
    public static Method TileExtended_getExtendedID = null;

    public static Class<?> TileMultipart = null;
    public static Method TileMultipart_addHarvestContents = null;

    public static Class<?> TileLogic = null;
    public static Field TileLogic_Rotation = null;
    public static Field TileLogic_Cover = null;
    public static Field TileLogic_SubId = null;

    public static Class<?> TilePipe = null;

    public static Class<?> TileTube = null;

    public static Class<?> TileWiring = null;
    public static Field TileWiring_ConSides = null;
    public static Field TileWiring_CenterPost = null;
    public static Field TileWiring_Metadata = null;

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

        return true;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

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
                TileLogic_SubId = AccessHelper.getField(TileLogic, "SubId");
            }

            if (mod_RedPowerMachine != null) {
                TilePipe = AccessHelper.getClass("eloraam.machine.TilePipe");

                TileTube = AccessHelper.getClass("eloraam.machine.TileTube");
            }

            if (mod_RedPowerWiring != null) {
                TileWiring = AccessHelper.getClass("eloraam.wiring.TileWiring");
                TileWiring_ConSides = AccessHelper.getField(TileWiring, "ConSides");
                TileWiring_CenterPost = AccessHelper.getField(TileWiring, "CenterPost");
                TileWiring_Metadata = AccessHelper.getField(TileWiring, "Metadata");
            }

            registrar.registerStackProvider(HUDHandlerRP2.INSTANCE, TileExtended);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[RedPower 2] Error while loading microblock hooks.", t);
        }
    }

}

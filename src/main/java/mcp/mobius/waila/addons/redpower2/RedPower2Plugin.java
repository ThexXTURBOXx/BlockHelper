package mcp.mobius.waila.addons.redpower2;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

public final class RedPower2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RedPower2Plugin();

    public static Class<?> RedPowerBase = null;
    public static Field RedPowerBase_blockMicro = null;

    public static Class<?> RedPowerLogic = null;

    public static Class<?> RedPowerMachine = null;

    public static Class<?> RedPowerWiring = null;

    public static Class<?> RedPowerWorld = null;
    public static Field RedPowerWorld_itemSeeds = null;

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
    public static Method TileExtended_addHarvestContents = null;

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

    public static Class<?> BlockCustomCrops = null;

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

package mcp.mobius.waila.addons.florasoma;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.util.MathHelper;

public final class FloraSomaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new FloraSomaPlugin();

    static Class<?> FloraCropBlock;
    static Method FloraCropBlock_getCropItem;

    static Class<?> BerryBush;

    private FloraSomaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("florasoma.crops.FloraCrops");
            mod_BlockHelper.LOG.log(Level.INFO, "[Flora and Soma] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Flora and Soma] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            FloraCropBlock = AccessHelper.getClass("florasoma.crops.blocks.FloraCropBlock");
            FloraCropBlock_getCropItem = AccessHelper.getDeclaredMethod(FloraCropBlock, new Class[]{int.class},
                    "getCropItem");

            registrar.registerStackProvider(HUDHandlerFloraSomaCrops.INSTANCE, FloraCropBlock);

            registrar.registerHeadProvider(HUDHandlerFloraSomaCrops.INSTANCE, FloraCropBlock);

            registrar.registerCropProvider(new DefaultCropProvider(3) {
                @Override
                public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
                    return accessor.getMetadata() % 4;
                }
            }, FloraCropBlock);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Flora and Soma] Error while loading crop hooks.", t);
        }

        try {
            BerryBush = AccessHelper.getClass("florasoma.crops.blocks.BerryBush");

            registrar.registerCropProvider(new DefaultCropProvider(2, 3) {
                @Override
                public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
                    return MathHelper.floor_double(accessor.getMetadata() / 4d);
                }
            }, BerryBush);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Flora and Soma] Error while loading bush hooks.", t);
        }
    }

}

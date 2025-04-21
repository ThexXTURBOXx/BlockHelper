package mcp.mobius.waila.addons.natura;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropHandler;
import mcp.mobius.waila.api.ICropHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public final class NaturaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new NaturaPlugin();

    static Class<?> CropBlock;
    static Method CropBlock_getCropItem;

    static Class<?> BerryBush;

    static Class<?> NetherBerryBush;

    private NaturaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("mods.natura.Natura");
            mod_BlockHelper.LOG.log(Level.INFO, "[Natura] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Natura] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            CropBlock = Class.forName("mods.natura.blocks.crops.CropBlock");
            CropBlock_getCropItem = CropBlock.getDeclaredMethod("getCropItem", int.class);
            CropBlock_getCropItem.setAccessible(true);

            registrar.registerStackProvider(HUDHandlerNaturaCrops.INSTANCE, CropBlock);

            registrar.registerHeadProvider(HUDHandlerNaturaCrops.INSTANCE, CropBlock);

            registrar.registerCropHandler(new DefaultCropHandler(8) {
                @Override
                public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
                    int meta = accessor.getMetadata();
                    return meta - (meta < 4 ? 0 : 4);
                }

                @Override
                public int getMaxStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
                    return accessor.getMetadata() < 4 ? 3 : 4;
                }
            }, CropBlock);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Natura] Error while loading crop hooks.", t);
        }

        try {
            BerryBush = Class.forName("mods.natura.blocks.crops.BerryBush");
            NetherBerryBush = Class.forName("mods.natura.blocks.crops.NetherBerryBush");

            ICropHandler handler = new DefaultCropHandler(2, 3) {
                @Override
                public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
                    return MathHelper.floor_double(accessor.getMetadata() / 4d);
                }
            };
            registrar.registerCropHandler(handler, BerryBush);
            registrar.registerCropHandler(handler, NetherBerryBush);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Natura] Error while loading bush hooks.", t);
        }
    }

}

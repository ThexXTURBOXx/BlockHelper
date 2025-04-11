package mcp.mobius.waila.addons.vanillamc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherStalk;
import net.minecraft.block.BlockStem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerCrops implements IWailaDataProvider {

    public static final Map<Class<?>, Integer> MAX_STAGES = new HashMap<Class<?>, Integer>();

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        /* Crops */
        if (config.getConfig("general.showcrop") && isCrop(block)) {
            int maxStage = getMaxStage(block, accessor.getBlockID());
            int growStage;
            if (block instanceof BlockCocoa)
                growStage = BlockCocoa.func_72219_c(accessor.getMetadata());
            else
                growStage = accessor.getMetadata();
            float growthValue = (growStage / (float) maxStage) * 100;
            if (growthValue < 100.0)
                currenttip.add(String.format("%s : %.0f %%", LangUtil.translateG("hud.msg.growth"),
                        growthValue));
            else
                currenttip.add(String.format("%s : %s", LangUtil.translateG("hud.msg.growth"),
                        LangUtil.translateG("hud.msg.mature")));
        }

        return currenttip;
    }

    private boolean isCrop(Block b) {
        boolean crop = b instanceof BlockCrops
                       || b instanceof BlockNetherStalk
                       || b instanceof BlockStem
                       || b instanceof BlockCocoa;
        if (!crop) {
            try {
                for (Method method : b.getClass().getDeclaredMethods()) {
                    String name = method.getName();
                    if (name.equals("getGrowthRate")) return true;
                    if (name.equals("getGrowthModifier")) return true;
                }
            } catch (Throwable ignored) {
            }
        }
        return crop;
    }

    private int getMaxStage(Block b, int id) {
        try {
            for (Class<?> c : MAX_STAGES.keySet())
                if (c.isInstance(b)) return MAX_STAGES.get(c);
            if (b instanceof BlockCrops) {
                return 7;
            } else if (b instanceof BlockStem) {
                return 7;
            } else if (b instanceof BlockNetherStalk) {
                return 3;
            } else if (b instanceof BlockCocoa) {
                return 2;
            } else {
                for (Field field : b.getClass().getFields()) {
                    if (StringUtils.containsIgnoreCase(field.getName(), "max")
                        && StringUtils.containsIgnoreCase(field.getName(), "stage")) {
                        return field.getInt(Block.blocksList[id]);
                    }
                }
                for (Field field : b.getClass().getDeclaredFields()) {
                    if (StringUtils.containsIgnoreCase(field.getName(), "max")
                        && StringUtils.containsIgnoreCase(field.getName(), "stage")) {
                        field.setAccessible(true);
                        return field.getInt(Block.blocksList[id]);
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return 7;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                                                    IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                     int x, int y, int z) {
        return tag;
    }

    public static void register() {
        IWailaDataProvider provider = new HUDHandlerCrops();

        ModuleRegistrar.instance().registerBodyProvider(provider, Block.class);

        try {
            Class<?> CropBlock = Class.forName("mods.natura.blocks.crops.CropBlock");
            MAX_STAGES.put(CropBlock, 3);
        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Natura] Error while loading crop hooks." + e);
        }
    }

}

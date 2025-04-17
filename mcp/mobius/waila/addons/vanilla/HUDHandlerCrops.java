package mcp.mobius.waila.addons.vanilla;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherStalk;
import net.minecraft.block.BlockStem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.MAX_STAGES;

public final class HUDHandlerCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerCrops();

    private HUDHandlerCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        /* Crops */
        if (config.get("general.showcrop") && isCrop(block)) {
            int maxStage = getMaxStage(block, accessor.getBlockID());
            int growStage;
            if (block instanceof BlockCocoa)
                growStage = BlockCocoa.func_72219_c(accessor.getMetadata());
            else
                growStage = accessor.getMetadata();
            float growthValue = (growStage / (float) maxStage) * 100;
            if (growthValue < 100.0)
                currenttip.add(String.format("%s : %.0f %%", I18n.translate("hud.msg.growth"), growthValue));
            else
                currenttip.add(I18n.translate("hud.msg.growth") + " : " +
                               I18n.translate("hud.msg.mature"));
        }
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
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

}

package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockNetherStalk;
import net.minecraft.src.BlockStem;
import net.minecraft.src.TileEntity;

public class VanillaIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block b, int id, int meta, InfoHolder info) {
        if (isCrop(b)) {
            double max_stage = getMaxStage(b, id);
            int grow = (int) ((meta / max_stage) * 100);
            String toShow;
            if (grow >= 100) {
                toShow = "Mature";
            } else {
                toShow = grow + "%";
            }
            info.add("Growth State: " + toShow);
        }

        if (id == Block.redstoneWire.blockID) {
            info.add("Strength: " + meta);
        }

        if (id == Block.lever.blockID) {
            String state = "Off";
            if (meta >= 8) {
                state = "On";
            }
            info.add("State: " + state);
        }
    }

    @Override
    public String getName(Block block, TileEntity te, int id, int meta) {
        if (block instanceof BlockStem) {
            Block drop = getDeclaredField(block, "a");
            return drop.translateBlockName();
        }
        return null;
    }

    private double getMaxStage(Block b, int id) {
        try {
            if (iof(b, "flora.FloraCrops")) {
                return 3;
            } else if (b instanceof BlockCrops) {
                return 7;
            } else if (b instanceof BlockStem) {
                return 7;
            } else if (b instanceof BlockNetherStalk) {
                return 3;
            } else {
                for (Field field : b.getClass().getFields()) {
                    if (containsIgnoreCase(field.getName(), "max")
                            && containsIgnoreCase(field.getName(), "stage")) {
                        field.setAccessible(true);
                        return field.getInt(Block.blocksList[id]);
                    }
                }
                for (Field field : b.getClass().getDeclaredFields()) {
                    if (containsIgnoreCase(field.getName(), "max")
                            && containsIgnoreCase(field.getName(), "stage")) {
                        field.setAccessible(true);
                        return field.getInt(Block.blocksList[id]);
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return 7;
    }

    private boolean isCrop(Block b) {
        boolean crop = b instanceof BlockCrops
                || b instanceof BlockNetherStalk
                || b instanceof BlockStem;
        if (!crop) {
            try {
                for (Method method : b.getClass().getDeclaredMethods()) {
                    String name = method.getName();
                    if (name.equals("getGrowthRate")) {
                        return true;
                    }
                    if (name.equals("getGrowthModifier")) {
                        return true;
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return crop;
    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

}

package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherStalk;
import net.minecraft.block.BlockStem;

public class VanillaIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (isCrop(state.block)) {
            double max_stage = getMaxStage(state.block, state.id);
            int grow = (int) ((state.meta / max_stage) * 100);
            String toShow;
            if (grow >= 100) {
                toShow = "Mature";
            } else {
                toShow = grow + "%";
            }
            info.add("Growth State: " + toShow);
        }

        if (state.id == Block.redstoneWire.blockID) {
            info.add("Strength: " + state.meta);
        }

        if (state.id == Block.lever.blockID) {
            String leverState = "Off";
            if (state.meta >= 8) {
                leverState = "On";
            }
            info.add("State: " + leverState);
        }
    }

    private double getMaxStage(Block b, int id) {
        try {
            if (iof(b, "mods.natura.blocks.crops.CropBlock")) {
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

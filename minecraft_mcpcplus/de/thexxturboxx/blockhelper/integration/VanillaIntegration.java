package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockNetherWart;
import net.minecraft.server.BlockStem;
import net.minecraft.server.TileEntity;

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

        if (id == Block.REDSTONE_WIRE.id) {
            info.add("Strength: " + meta);
        }

        if (id == Block.LEVER.id) {
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
            Block drop = getDeclaredField(block, "blockFruit");
            return drop.getName();
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
            } else if (b instanceof BlockNetherWart) {
                return 3;
            } else {
                for (Field field : b.getClass().getFields()) {
                    if (containsIgnoreCase(field.getName(), "max")
                            && containsIgnoreCase(field.getName(), "stage")) {
                        field.setAccessible(true);
                        return field.getInt(Block.byId[id]);
                    }
                }
                for (Field field : b.getClass().getDeclaredFields()) {
                    if (containsIgnoreCase(field.getName(), "max")
                            && containsIgnoreCase(field.getName(), "stage")) {
                        field.setAccessible(true);
                        return field.getInt(Block.byId[id]);
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return 7;
    }

    private boolean isCrop(Block b) {
        boolean crop = b instanceof BlockCrops
                || b instanceof BlockNetherWart
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

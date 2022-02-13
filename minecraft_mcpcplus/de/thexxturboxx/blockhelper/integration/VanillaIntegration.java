package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockStem;

public class VanillaIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (isCrop(state.block)) {
            double max_stage = getMaxStage(state.block, state.id);
            int grow = (int) ((state.meta / max_stage) * 100);
            String toShow;
            if (grow >= 100) {
                toShow = I18n.format("mature");
            } else {
                toShow = grow + "%";
            }
            info.add(I18n.format("growth_state_format", toShow));
        }

        if (state.id == Block.REDSTONE_WIRE.id) {
            info.add(I18n.format("strength_format", state.meta));
        }

        if (state.id == Block.LEVER.id) {
            String leverState = I18n.format(state.meta >= 8 ? "on" : "off");
            info.add(I18n.format("state_format", leverState));
        }

        if (state.id == Block.DIODE_OFF.id || state.id == Block.DIODE_ON.id) {
            info.add(I18n.format("delay", ((state.meta & 0xc) >> 2) + 1));
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.vanillaIntegration;
    }

    private double getMaxStage(Block b, int id) {
        try {
            if (b instanceof BlockCrops) {
                return 7;
            } else if (b instanceof BlockStem) {
                return 7;
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
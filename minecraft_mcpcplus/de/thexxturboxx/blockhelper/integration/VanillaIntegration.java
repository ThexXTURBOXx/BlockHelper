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
import net.minecraft.server.BlockNetherWart;
import net.minecraft.server.BlockStem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemRecord;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.TileEntityNote;
import net.minecraft.server.TileEntityRecordPlayer;

public class VanillaIntegration extends BlockHelperInfoProvider {

    private static final String[] NOTES = {"F\u266F/G\u266D", "G", "G\u266F/A\u266D", "A", "A\u266F/B\u266D", "B",
            "C", "C\u266F/D\u266D", "D", "D\u266F/E\u266D", "E", "F"};

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

        if (state.id == Block.NOTE_BLOCK.id) {
            TileEntityNote te = (TileEntityNote) state.te;
            info.add(I18n.format("note", NOTES[te.note % 12] + (te.note / 12 + 1)));

            Material m = state.world.getMaterial(state.mop.b, state.mop.c - 1, state.mop.d);
            String instrument = "piano";
            if (m == Material.STONE) {
                instrument = "bass_drum";
            } else if (m == Material.SAND) {
                instrument = "snare_drum";
            } else if (m == Material.SHATTERABLE) {
                instrument = "clicks_sticks";
            } else if (m == Material.WOOD) {
                instrument = "bass_guitar";
            }
            info.add(I18n.format("instrument", I18n.format(instrument)));
        }

        if (state.id == Block.JUKEBOX.id) {
            TileEntityRecordPlayer te = (TileEntityRecordPlayer) state.te;
            if (te.a != 0)
                info.add(I18n.format("record", "C418 - " + ((ItemRecord) Item.byId[te.a]).a));
        }

        if (state.id == Block.MOB_SPAWNER.id) {
            TileEntityMobSpawner te = (TileEntityMobSpawner) state.te;
            info.add(I18n.format("mob", te.mobName));
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

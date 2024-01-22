package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockNetherBrick;
import net.minecraft.src.BlockStem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRecord;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.TileEntityNote;
import net.minecraft.src.TileEntityRecordPlayer;

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
                toShow = I18n.format(state.translator, "mature");
            } else {
                toShow = grow + "%";
            }
            info.add(I18n.format(state.translator, "growth_state_format", toShow));
        }

        if (state.id == Block.redstoneWire.blockID) {
            info.add(I18n.format(state.translator, "strength_format", state.meta));
        }

        if (state.id == Block.lever.blockID) {
            String leverState = I18n.format(state.translator, state.meta >= 8 ? "on" : "off");
            info.add(I18n.format(state.translator, "state_format", leverState));
        }

        if (state.id == Block.redstoneRepeaterIdle.blockID || state.id == Block.redstoneRepeaterActive.blockID) {
            info.add(I18n.format(state.translator, "delay", ((state.meta & 0xc) >> 2) + 1));
        }

        if (state.id == Block.music.blockID) {
            TileEntityNote te = (TileEntityNote) state.te;
            info.add(I18n.format(state.translator, "note", NOTES[te.note % 12] + (te.note / 12 + 1)));

            Material m = state.world.getBlockMaterial(state.mop.blockX, state.mop.blockY - 1, state.mop.blockZ);
            String instrument = "piano";
            if (m == Material.rock) {
                instrument = "bass_drum";
            } else if (m == Material.sand) {
                instrument = "snare_drum";
            } else if (m == Material.glass) {
                instrument = "clicks_sticks";
            } else if (m == Material.wood) {
                instrument = "bass_guitar";
            }
            info.add(I18n.format(state.translator, "instrument", I18n.format(state.translator, instrument)));
        }

        if (state.id == Block.jukebox.blockID) {
            TileEntityRecordPlayer te = (TileEntityRecordPlayer) state.te;
            if (te.record != 0)
                info.add(I18n.format(state.translator, "record",
                        "C418 - " + ((ItemRecord) Item.itemsList[te.record]).recordName));
        }

        if (state.id == Block.mobSpawner.blockID) {
            TileEntityMobSpawner te = (TileEntityMobSpawner) state.te;
            info.add(I18n.format(state.translator, "mob", getDeclaredField(TileEntityMobSpawner.class, te, "i")));
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
            } else if (b instanceof BlockNetherBrick) {
                // Badly translated class name
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
                       || b instanceof BlockNetherBrick
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

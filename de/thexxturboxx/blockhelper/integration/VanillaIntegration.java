package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCocoa;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockFlowerPot;
import net.minecraft.src.BlockNetherStalk;
import net.minecraft.src.BlockStem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRecord;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.TileEntityNote;
import net.minecraft.src.TileEntityRecordPlayer;
import net.minecraft.src.TileEntitySkull;

public class VanillaIntegration extends BlockHelperInfoProvider {

    private static final String[] NOTES = {"F\u266F/G\u266D", "G", "G\u266F/A\u266D", "A", "A\u266F/B\u266D", "B",
            "C", "C\u266F/D\u266D", "D", "D\u266F/E\u266D", "E", "F"};

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (isCrop(state.block)) {
            double max_stage = getMaxStage(state.block, state.id);
            int growStage = state.meta;
            if (state.block instanceof BlockCocoa)
                growStage = BlockCocoa.func_72219_c(state.meta);
            int growPercentage = (int) ((growStage / max_stage) * 100);
            String toShow;
            if (growPercentage >= 100) {
                toShow = I18n.format(state.translator, "mature");
            } else {
                toShow = growPercentage + "%";
            }
            info.add(I18n.format(state.translator, "growth_state_format", toShow));
        }

        if (state.id == Block.field_82516_cf.blockID) {
            ItemStack flower = BlockFlowerPot.func_82531_c(state.meta);
            if (flower != null)
                info.add(I18n.format(state.translator, "flower", flower.func_82833_r()));
        }

        if (state.te instanceof TileEntitySkull) {
            TileEntitySkull te = (TileEntitySkull) state.te;
            if (te.func_82117_a() == 3) {
                String playerName = te.func_82120_c();
                if (playerName != null && !playerName.isEmpty())
                    info.add(I18n.format(state.translator, "head_owner", playerName));
            }
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
            info.add(I18n.format(state.translator, "mob", getDeclaredField(TileEntityMobSpawner.class, te, "d")));
        }
    }

    @Override
    public String getName(BlockHelperBlockState state) {
        if (state.id == Block.field_82510_ck.blockID) {
            ItemStack is = new ItemStack(state.id, 1, state.meta >> 2);
            return is.func_82833_r();
        }

        if (state.id == Block.pistonExtension.blockID) {
            return I18n.format(state.translator, "piston_head");
        }

        if (state.id == Block.pistonMoving.blockID) {
            return I18n.format(state.translator, "moving_piston");
        }

        if (state.id == Block.sapling.blockID || state.id == Block.leaves.blockID || state.id == Block.wood.blockID) {
            ItemStack is = new ItemStack(state.id, 1, state.meta & 3);
            return is.func_82833_r();
        }

        if (state.id == Block.woodSingleSlab.blockID || state.id == Block.woodDoubleSlab.blockID
            || state.id == Block.stoneSingleSlab.blockID || state.id == Block.stoneDoubleSlab.blockID) {
            ItemStack is = new ItemStack(state.id, 1, state.meta & 7);
            return is.func_82833_r();
        }

        if (state.id == Block.endPortal.blockID) {
            return I18n.format(state.translator, "end_portal");
        }

        return super.getName(state);
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (state.id == Block.field_82512_cj.blockID) {
            int skullType = ((TileEntitySkull) state.te).func_82117_a();
            skullType = skullType <= 4 ? skullType : 0;
            return new ItemStack(Item.field_82799_bQ, 1, skullType);
        }

        return super.getItemStack(state);
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
            } else if (b instanceof BlockNetherStalk) {
                return 3;
            } else if (b instanceof BlockCocoa) {
                return 2;
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
                       || b instanceof BlockStem
                       || b instanceof BlockCocoa;
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

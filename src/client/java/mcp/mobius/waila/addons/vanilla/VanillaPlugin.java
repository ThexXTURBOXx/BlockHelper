package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.BlockRedstoneOre;
import net.minecraft.src.BlockSign;
import net.minecraft.src.BlockStep;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.TileEntityFurnace;

public final class VanillaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new VanillaPlugin();

    static Block mobSpawner = Block.mobSpawner;
    static Block crops = Block.crops;
    static Block lever = Block.lever;
    static Block repeaterIdle = Block.redstoneRepeaterIdle;
    static Block repeaterActv = Block.redstoneRepeaterActive;
    static Block redstone = Block.redstoneWire;
    static Block jukebox = Block.jukebox;
    static Block leave = Block.leaves;
    static Block log = Block.wood;
    static Block sapling = Block.sapling;
    static Block noteBlock = Block.musicBlock;
    static Block sugarCane = Block.reed;
    static Block bed = Block.blockBed;
    static Block pistonExtension = Block.pistonExtension;
    static Block pistonMoving = Block.pistonMoving;
    static Block tallGrass = Block.tallGrass;

    private VanillaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.addSyncedConfig("VanillaMC", "vanilla.showhp");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tame");
        registrar.addSyncedConfig("VanillaMC", "vanilla.sheep");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tnt");

        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, EntityLiving.class);

        registrar.registerBodyProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.furnace");

        registrar.registerBodyProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);

        registrar.registerCropProvider(new DefaultCropProvider(7), BlockCrops.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.jukebox");
        registrar.addSyncedConfig("VanillaMC", "vanilla.noteblock");

        registrar.addConfig("VanillaMC", "vanilla.repeaterol");

        registrar.registerDecorator(HUDDecoratorVanilla.INSTANCE, repeaterIdle.getClass());

        registrar.addConfig("VanillaMC", "vanilla.spawntype");
        registrar.addConfig("VanillaMC", "vanilla.leverstate");
        registrar.addConfig("VanillaMC", "vanilla.repeater");
        registrar.addConfig("VanillaMC", "vanilla.redstone");

        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockRedstoneOre.class);
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, sugarCane.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, crops.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, leave.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, log.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, bed.getClass());

        registrar.registerStackProvider(StackDropFixer.DEFAULT, sapling.getClass());
        registrar.registerStackProvider(StackDropFixer.DEFAULT, BlockStep.class);
        registrar.registerStackProvider(StackDropFixer.DEFAULT, BlockSign.class);
        registrar.registerStackProvider(StackDropFixer.withMetaOverride(0), BlockDoor.class);

        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, mobSpawner.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pistonExtension.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pistonMoving.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, BlockStep.class);
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, tallGrass.getClass());

        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, lever.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, noteBlock.getClass());
    }

}

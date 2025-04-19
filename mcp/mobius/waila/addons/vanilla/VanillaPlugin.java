package mcp.mobius.waila.addons.vanilla;

import cpw.mods.fml.relauncher.Side;
import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockStep;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySkull;

public final class VanillaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new VanillaPlugin();

    public static final Map<Class<?>, Integer> MAX_STAGES = new HashMap<Class<?>, Integer>();

    static Block mobSpawner = Block.mobSpawner;
    static Block crops = Block.crops;
    static Block carrot = Block.carrot;
    static Block potato = Block.potato;
    static Block melonStem = Block.melonStem;
    static Block pumpkinStem = Block.pumpkinStem;
    static Block lever = Block.lever;
    static Block repeaterIdle = Block.redstoneRepeaterIdle;
    static Block repeaterActv = Block.redstoneRepeaterActive;
    static Block comparatorIdl = Block.redstoneComparatorIdle;
    static Block comparatorAct = Block.redstoneComparatorActive;
    static Block redstone = Block.redstoneWire;
    static Block jukebox = Block.jukebox;
    static Block silverfish = Block.silverfish;
    static Block leave = Block.leaves;
    static Block log = Block.wood;
    static Block quartz = Block.blockNetherQuartz;
    static Block anvil = Block.anvil;
    static Block sapling = Block.sapling;
    static Block flowerPot = Block.flowerPot;
    static Block noteBlock = Block.music;
    static Block beacon = Block.beacon;
    static Block endPortal = Block.endPortal;
    static Block cauldron = Block.cauldron;
    static Block sugarCane = Block.reed;
    static Block bed = Block.bed;
    static Block pistonExtension = Block.pistonExtension;
    static Block pistonMoving = Block.pistonMoving;

    private VanillaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        registrar.addSyncedConfig("VanillaMC", "general.showhp");
        registrar.addSyncedConfig("VanillaMC", "vanilla.breed");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tame");
        registrar.addSyncedConfig("VanillaMC", "vanilla.sheep");
        registrar.addSyncedConfig("VanillaMC", "vanilla.villager");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tnt");

        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        if (side.isClient())
            registrar.registerBodyProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.furnace");

        registrar.registerNBTProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);

        if (side.isClient())
            registrar.registerBodyProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);

        if (side.isClient()) {
            registrar.addConfig("General", "general.showcrop");
            registrar.registerBodyProvider(HUDHandlerCrops.INSTANCE, Block.class);
        }

        registrar.addSyncedConfig("VanillaMC", "vanilla.jukebox");
        registrar.addSyncedConfig("VanillaMC", "vanilla.noteblock");
        registrar.addSyncedConfig("VanillaMC", "vanilla.beacon");

        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, comparatorIdl.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, comparatorAct.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, noteBlock.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, beacon.getClass());

        if (side.isClient()) {
            registrar.addConfig("VanillaMC", "vanilla.repeaterol");

            registrar.registerDecorator(HUDDecoratorVanilla.INSTANCE, repeaterIdle.getClass());
            registrar.registerDecorator(HUDDecoratorVanilla.INSTANCE, comparatorIdl.getClass());

            registrar.addConfig("VanillaMC", "vanilla.spawntype");
            registrar.addConfig("VanillaMC", "vanilla.leverstate");
            registrar.addConfig("VanillaMC", "vanilla.repeater");
            registrar.addConfig("VanillaMC", "vanilla.comparator");
            registrar.addConfig("VanillaMC", "vanilla.redstone");
            registrar.addConfig("VanillaMC", "vanilla.silverfish");
            registrar.addConfig("VanillaMC", "vanilla.flowerpot");
            registrar.addConfig("VanillaMC", "vanilla.skull");

            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, silverfish.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockRedstoneOre.class);
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, sugarCane.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, melonStem.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, pumpkinStem.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, crops.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, carrot.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, potato.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, leave.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, log.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, quartz.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, anvil.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, sapling.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, flowerPot.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, cauldron.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, bed.getClass());
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockStep.class);
            registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockWoodSlab.class);

            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, mobSpawner.getClass());
            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, melonStem.getClass());
            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pumpkinStem.getClass());
            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, endPortal.getClass());
            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pistonExtension.getClass());
            registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pistonMoving.getClass());

            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, lever.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, comparatorIdl.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, comparatorAct.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, flowerPot.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, TileEntitySkull.class);
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, noteBlock.getClass());
            registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, beacon.getClass());
        }
    }

}

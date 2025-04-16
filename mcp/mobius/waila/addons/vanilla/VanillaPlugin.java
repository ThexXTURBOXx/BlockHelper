package mcp.mobius.waila.addons.vanilla;

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

public final class VanillaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new VanillaPlugin();

    public static final Map<Class<?>, Integer> MAX_STAGES = new HashMap<Class<?>, Integer>();

    static Block mobSpawner = Block.mobSpawner;
    static Block crops = Block.crops;
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

    private VanillaPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        registrar.addConfig("General", "general.showcrop");

        registrar.registerBodyProvider(HUDHandlerCrops.INSTANCE, Block.class);

        registrar.addConfigRemote("VanillaMC", "general.showhp");

        registrar.registerBodyProvider(HUDHandlerEntities.INSTANCE, Entity.class);
        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addConfigRemote("VanillaMC", "vanilla.furnace");

        registrar.registerBodyProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);
        registrar.registerNBTProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);

        registrar.addConfig("VanillaMC", "vanilla.spawntype");
        registrar.addConfig("VanillaMC", "vanilla.leverstate");
        registrar.addConfig("VanillaMC", "vanilla.repeater");
        registrar.addConfig("VanillaMC", "vanilla.comparator");
        registrar.addConfig("VanillaMC", "vanilla.redstone");
        registrar.addConfig("VanillaMC", "vanilla.silverfish");
        registrar.addConfigRemote("VanillaMC", "vanilla.jukebox");

        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, silverfish.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockRedstoneOre.class);
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, crops.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, leave.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, log.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, quartz.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, anvil.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, sapling.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockStep.class);
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockWoodSlab.class);

        //registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, Block.class);
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, mobSpawner.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, melonStem.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, pumpkinStem.getClass());

        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, lever.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, comparatorIdl.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, comparatorAct.getClass());
        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());

        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, mobSpawner.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, lever.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, repeaterIdle.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, repeaterActv.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, comparatorIdl.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, comparatorAct.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());
        registrar.registerNBTProvider(HUDHandlerVanilla.INSTANCE, silverfish.getClass());
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        registrar.addConfig("VanillaMC", "vanilla.repeaterol");

        registrar.registerDecorator(HUDDecoratorVanilla.INSTANCE, repeaterIdle.getClass());
        registrar.registerDecorator(HUDDecoratorVanilla.INSTANCE, comparatorIdl.getClass());
    }

}

package mcp.mobius.waila.addons.vanilla;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
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

    private VanillaPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        {
            registrar.addConfig("General", "general.showcrop");

            registrar.registerBodyProvider(new HUDHandlerCrops(), Block.class);

            try {
                Class<?> CropBlock = Class.forName("mods.natura.blocks.crops.CropBlock");
                MAX_STAGES.put(CropBlock, 3);
            } catch (Throwable t) {
                mod_BlockHelper.LOG.log(Level.WARNING, "[Natura] Error while loading crop hooks.", t);
            }
        }

        {
            registrar.addConfigRemote("VanillaMC", "general.showhp");

            IEntityProvider provider = new HUDHandlerEntities();
            registrar.registerBodyProvider(provider, Entity.class);
            registrar.registerNBTProvider(provider, Entity.class);
        }

        {
            IDataProvider provider = new HUDHandlerFurnace();
            registrar.registerBodyProvider(provider, TileEntityFurnace.class);
            registrar.registerNBTProvider(provider, TileEntityFurnace.class);
        }

        {
            registrar.addConfig("VanillaMC", "vanilla.spawntype");
            registrar.addConfig("VanillaMC", "vanilla.leverstate");
            registrar.addConfig("VanillaMC", "vanilla.repeater");
            registrar.addConfig("VanillaMC", "vanilla.comparator");
            registrar.addConfig("VanillaMC", "vanilla.redstone");
            registrar.addConfig("VanillaMC", "vanilla.silverfish");
            registrar.addConfigRemote("VanillaMC", "vanilla.jukebox");

            IDataProvider provider = new HUDHandlerVanilla();

            registrar.registerStackProvider(provider, silverfish.getClass());
            registrar.registerStackProvider(provider, redstone.getClass());
            registrar.registerStackProvider(provider, BlockRedstoneOre.class);
            registrar.registerStackProvider(provider, crops.getClass());
            registrar.registerStackProvider(provider, leave.getClass());
            registrar.registerStackProvider(provider, log.getClass());
            registrar.registerStackProvider(provider, quartz.getClass());

            //registrar.registerStackProvider(provider, Block.class);
            registrar.registerHeadProvider(provider, mobSpawner.getClass());
            registrar.registerHeadProvider(provider, melonStem.getClass());
            registrar.registerHeadProvider(provider, pumpkinStem.getClass());

            registrar.registerBodyProvider(provider, lever.getClass());
            registrar.registerBodyProvider(provider, repeaterIdle.getClass());
            registrar.registerBodyProvider(provider, repeaterActv.getClass());
            registrar.registerBodyProvider(provider, comparatorIdl.getClass());
            registrar.registerBodyProvider(provider, comparatorAct.getClass());
            registrar.registerHeadProvider(provider, redstone.getClass());
            registrar.registerBodyProvider(provider, redstone.getClass());
            registrar.registerBodyProvider(provider, jukebox.getClass());

            registrar.registerNBTProvider(provider, mobSpawner.getClass());
            registrar.registerNBTProvider(provider, lever.getClass());
            registrar.registerNBTProvider(provider, repeaterIdle.getClass());
            registrar.registerNBTProvider(provider, repeaterActv.getClass());
            registrar.registerNBTProvider(provider, comparatorIdl.getClass());
            registrar.registerNBTProvider(provider, comparatorAct.getClass());
            registrar.registerNBTProvider(provider, redstone.getClass());
            registrar.registerNBTProvider(provider, jukebox.getClass());
            registrar.registerNBTProvider(provider, silverfish.getClass());
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        {
            registrar.addConfig("VanillaMC", "vanilla.repeaterol");

            IBlockDecorator decorator = new HUDDecoratorVanilla();
            registrar.registerDecorator(decorator, repeaterIdle.getClass());
            registrar.registerDecorator(decorator, comparatorIdl.getClass());
        }
    }

}

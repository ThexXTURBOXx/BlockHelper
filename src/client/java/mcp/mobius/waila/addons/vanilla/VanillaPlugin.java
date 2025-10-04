package mcp.mobius.waila.addons.vanilla;

import java.lang.reflect.Field;
import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.BlockRedstoneOre;
import net.minecraft.src.BlockSign;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.ItemRecord;
import net.minecraft.src.TileEntityFurnace;

public final class VanillaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new VanillaPlugin();

    static Block mobSpawner = Block.mobSpawner;
    static Block crops = Block.crops;
    static Block lever = Block.lever;
    static Block redstone = Block.redstoneWire;
    static Block jukebox = Block.jukebox;
    static Block leave = Block.leaves;
    static Block log = Block.wood;
    static Block sapling = Block.sapling;
    static Block sugarCane = Block.reed;

    static Field ItemRecord_recordName;

    private VanillaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            ItemRecord_recordName = AccessHelper.getDeclaredField(ItemRecord.class, "recordName", "a");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        registrar.addSyncedConfig("VanillaMC", "vanilla.showhp");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tnt");

        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, EntityLiving.class);

        registrar.registerStackProvider(HUDHandlerEntities.INSTANCE, EntityTNTPrimed.class);

        registrar.registerHeadProvider(HUDHandlerEntities.INSTANCE, EntityPlayer.class);

        registrar.registerBodyProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.furnace");

        registrar.registerBodyProvider(HUDHandlerFurnace.INSTANCE, TileEntityFurnace.class);

        registrar.registerCropProvider(new DefaultCropProvider(7), BlockCrops.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.jukebox");

        registrar.addConfig("VanillaMC", "vanilla.spawntype");
        registrar.addConfig("VanillaMC", "vanilla.leverstate");
        registrar.addConfig("VanillaMC", "vanilla.redstone");

        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, BlockRedstoneOre.class);
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, sugarCane.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, crops.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, leave.getClass());
        registrar.registerStackProvider(HUDHandlerVanilla.INSTANCE, log.getClass());

        registrar.registerStackProvider(StackDropFixer.DEFAULT, sapling.getClass());
        registrar.registerStackProvider(StackDropFixer.DEFAULT, BlockSign.class);
        registrar.registerStackProvider(StackDropFixer.withMetaOverride(0), BlockDoor.class);

        registrar.registerHeadProvider(HUDHandlerVanilla.INSTANCE, mobSpawner.getClass());

        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, lever.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, redstone.getClass());
        registrar.registerBodyProvider(HUDHandlerVanilla.INSTANCE, jukebox.getClass());
    }

}

package mcp.mobius.waila.addons.core;

import java.lang.reflect.Field;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.LangUtil;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class HUDHandlerBlocks implements IDataProvider {

    private static Field curBlockDamageMP;

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {

        String name = null;
        try {
            String s = DisplayUtil.itemDisplayNameShort(itemStack);
            if (s != null && !s.endsWith("Unnamed"))
                name = s;

            if (name != null)
                currenttip.add(name);
        } catch (Throwable ignored) {
        }

        if (itemStack.getItem() == Item.redstone) {
            int md = accessor.getMetadata();
            String s = "" + md;
            if (s.length() < 2)
                s = " " + s;
            currenttip.set(currenttip.size() - 1, name + " " + s);
        }

        if (currenttip.isEmpty())
            currenttip.add("< Unnamed >");
        else {
            if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA,
                    true)) {
                currenttip.add(String.format(ITALIC + "ID %d:%d", accessor.getBlockID(), accessor.getMetadata()));
            }
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block b = accessor.getBlock();
        int meta = accessor.getMetadata();
        World w = accessor.getWorld();
        int x = accessor.getPosition().blockX;
        int y = accessor.getPosition().blockY;
        int z = accessor.getPosition().blockZ;

		/*
		if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHIFTBLOCK, false)
		&& currenttip.size() > 0 && !accessor.getPlayer().isSneaking()){
			currenttip.clear();
			currenttip.add(ITALIC + "Press shift for more data");
		}
		*/

        if (PluginConfig.instance().get("general.harvest")) {
            String harvest = "hud.msg.please_report";
            if (b != null) {
                if (b.getBlockHardness(w, x, y, z) < 0.0F) {
                    harvest = "hud.msg.unbreakable";
                } else if (b.canHarvestBlock(accessor.getPlayer(), meta)) {
                    harvest = "hud.msg.harvestable";
                } else {
                    harvest = "hud.msg.not_harvestable";
                }
            }
            currenttip.add(LangUtil.translateG(harvest));
        }

        if (PluginConfig.instance().get("general.lightlevel") &&
            SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, w, x, y + 1, z)) {
            int blockLightLevel = w.getSavedLightValue(EnumSkyBlock.Block, x, y + 1, z);
            String blockLight = (blockLightLevel <= 7 ? "§4" : "§a") + blockLightLevel;
            String skyLight = w.getSavedLightValue(EnumSkyBlock.Sky, x, y + 1, z) + "";
            currenttip.add(LangUtil.translateG("hud.msg.light_level", blockLight, skyLight));
        }

        if (PluginConfig.instance().get("general.break")) {
            try {
                float curBlockDamage = curBlockDamageMP.getFloat(Minecraft.getMinecraft().playerController);
                if (curBlockDamage > 0) {
                    String progress = MathHelper.floor_float(100 * curBlockDamage) + "%";
                    currenttip.add(LangUtil.translateG("hud.msg.break_progression", progress));
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        String modName = ModIdentification.nameFromStack(itemStack);
        if (modName != null && !modName.isEmpty()) {
            currenttip.add(BLUE + ITALIC + modName);
        }
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
    }

    public static void register() {
        WailaRegistrar.instance().addConfig("General", "general.harvest");
        WailaRegistrar.instance().addConfig("General", "general.lightlevel");
        WailaRegistrar.instance().addConfig("General", "general.break");

        HUDHandlerBlocks provider = new HUDHandlerBlocks();
        WailaRegistrar.instance().registerHeadProvider(provider, Block.class);
        WailaRegistrar.instance().registerBodyProvider(provider, Block.class);
        WailaRegistrar.instance().registerTailProvider(provider, Block.class);

        try {
            curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("curBlockDamageMP");
            curBlockDamageMP.setAccessible(true);
        } catch (Throwable t) {
            try {
                curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("field_78770_f");
                curBlockDamageMP.setAccessible(true);
            } catch (Throwable t1) {
                throw new RuntimeException(t1);
            }
        }
    }

}

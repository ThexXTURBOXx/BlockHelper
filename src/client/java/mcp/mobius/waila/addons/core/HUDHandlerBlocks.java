package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.ModIdentification;
import mcp.mobius.waila.utils.SpawnUtil;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.src.Block;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper.Accessor;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.DRED;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.YELLOW;

public final class HUDHandlerBlocks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBlocks();

    private HUDHandlerBlocks() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            String s = DisplayUtil.itemDisplayNameShort(itemStack);
            String name = null;
            if (s != null && !s.endsWith("Unnamed"))
                name = s;

            if (name != null)
                currenttip.add(name);
        } catch (Throwable ignored) {
        }

        if (currenttip.isEmpty())
            currenttip.add(I18n.translate("hud.msg.please_report"));
        if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA, true))
            currenttip.add(ITALIC + "ID " + accessor.getBlockID() + ":" + accessor.getMetadata());
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

        if (config.get("general.harvest")) {
            String harvest = "hud.msg.please_report";
            if (b != null) {
                if (Accessor.getHardness(b, meta) < 0.0F) {
                    harvest = "hud.msg.unbreakable";
                } else if (Accessor.canHarvestBlock(b, accessor.getPlayer(), meta)) {
                    harvest = "hud.msg.harvestable";
                } else {
                    harvest = "hud.msg.not_harvestable";
                }
            }
            currenttip.add(I18n.translate(harvest));
        }

        if (config.get("general.lightlevel") &&
            (!w.isBlockNormalCubeDefault(x, y + 1, z, false) || w.isAirBlock(x, y + 1, z))) {
            int blockLightLevel = w.getSavedLightValue(EnumSkyBlock.Block, x, y + 1, z);
            byte spawnMode = SpawnUtil.getSpawnMode(w, x, y + 1, z);
            String blockLight = (spawnMode == 0 ? GREEN : (spawnMode == 1 ? YELLOW : DRED)) + blockLightLevel;
            String skyLight = w.getSavedLightValue(EnumSkyBlock.Sky, x, y + 1, z) + "";
            currenttip.add(I18n.translate("hud.msg.light_level") + ": " + blockLight + YELLOW + " (" + skyLight + ")");
        }

        if (config.get("general.break")) {
            try {
                float curBlockDamage = ModLoader.getMinecraftInstance().renderGlobal.damagePartialTime;
                if (curBlockDamage > 0) {
                    String progress = MathHelper.floor_float(100 * curBlockDamage) + "%";
                    currenttip.add(I18n.translate("hud.msg.break_progression") + ": " + progress);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        String modName = ModIdentification.identifyMod(itemStack);
        if (modName.isEmpty())
            modName = ModIdentification.identifyMod(accessor.getTileEntity());
        if (!modName.isEmpty())
            currenttip.add(BLUE + ITALIC + modName);
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te != null)
            te.writeToNBT(tag);
    }
}

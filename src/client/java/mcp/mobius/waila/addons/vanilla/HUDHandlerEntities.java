package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntities implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntities();

    private HUDHandlerEntities() {
    }

    public static int nhearts = 20;
    public static float maxhpfortext = 40.0f;

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        if (accessor.getEntity() instanceof EntityTNTPrimed)
            return new ItemStack(Block.tnt);
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (entity instanceof EntityPlayer)
            currenttip.replaceFirstTagEntry(WHITE + ((EntityPlayer) entity).field_771_i,
                    mcp.mobius.waila.addons.core.HUDHandlerEntities.ENTITY_NAME_TAG);
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.showhp"))
            if (entity instanceof EntityLiving) {
                nhearts = nhearts <= 0 ? 20 : nhearts;

                NBTTagCompound tag = accessor.getNBTData();
                float health = accessor.getNBTInteger(tag, "Health");
                float healthHearts = health / 2.0f;

                if (health > maxhpfortext)
                    currenttip.add(String.format("%.0f \u2764", health));
                else
                    currenttip.add(TTRenderHealth.create(nhearts, healthHearts, healthHearts));
            }

        if (config.get("vanilla.chicken"))
            if (entity instanceof EntityChicken) {
                String eggSeconds = String.format("%.2f", accessor.getNBTInteger("NextEgg") / 20f);
                currenttip.add(I18n.translate("hud.msg.next_egg") + ": " +
                               I18n.translate("hud.msg.seconds_format", eggSeconds));
            }

        if (config.get("vanilla.tnt"))
            if (entity instanceof EntityTNTPrimed) {
                String fuseSeconds = String.format("%.2f", accessor.getNBTInteger("Fuse") / 20f);
                currenttip.add(I18n.translate("hud.msg.fuse") + ": " +
                               I18n.translate("hud.msg.seconds_format", fuseSeconds));
            }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        if (ent instanceof EntityChicken)
            tag.setInteger("NextEgg", ((EntityChicken) ent).timeUntilNextEgg);
    }

}

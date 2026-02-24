package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.StringUtils;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityVillage;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.isWheat;
import static mcp.mobius.waila.api.SpecialChars.HEART;
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
            currenttip.set(0, WHITE + ((EntityPlayer) entity).username);
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.showhp"))
            if (entity instanceof EntityLiving) {
                nhearts = nhearts <= 0 ? 20 : nhearts;

                NBTTagCompound tag = accessor.getNBTData();
                float health = accessor.getNBTInteger(tag, "Health");
                float maxhp = accessor.getNBTInteger(tag, "MaxHealth");
                float healthHearts = health / 2.0f;
                float maxhpHearts = maxhp / 2.0f;

                if (maxhp > maxhpfortext)
                    currenttip.add(String.format("%.0f " + HEART + " / %.0f " + HEART, health, maxhp));
                else
                    currenttip.add(TTRenderHealth.create(nhearts, healthHearts, maxhpHearts));
            }

        if (config.get("vanilla.breed"))
            if (entity instanceof EntityAnimal) {
                int age = accessor.getNBTInteger("Age");
                if (age < 0) {
                    currenttip.add(I18n.translate("hud.msg.adult_in") + ": " +
                                   I18n.translate("hud.msg.seconds_format", -age / 20));
                } else {
                    if (age > 0) {
                        currenttip.add(I18n.translate("hud.msg.cooldown") + ": " +
                                       I18n.translate("hud.msg.seconds_format", age / 20));
                    } else {
                        EntityAnimal animal = (EntityAnimal) entity;
                        int inLove = accessor.getNBTInteger("InLove");
                        try {
                            if (inLove != 0)
                                currenttip.add(I18n.translate("hud.msg.in_love") + ": " +
                                               I18n.translate("hud.msg.seconds_format", inLove / 20));
                            else if (accessor.getPlayer().getCurrentEquippedItem() != null &&
                                     (Boolean) isWheat.invoke(animal, accessor.getPlayer().getCurrentEquippedItem()))
                                currenttip.add(I18n.translate("hud.msg.can_be_bred"));
                        } catch (Throwable t) {
                            WailaExceptionHandler.handleErr(t, animal.getClass(), currenttip);
                        }
                    }
                }
            }

        if (config.get("vanilla.tame")) {
            if (entity instanceof EntityWolf) {
                String ownerName = accessor.getNBTData().getString("Owner");
                boolean isTamed = ownerName != null && !ownerName.isEmpty();
                if (isTamed) {
                    currenttip.add(I18n.translate("hud.msg.owner") + ": " + ownerName);

                    int collarColor = accessor.getNBTInteger("CollarColor");
                    currenttip.add(I18n.translate("hud.msg.collar") + ": " +
                                   I18n.color(BlockCloth.getDyeFromBlock(collarColor)));
                }
                boolean angry = accessor.getNBTData().getBoolean("Angry");
                if (angry)
                    currenttip.add(I18n.translate("hud.msg.state") + ": " +
                                   I18n.translate("hud.msg.angry"));
            }
        }

        if (config.get("vanilla.sheep"))
            if (entity instanceof EntitySheep) {
                currenttip.add(I18n.translate("hud.msg.color") + ": " +
                               I18n.color(BlockCloth.getDyeFromBlock(accessor.getNBTInteger("Color"))));
            }

        if (config.get("vanilla.villager")) {
            int conversionTime;
            if (entity instanceof EntityZombie &&
                accessor.getNBTData().getBoolean("IsVillager") &&
                (conversionTime = accessor.getNBTInteger("ConversionTime")) > -1) {
                currenttip.add(I18n.translate("hud.msg.converting_to_villager") + ": " +
                               I18n.translate("hud.msg.seconds_format", conversionTime / 20));
            }

            if (entity instanceof EntityVillage) {
                String profession = "hud.msg.villager.modded";
                int professionId = accessor.getNBTInteger("Profession");
                switch (professionId) {
                case 0:
                    profession = "hud.msg.villager.farmer";
                    break;
                case 1:
                    profession = "hud.msg.villager.librarian";
                    break;
                case 2:
                    profession = "hud.msg.villager.priest";
                    break;
                case 3:
                    profession = "hud.msg.villager.smith";
                    break;
                case 4:
                    profession = "hud.msg.villager.butcher";
                    break;
                }
                currenttip.add(I18n.translate("hud.msg.profession") + ": " +
                               StringUtils.firstCharacterUppercase(I18n.translate(profession)
                                       .replaceFirst("hud\\.msg\\.villager\\.", "")));
            }
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
        if (ent instanceof EntityLiving)
            tag.setInteger("MaxHealth", ((EntityLiving) ent).func_40117_c());
    }

}

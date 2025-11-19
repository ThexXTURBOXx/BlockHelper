package mcp.mobius.waila.addons.vanilla;

import cpw.mods.fml.common.registry.VillagerRegistry;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityTameable;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.api.SpecialChars.HEART;
import static mcp.mobius.waila.api.SpecialChars.getRenderString;

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
        if (accessor.getEntity() instanceof EntityPlayer) {
            ItemStack skull = new ItemStack(Item.field_82799_bQ, 1, 3);
            String playerName = ((EntityPlayer) accessor.getEntity()).username;
            if (playerName != null && !playerName.isEmpty()) {
                skull.setTagCompound(new NBTTagCompound());
                skull.getTagCompound().setString("SkullOwner", playerName);
            }
            return skull;
        }
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
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
                    currenttip.add(getRenderString("waila.health", nhearts, healthHearts, maxhpHearts));
            }

        if (config.get("vanilla.breed"))
            if (entity instanceof EntityAgeable) {
                int age = accessor.getNBTInteger("Age");
                if (age < 0) {
                    currenttip.add(I18n.translate("hud.msg.adult_in") + ": " +
                                   I18n.translate("hud.msg.seconds_format", -age / 20));
                } else {
                    if (age > 0) {
                        currenttip.add(I18n.translate("hud.msg.cooldown") + ": " +
                                       I18n.translate("hud.msg.seconds_format", age / 20));
                    } else if (entity instanceof EntityAnimal) {
                        EntityAnimal animal = (EntityAnimal) entity;
                        int inLove = accessor.getNBTInteger("InLove");
                        if (inLove != 0)
                            currenttip.add(I18n.translate("hud.msg.in_love") + ": " +
                                           I18n.translate("hud.msg.seconds_format", inLove / 20));
                        else if (accessor.getPlayer().getCurrentEquippedItem() != null &&
                                 animal.isWheat(accessor.getPlayer().getCurrentEquippedItem()))
                            currenttip.add(I18n.translate("hud.msg.can_be_bred"));
                    }
                }
            }

        if (config.get("vanilla.tame")) {
            if (entity instanceof EntityTameable) {
                String ownerName = accessor.getNBTData().getString("Owner");
                boolean isTamed = ownerName != null && !ownerName.isEmpty();
                if (isTamed)
                    currenttip.add(I18n.translate("hud.msg.owner") + ": " + ownerName);

                if (entity instanceof EntityWolf) {
                    if (isTamed) {
                        int collarColor = accessor.getNBTInteger("CollarColor");
                        currenttip.add(I18n.translate("hud.msg.collar") + ": " +
                                       I18n.color(BlockCloth.getDyeFromBlock(collarColor)));
                    }
                    boolean angry = accessor.getNBTData().getBoolean("Angry");
                    if (angry)
                        currenttip.add(I18n.translate("hud.msg.state") + ": " +
                                       I18n.translate("hud.msg.angry"));
                }

                if (entity instanceof EntityOcelot && isTamed) {
                    String breed = "hud.msg.please_report";
                    switch (accessor.getNBTInteger("CatType")) {
                    case 0:
                        // Usually should not happen since it is tamed
                        breed = "hud.msg.cat.ocelote";
                        break;
                    case 1:
                        breed = "hud.msg.cat.black";
                        break;
                    case 2:
                        breed = "hud.msg.cat.red";
                        break;
                    case 3:
                        breed = "hud.msg.cat.siamese";
                        break;
                    }
                    currenttip.add(I18n.translate("hud.msg.breed") + ": " + I18n.translate(breed));
                }
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

            if (entity instanceof EntityVillager) {
                String profession = null;
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
                if (profession == null) {
                    String skin = VillagerRegistry.getVillagerSkin(professionId, null);
                    if (skin == null)
                        profession = "hud.msg.villager.modded";
                    else
                        // Strip path and ".png"
                        profession = "hud.msg.villager." + skin.substring(skin.lastIndexOf("/") + 1, skin.length() - 4);
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
            tag.setInteger("MaxHealth", ((EntityLiving) ent).getMaxHealth());
    }

}

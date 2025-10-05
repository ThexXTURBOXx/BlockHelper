package mcp.mobius.waila.addons.core;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.StringUtils;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IMob;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.core.CorePlugin.getDropItemId;
import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntities implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntities();

    private HUDHandlerEntities() {
    }

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        if (accessor.getEntity() instanceof EntityLiving) {
            try {
                EntityLiving living = (EntityLiving) accessor.getEntity();
                int dropId = (Integer) getDropItemId.invoke(living);
                if (dropId > 0)
                    return new ItemStack(dropId, 1, 0);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass(), null);
            }
        }
        return accessor.getEntity().getPickedResult(accessor.getPosition());
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        String color = entity instanceof IMob ? RED : WHITE;

        retrieve:
        try {
            String entityName = entity.getEntityName();
            if (entityName == null || entityName.isEmpty()) break retrieve;

            if (entityName.startsWith("entity.") && entityName.endsWith(".name")) {
                entityName = I18n.translate(entityName);
                if (entityName == null || entityName.isEmpty()) break retrieve;
            }

            currenttip.add(color + entityName);
            return;
        } catch (Throwable ignored) {
        }

        try {
            String entityName = entity.getClass().getSimpleName();
            entityName = entityName.replaceFirst("Entity", "");
            if (!entityName.isEmpty()) {
                currenttip.add(color + StringUtils.firstCharacterUppercase(entityName));
                return;
            }
        } catch (Throwable ignored) {
        }

        currenttip.add(color + I18n.translate("hud.msg.please_report"));
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            currenttip.add(BLUE + ITALIC + getEntityMod(entity));
        } catch (Throwable t) {
            currenttip.add(BLUE + ITALIC + I18n.translate("hud.msg.unknown"));
        }
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        if (ent != null)
            ent.writeToNBT(tag);
    }

    private static String getEntityMod(Entity entity) {
        EntityRegistration er = EntityRegistry.instance().lookupModSpawn(entity.getClass(), true);
        if (er == null) return "Minecraft";
        ModContainer mod = er.getContainer();
        if (mod == null) return "Minecraft";
        return mod.getName();
    }

}

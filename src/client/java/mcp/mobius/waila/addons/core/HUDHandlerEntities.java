package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.ModIdentification;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IMobs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.core.CorePlugin.getDropItemId;
import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerEntities implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntities();

    public static final String ENTITY_NAME_TAG = "BHCORE_EntityName";
    public static final String ENTITY_MOD_NAME_TAG = "BHCORE_EntityModName";

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
        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        String color = entity instanceof IMobs ? RED : WHITE;

        retrieve:
        try {
            String entityName = EntityList.getEntityString(entity);
            if (entityName == null || entityName.isEmpty()) break retrieve;

            if (entityName.startsWith("entity.") && entityName.endsWith(".name")) {
                entityName = I18n.translate(entityName);
                if (entityName == null || entityName.isEmpty()) break retrieve;
            }

            currenttip.add(color + entityName, ENTITY_NAME_TAG);
            return;
        } catch (Throwable ignored) {
        }

        currenttip.add(color + I18n.translate("hud.msg.please_report"), ENTITY_NAME_TAG);
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        try {
            currenttip.add(formatModName(ModIdentification.identifyMod(entity)), ENTITY_MOD_NAME_TAG);
        } catch (Throwable t) {
            currenttip.add(formatModName(I18n.translate("hud.msg.unknown")), ENTITY_MOD_NAME_TAG);
        }
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        if (ent != null)
            ent.writeToNBT(tag);
    }

    public static String formatModName(String modName) {
        return BLUE + ITALIC + modName;
    }

}

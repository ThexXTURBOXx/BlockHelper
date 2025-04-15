package mcp.mobius.waila.addons.core;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
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
        return accessor.getEntity().getPickedResult(accessor.getPosition());
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (entity instanceof EntityItemFrame
            || (entity instanceof EntityOcelot
                && !((EntityOcelot) entity).func_94056_bM()
                && ((EntityOcelot) entity).isTamed())) {
            currenttip.add(WHITE + LangUtil.translateG(entity.getTranslatedEntityName()));
        } else {
            try {
                currenttip.add(WHITE + entity.getTranslatedEntityName());
            } catch (Throwable t) {
                currenttip.add(WHITE + "Unknown");
            }
        }
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
            currenttip.add(BLUE + ITALIC + "Unknown");
        }
    }

    @Override
    public void appendServerData(EntityPlayerMP player, Entity te, NBTTagCompound tag, World world) {
    }

    private static String getEntityMod(Entity entity) {
        try {
            EntityRegistration er = EntityRegistry.instance().lookupModSpawn(entity.getClass(), true);
            ModContainer modC = er.getContainer();
            return modC.getName();
        } catch (NullPointerException e) {
            return "Minecraft";
        }
    }

}

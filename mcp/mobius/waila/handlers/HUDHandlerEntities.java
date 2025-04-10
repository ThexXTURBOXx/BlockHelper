package mcp.mobius.waila.handlers;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerEntities implements IWailaEntityProvider {

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (entity instanceof EntityItemFrame
            || (entity instanceof EntityOcelot
                && !((EntityOcelot) entity).func_94056_bM()
                && ((EntityOcelot) entity).isTamed())) {
            currenttip.add(WHITE + LangUtil.translateG(entity.getTranslatedEntityName()));
        } else {
            try {
                currenttip.add(WHITE + entity.getTranslatedEntityName());
            } catch (Exception e) {
                currenttip.add(WHITE + "Unknown");
            }
        }
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        try {
            currenttip.add(BLUE + ITALIC + getEntityMod(entity));
        } catch (Exception e) {
            currenttip.add(BLUE + ITALIC + "Unknown");
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity te, NBTTagCompound tag, World world) {
        return tag;
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

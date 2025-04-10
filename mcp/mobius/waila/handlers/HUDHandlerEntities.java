package mcp.mobius.waila.handlers;

import java.util.List;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import static mcp.mobius.waila.api.SpecialChars.*;

public class HUDHandlerEntities implements IWailaEntityProvider {

	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		try{
			currenttip.add(WHITE + entity.getTranslatedEntityName());
		} catch (Exception e){
			currenttip.add(WHITE + "Unknown");
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		try{
			currenttip.add(BLUE + ITALIC + getEntityMod(entity));
		} catch (Exception e){
			currenttip.add(BLUE + ITALIC + "Unknown");
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity te, NBTTagCompound tag, World world) {
		return tag;
	}

    private static String getEntityMod(Entity entity){
    	String modName = "";
    	try{
    		EntityRegistration er = EntityRegistry.instance().lookupModSpawn(entity.getClass(), true);
    		ModContainer modC = er.getContainer();
    		modName = modC.getName();
    	} catch (NullPointerException e){
    		modName = "Minecraft";
    	}
    	return modName;
    }

}

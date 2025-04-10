package mcp.mobius.waila.addons.vanillamc;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.WHITE;
import static mcp.mobius.waila.api.SpecialChars.getRenderString;

public class HUDHandlerEntities implements IWailaEntityProvider {

    public static int nhearts = 20;
    public static float maxhpfortext = 40.0f;

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public ITaggedList<String, String> getWailaHead(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaBody(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig("general.showhp"))
            if (entity instanceof EntityLiving) {
                nhearts = nhearts <= 0 ? 20 : nhearts;

                NBTTagCompound tag = accessor.getNBTData();
                float health = accessor.getNBTInteger(tag, "Health");
                float maxhp = accessor.getNBTInteger(tag, "MaxHealth");
                float healthHearts = health / 2.0f;
                float maxhpHearts = maxhp / 2.0f;

                if (maxhp > maxhpfortext)
                    currenttip.add(String.format("HP : " + WHITE + "%.0f" + GRAY + " / " + WHITE + "%.0f", health,
                            maxhp));

                else {
                    currenttip.add(getRenderString("waila.health", String.valueOf(nhearts),
                            String.valueOf(healthHearts), String.valueOf(maxhpHearts)));
                }
            }

        return currenttip;
    }

    @Override
    public ITaggedList<String, String> getWailaTail(Entity entity, ITaggedList<String, String> currenttip,
                                                    IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (ent != null)
            ent.writeToNBT(tag);
        if (ent instanceof EntityLiving)
            tag.setInteger("MaxHealth", ((EntityLiving) ent).getMaxHealth());
        return tag;
    }

    public static void register() {
        ModuleRegistrar.instance().addConfigRemote("VanillaMC", "general.showhp");

        IWailaEntityProvider provider = new HUDHandlerEntities();

        ModuleRegistrar.instance().registerBodyProvider(provider, Entity.class);

        ModuleRegistrar.instance().registerNBTProvider(provider, Entity.class);
    }

}

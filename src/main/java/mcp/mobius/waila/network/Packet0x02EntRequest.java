package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldServer;

public class Packet0x02EntRequest implements IWailaPacket {

    private int id;

    public Packet0x02EntRequest() {
    }

    public Packet0x02EntRequest(Entity ent) {
        this.id = ent.entityId;
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeInt(id);
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            id = dat.readInt();
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass(), null);
        }
    }

    @Override
    public void handleClient() {
    }

    @Override
    public void handleServer(Player rawSender) {
        if (!(rawSender instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) rawSender;
        WorldServer world = player.getServerForPlayer();
        if (world == null) return;
        Entity entity = world.getEntityByID(id);
        if (entity == null || player.getDistanceSqToEntity(entity) > MAX_REACH_SQ) return;

        try {
            NBTTagCompound tag = new NBTTagCompound();

            if (WailaRegistrar.instance().hasNBTEntityProviders(entity)) {
                ServerDataAccessorCommon accessor = ServerDataAccessorCommon.INSTANCE;
                accessor.set(world, player, entity);

                for (List<IEntityProvider> providersList :
                        WailaRegistrar.instance().getNBTEntityProviders(entity).values()) {
                    for (IEntityProvider provider : providersList) {
                        try {
                            provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                        } catch (Throwable t) {
                            WailaExceptionHandler.handleErr(t, entity.getClass(), null);
                        }
                    }
                }
            }

            tag.setInteger("WailaEntityID", entity.entityId);

            WailaPacketHandler.sendPacketToPlayer(new Packet0x03NBTData(tag), rawSender);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass(), null);
        }
    }

}

package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

public class Packet0x02EntRequest implements IWailaPacket {

    public int id;

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
    public void handleServer(EntityPlayerSP sender) {
        World world = sender.worldObj;
        if (world == null) return;
        Entity entity = mod_BlockHelper.Accessor.getEntityByID(world, id);
        if (entity == null) return;

        try {
            NBTTagCompound tag = new NBTTagCompound();
            if (WailaRegistrar.instance().hasNBTEntityProviders(entity)) {
                ServerDataAccessorCommon accessor = ServerDataAccessorCommon.INSTANCE;
                accessor.set(world, sender, entity);

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

            WailaPacketHandler.sendPacketToPlayer(new Packet0x03NBTData(tag));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass(), null);
        }
    }

}

package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;
import net.minecraft.server.mod_BlockHelper;

public class Packet0x02EntRequest implements IWailaPacket {

    private int id;

    public Packet0x02EntRequest() {
    }

    public Packet0x02EntRequest(Entity ent) {
        this.id = ent.id;
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
            WailaExceptionHandler.handleErr(t, this.getClass());
        }
    }

    @Override
    public void handleServer(EntityPlayer sender) {
        World world = sender.world;
        if (world == null) return;
        Entity entity = mod_BlockHelper.Accessor.getEntityByID(world, id);
        if (entity == null || sender.g(entity) > MAX_REACH_SQ) return;

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
                            WailaExceptionHandler.handleErr(t, entity.getClass());
                        }
                    }
                }
            }

            tag.a("WailaEntityID", entity.id);

            WailaPacketHandler.sendPacketToPlayer(new Packet0x03NBTData(tag), sender);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass());
        }
    }

}

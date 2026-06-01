package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.Block;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class Packet0x01TileRequest implements IWailaPacket {

    private int posX;
    private int posY;
    private int posZ;

    public Packet0x01TileRequest() {
    }

    public Packet0x01TileRequest(TileEntity ent) {
        this.posX = ent.x;
        this.posY = ent.y;
        this.posZ = ent.z;
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeInt(posX);
        target.writeInt(posY);
        target.writeInt(posZ);
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            posX = dat.readInt();
            posY = dat.readInt();
            posZ = dat.readInt();
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass());
        }
    }

    @Override
    public void handleServer(EntityPlayer sender) {
        World world = sender.world;
        if (world == null || sender.e(posX, posY, posZ) > MAX_REACH_SQ) return;
        TileEntity entity = world.getTileEntity(posX, posY, posZ);
        Block block = Block.byId[world.getTypeId(posX, posY, posZ)];
        if (entity == null) return;

        try {
            NBTTagCompound tag = new NBTTagCompound();

            boolean hasNBTBlock = WailaRegistrar.instance().hasNBTProviders(block);
            boolean hasNBTEnt = WailaRegistrar.instance().hasNBTProviders(entity);

            if (hasNBTBlock || hasNBTEnt) {
                ServerDataAccessorCommon accessor = ServerDataAccessorCommon.INSTANCE;
                accessor.set(world, sender, entity, posX, posY, posZ);

                if (hasNBTBlock) {
                    for (List<IDataProvider> providersList :
                            WailaRegistrar.instance().getNBTProviders(block).values()) {
                        for (IDataProvider provider : providersList) {
                            try {
                                provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                            } catch (Throwable t) {
                                WailaExceptionHandler.handleErr(t, block.getClass());
                            }
                        }
                    }
                }

                if (hasNBTEnt) {
                    for (List<IDataProvider> providersList :
                            WailaRegistrar.instance().getNBTProviders(entity).values()) {
                        for (IDataProvider provider : providersList) {
                            try {
                                provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                            } catch (Throwable t) {
                                WailaExceptionHandler.handleErr(t, entity.getClass());
                            }
                        }
                    }
                }
            }

            tag.setInt("WailaX", posX);
            tag.setInt("WailaY", posY);
            tag.setInt("WailaZ", posZ);

            WailaPacketHandler.sendPacketToPlayer(new Packet0x03NBTData(tag), sender);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass());
        }
    }

}

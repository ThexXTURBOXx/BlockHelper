package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class Packet0x01TileRequest implements IWailaPacket {

    public int posX;
    public int posY;
    public int posZ;

    public Packet0x01TileRequest() {
    }

    public Packet0x01TileRequest(TileEntity ent) {
        this.posX = ent.xCoord;
        this.posY = ent.yCoord;
        this.posZ = ent.zCoord;
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
            WailaExceptionHandler.handleErr(t, this.getClass().toString(), null);
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
        TileEntity entity = world.getBlockTileEntity(posX, posY, posZ);
        Block block = Block.blocksList[world.getBlockId(posX, posY, posZ)];
        if (entity == null) return;

        try {
            NBTTagCompound tag = new NBTTagCompound();

            boolean hasNBTBlock = WailaRegistrar.instance().hasNBTProviders(block);
            boolean hasNBTEnt = WailaRegistrar.instance().hasNBTProviders(entity);

            if (hasNBTBlock || hasNBTEnt) {
                ServerDataAccessorCommon accessor = ServerDataAccessorCommon.INSTANCE;
                accessor.set(world, player, entity, posX, posY, posZ);

                if (hasNBTBlock) {
                    for (List<IDataProvider> providersList :
                            WailaRegistrar.instance().getNBTProviders(block).values()) {
                        for (IDataProvider provider : providersList) {
                            try {
                                provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                            } catch (Throwable t) {
                                WailaExceptionHandler.handleErr(t, Packet0x01TileRequest.class.toString(), null);
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
                                WailaExceptionHandler.handleErr(t, Packet0x01TileRequest.class.toString(), null);
                            }
                        }
                    }
                }
            }

            tag.setInteger("WailaX", posX);
            tag.setInteger("WailaY", posY);
            tag.setInteger("WailaZ", posZ);

            WailaPacketHandler.sendPacketToPlayer(new Packet0x03NBTData(tag), rawSender);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass().toString(), null);
        }
    }

}

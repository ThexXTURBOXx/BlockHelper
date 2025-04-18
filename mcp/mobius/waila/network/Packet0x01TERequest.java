package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Packet0x01TERequest implements IWailaPacket {

    private static Field classToNameMap;

    static {
        try {
            classToNameMap = TileEntity.class.getDeclaredField("b");
            classToNameMap.setAccessible(true);
        } catch (Throwable t) {
            try {
                classToNameMap = TileEntity.class.getDeclaredField("field_70323_b");
                classToNameMap.setAccessible(true);
            } catch (Throwable t1) {
                try {
                    classToNameMap = TileEntity.class.getDeclaredField("classToNameMap");
                    classToNameMap.setAccessible(true);
                } catch (Throwable t2) {
                    throw new RuntimeException(t2);
                }
            }
        }
    }

    public int dim;
    public int posX;
    public int posY;
    public int posZ;
    public Set<String> keys = new HashSet<String>();

    public Packet0x01TERequest() {
    }

    public Packet0x01TERequest(TileEntity ent, Set<String> keys) {
        this.dim = ent.getWorldObj().provider.dimensionId;
        this.posX = ent.xCoord;
        this.posY = ent.yCoord;
        this.posZ = ent.zCoord;
        this.keys = keys;
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeInt(dim);
        target.writeInt(posX);
        target.writeInt(posY);
        target.writeInt(posZ);
        target.writeInt(this.keys.size());

        for (String key : keys)
            Packet.writeString(key, target);
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            dim = dat.readInt();
            posX = dat.readInt();
            posY = dat.readInt();
            posZ = dat.readInt();

            int nkeys = dat.readInt();

            for (int i = 0; i < nkeys; i++)
                this.keys.add(Packet.readString(dat, 250));

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass().toString(), null);
        }
    }

    @Override
    public void handleClient() {
    }

    @Override
    public void handleServer(Player rawSender) {
        WorldServer world = DimensionManager.getWorld(dim);
        if (world == null) return;
        TileEntity entity = world.getBlockTileEntity(posX, posY, posZ);
        Block block = Block.blocksList[world.getBlockId(posX, posY, posZ)];
        if (entity == null) return;
        if (!(rawSender instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) rawSender;

        try {
            NBTTagCompound tag = new NBTTagCompound();
            boolean hasNBTBlock = WailaRegistrar.instance().hasNBTProviders(block);
            boolean hasNBTEnt = WailaRegistrar.instance().hasNBTProviders(entity);

            if (hasNBTBlock || hasNBTEnt) {
                tag.setInteger("x", posX);
                tag.setInteger("y", posY);
                tag.setInteger("z", posZ);
                tag.setString("id", ((Map<Class<?>, String>) classToNameMap.get(null)).get(entity.getClass()));

                ServerDataAccessorCommon accessor = ServerDataAccessorCommon.INSTANCE;
                accessor.set(world, player, entity, posX, posY, posZ);

                for (List<IDataProvider> providersList :
                        WailaRegistrar.instance().getNBTProviders(block).values()) {
                    for (IDataProvider provider : providersList) {
                        try {
                            provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                        } catch (Throwable t) {
                            WailaExceptionHandler.handleErr(t, Packet0x01TERequest.class.toString(), null);
                        }
                    }
                }

                for (List<IDataProvider> providersList :
                        WailaRegistrar.instance().getNBTProviders(entity).values()) {
                    for (IDataProvider provider : providersList) {
                        try {
                            provider.appendServerData(entity, tag, accessor, PluginConfig.instance());
                        } catch (Throwable t) {
                            WailaExceptionHandler.handleErr(t, Packet0x01TERequest.class.toString(), null);
                        }
                    }
                }

            } else {
                entity.writeToNBT(tag);
                tag = NBTUtil.createTag(tag, keys);
            }

            tag.setInteger("WailaX", posX);
            tag.setInteger("WailaY", posY);
            tag.setInteger("WailaZ", posZ);
            tag.setString("WailaID", ((Map<Class<?>, String>) classToNameMap.get(null)).get(entity.getClass()));

            WailaPacketHandler.sendPacketToPlayer(new Packet0x02TENBTData(tag), rawSender);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, entity.getClass().toString(), null);
        }
    }

}

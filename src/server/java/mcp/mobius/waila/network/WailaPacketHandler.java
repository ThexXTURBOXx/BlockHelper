package mcp.mobius.waila.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.mod_BlockHelper;

public class WailaPacketHandler {

    public static final WailaPacketHandler INSTANCE = new WailaPacketHandler();

    private WailaPacketHandler() {
    }

    public void onPacketData(EntityPlayerMP source, Packet250CustomPayload payload) {
        try {
            if (!payload.channel.equals(mod_BlockHelper.CHANNEL)) return;

            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(payload.data));
            byte header = getHeader(inputStream);

            IWailaPacket castedPacket = getWailaPacket(header);

            if (castedPacket != null) {
                castedPacket.decode(inputStream);
                castedPacket.handleServer(source);
            }

            inputStream.close();
        } catch (Throwable ignored) {
        }
    }

    public static byte getHeader(DataInputStream inputStream) {
        try {
            return inputStream.readByte();
        } catch (IOException e) {
            return -1;
        }
    }

    public static IWailaPacket getWailaPacket(byte header) {
        if (header == 0x00) {
            return new Packet0x00ServerPing();
        } else if (header == 0x01) {
            return new Packet0x01TileRequest();
        } else if (header == 0x02) {
            return new Packet0x02EntRequest();
        } else if (header == 0x03) {
            return new Packet0x03NBTData();
        }
        return null;
    }

    public static byte getPacketId(IWailaPacket packet) {
        if (packet instanceof Packet0x00ServerPing) {
            return 0x00;
        } else if (packet instanceof Packet0x01TileRequest) {
            return 0x01;
        } else if (packet instanceof Packet0x02EntRequest) {
            return 0x02;
        } else if (packet instanceof Packet0x03NBTData) {
            return 0x03;
        }
        return -1;
    }

    public static Packet250CustomPayload wrapMLPacket(IWailaPacket packet) {
        Packet250CustomPayload mlPacket = new Packet250CustomPayload();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeByte(getPacketId(packet));
            packet.encode(outputStream);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, packet.getClass());
        }
        mlPacket.channel = mod_BlockHelper.CHANNEL;
        mlPacket.data = bos.toByteArray();
        mlPacket.length = bos.size();
        return mlPacket;
    }

    public static void sendPacketToPlayer(IWailaPacket packet, EntityPlayerMP player) {
        player.playerNetServerHandler.sendPacket(wrapMLPacket(packet));
    }

}

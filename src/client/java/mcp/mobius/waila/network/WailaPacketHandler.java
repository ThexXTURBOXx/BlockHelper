package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;

public class WailaPacketHandler {

    public static final WailaPacketHandler INSTANCE = new WailaPacketHandler();

    private WailaPacketHandler() {
    }

    /*public void onPacketData(Packet200ModLoader packet) {
        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(toByteArray(packet.dataInt)));

            String channel = inputStream.readUTF();
            if (!channel.equals(mod_BlockHelper.CHANNEL) && !channel.equals(mod_BlockHelper.CHANNEL_SSP)) return;

            byte header = getHeader(inputStream);

            IWailaPacket castedPacket = getWailaPacket(header);

            if (castedPacket != null) {
                castedPacket.decode(inputStream);
                handlePacket(channel, castedPacket);
            }

            inputStream.close();
        } catch (Throwable ignored) {
        }
    }*/

    public void handlePacket(String channel, IWailaPacket packet) {
        if (channel.equals(mod_BlockHelper.CHANNEL))
            packet.handleClient();
        else if (channel.equals(mod_BlockHelper.CHANNEL_SSP))
            packet.handleServer(ModLoader.getMinecraftInstance().thePlayer);
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

    // Format: first int is byte array length.
    // Then, we assume little endian for the bytes.
    public static byte[] toByteArray(int[] arr) {
        byte[] ret = new byte[arr[0]];
        for (int i = 0; i < ret.length; ++i)
            ret[i] = (byte) ((arr[1 + i / 4] >> ((i % 4) * 8)) & 0xFF);
        return ret;
    }

    // Format: first int is byte array length.
    // Then, we use little endian for the bytes.
    public static int[] toIntArray(byte[] arr) {
        int[] ret = new int[1 + (arr.length + 3) / 4]; // ceil div
        ret[0] = arr.length;
        for (int i = 0; i < arr.length; ++i)
            ret[1 + i / 4] |= (arr[i] & 0xFF) << ((i % 4) * 8);
        return ret;
    }

    /*public static Packet200ModLoader wrapMLPacket(IWailaPacket packet) {
        Packet200ModLoader mlPacket = new Packet200ModLoader();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeUTF(mod_BlockHelper.CHANNEL);
            outputStream.writeByte(getPacketId(packet));
            packet.encode(outputStream);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, packet.getClass(), null);
        }
        mlPacket.dataInt = toIntArray(bos.toByteArray());
        return mlPacket;
    }*/

    public static void sendPacketToPlayer(IWailaPacket packet) {
        WailaPacketHandler.INSTANCE.handlePacket(mod_BlockHelper.CHANNEL, packet);
    }

    public static void sendPacketToServer(IWailaPacket packet) {
        /*if (ModLoader.getMinecraftInstance().theWorld.multiplayerWorld)
            ModLoader.SendPacket(mod_BlockHelper.INSTANCE, wrapMLPacket(packet));
        else*/
        WailaPacketHandler.INSTANCE.handlePacket(mod_BlockHelper.CHANNEL_SSP, packet);
    }

    public static void writeString(String str, DataOutputStream dos) throws IOException {
        if (str.length() > 32767) {
            throw new IOException("String too big");
        } else {
            dos.writeShort(str.length());
            dos.writeChars(str);
        }
    }

    public static String readString(DataInputStream dis, int maxSize) throws IOException {
        short size = dis.readShort();
        if (size > maxSize) {
            throw new IOException("Received string length longer than maximum allowed (" +
                                  size + " > " + maxSize + ")");
        } else if (size < 0) {
            throw new IOException("Received string length is less than zero! Weird string!");
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; ++i)
                builder.append(dis.readChar());
            return builder.toString();
        }
    }

}

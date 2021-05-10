package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.FMLCommonHandler;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import forge.DimensionManager;
import forge.IConnectionHandler;
import forge.IPacketHandler;
import forge.MessageManager;
import forge.NetworkMod;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.Packet250CustomPayload;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    private static final String MOD_ID = "mod_BlockHelper";
    static final String NAME = "Block Helper";
    static final String VERSION = "1.0.0";
    static final String MC_VERSION = "1.2.5";
    static final String CHANNEL = "BlockHelperInfo";

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(FMLCommonHandler.instance().getFMLLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static BlockHelperCommonProxy proxy;

    public static String getModId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void load() {
        proxy = new BlockHelperCommonProxy();
        proxy.load(this);
    }

    @Override
    public boolean clientSideRequired() {
        return true;
    }

    @Override
    public boolean serverSideRequired() {
        return false;
    }

    @Override
    public void onPacketData(NetworkManager manager, String channel, byte[] data) {
        try {
            if (channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(data);
                DataInputStream is = new DataInputStream(isRaw);
                PacketInfo pi = null;
                try {
                    pi = (PacketInfo) PacketCoder.decode(is);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (pi == null || pi.mop == null)
                    return;
                World w = DimensionManager.getWorld(pi.dimId);
                if (pi.mt == MopType.ENTITY) {
                    Entity en = pi.mop.entity;
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    DataOutputStream os = new DataOutputStream(buffer);
                    PacketClient pc = new PacketClient();
                    if (en != null) {
                        try {
                            pc.add(((EntityLiving) en).getHealth() + " \u2764 / "
                                    + ((EntityLiving) en).getMaxHealth() + " \u2764");
                            PacketCoder.encode(os, pc);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Throwable e) {
                            try {
                                PacketCoder.encode(os, pc);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            PacketCoder.encode(os, pc);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    byte[] fieldData = buffer.toByteArray();
                    Packet250CustomPayload packet = new Packet250CustomPayload();
                    packet.tag = CHANNEL;
                    packet.data = fieldData;
                    packet.length = fieldData.length;
                    manager.queue(packet);
                } else if (pi.mt == MopType.BLOCK) {
                    TileEntity te = w.getTileEntity(pi.mop.b, pi.mop.c, pi.mop.d);
                    int id = w.getTypeId(pi.mop.b, pi.mop.c, pi.mop.d);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    DataOutputStream os = new DataOutputStream(buffer);
                    PacketClient info = new PacketClient();
                    if (id > 0) {
                        int meta = w.getData(pi.mop.b, pi.mop.c, pi.mop.d);
                        Block b = Block.byId[id];
                        BlockHelperModSupport.addInfo(info, b, id, meta, te);
                    }
                    try {
                        PacketCoder.encode(os, info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] fieldData = buffer.toByteArray();
                    Packet250CustomPayload packet = new Packet250CustomPayload();
                    packet.tag = CHANNEL;
                    packet.data = fieldData;
                    packet.length = fieldData.length;
                    manager.queue(packet);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

    @SuppressWarnings("unchecked")
    static Entity getEntityByID(World w, int entityId) {
        for (Entity e : (List<Entity>) w.entityList) {
            if (e.id == entityId) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void onConnect(NetworkManager network) {
    }

    @Override
    public void onLogin(NetworkManager network, Packet1Login login) {
        MessageManager.getInstance().registerChannel(network, this, CHANNEL);
    }

    @Override
    public void onDisconnect(NetworkManager network, String message, Object[] args) {
    }

}


package net.minecraft.src;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.client.BlockHelperGui;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.IConnectionHandler;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;
import net.minecraft.src.forge.NetworkMod;

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.2.0-pre1";
    public static final String MC_VERSION = "1.2.5";
    public static final String CHANNEL = "BlockHelperInfo";
    public static final String CHANNEL_SSP = "BlockHelperInfoSSP";
    public static mod_BlockHelper INSTANCE;

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(ModLoader.getLogger());
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
        INSTANCE = this;
        proxy = new BlockHelperClientProxy();
        proxy.load(this);
    }

    @Override
    public boolean clientSideRequired() {
        return false;
    }

    @Override
    public boolean serverSideRequired() {
        return false;
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return true;
        return BlockHelperGui.getInstance().onTickInGame(mc);
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

    @Override
    public void onPacketData(NetworkManager network, String channel, byte[] data) {
        try {
            if (channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(data);
                DataInputStream is = new DataInputStream(isRaw);
                try {
                    BlockHelperGui.getInstance().setData(((PacketClient) PacketCoder.decode(is)).data);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    is.close();
                    isRaw.close();
                }
            } else if (channel.equals(CHANNEL_SSP)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(data);
                DataInputStream is = new DataInputStream(isRaw);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream os = new DataOutputStream(buffer);
                try {
                    PacketInfo pi = null;
                    try {
                        pi = (PacketInfo) PacketCoder.decode(is);
                    } catch (IOException ignored) {
                    }

                    World w = ModLoader.getMinecraftInstance().theWorld;
                    PacketClient info = new PacketClient();

                    StringTranslate translator = StringTranslate.getInstance();

                    if (pi != null && pi.mop != null) {
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = pi.mop.entityHit;
                            if (en != null) {
                                if (BlockHelperCommonProxy.showHealth) {
                                    try {
                                        info.add(((EntityLiving) en).getHealth() + " \u2764 / "
                                                 + ((EntityLiving) en).getMaxHealth() + " \u2764");
                                    } catch (Throwable ignored) {
                                    }
                                }

                                BlockHelperModSupport.addInfo(new BlockHelperEntityState(translator, w, en), info);
                            }
                        } else if (pi.mt == MopType.BLOCK) {
                            int x = pi.mop.blockX;
                            int y = pi.mop.blockY;
                            int z = pi.mop.blockZ;
                            TileEntity te = w.getBlockTileEntity(x, y, z);
                            int id = w.getBlockId(x, y, z);
                            if (id > 0) {
                                int meta = w.getBlockMetadata(x, y, z);
                                Block b = Block.blocksList[id];
                                BlockHelperModSupport.addInfo(
                                        new BlockHelperBlockState(translator, w, pi.mop, b, te, id, meta), info);
                            }
                        } else {
                            return;
                        }
                    } else {
                        info.add(I18n.format(translator, "server_side_error"));
                        info.add(I18n.format(translator, "version_mismatch"));
                    }

                    try {
                        PacketCoder.encode(os, info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    byte[] fieldData = buffer.toByteArray();
                    if (w.isRemote) {
                        Packet250CustomPayload packet = new Packet250CustomPayload();
                        packet.channel = CHANNEL;
                        packet.data = fieldData;
                        packet.length = fieldData.length;
                        ModLoader.getMinecraftInstance().getSendQueue().addToSendQueue(packet);
                    } else {
                        onPacketData(null, CHANNEL, fieldData);
                    }
                } finally {
                    os.close();
                    buffer.close();
                    is.close();
                    isRaw.close();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static int damageDropped(Block b, int meta) {
        return b.damageDropped(meta);
    }

    @SuppressWarnings("unchecked")
    public static Entity getEntityByID(World w, int entityId) {
        if (w == null) {
            return null;
        }
        try {
            if (w instanceof WorldClient) {
                Entity e = ((WorldClient) w).getEntityByID(entityId);
                if (e != null) {
                    return e;
                }
            }
        } catch (Throwable ignored) {
        }
        List<Entity> list = (List<Entity>) w.getLoadedEntityList();
        if (list != null) {
            for (Entity e : list) {
                if (e.entityId == entityId) {
                    return e;
                }
            }
        }
        return null;
    }

}

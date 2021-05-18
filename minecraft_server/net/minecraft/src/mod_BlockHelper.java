package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.src.forge.DimensionManager;
import net.minecraft.src.forge.IConnectionHandler;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;
import net.minecraft.src.forge.NetworkMod;

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "1.2.5";
    public static final String CHANNEL = "BlockHelperInfo";
    public static mod_BlockHelper INSTANCE;

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
        INSTANCE = this;
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
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream os = new DataOutputStream(buffer);
                try {
                    PacketInfo pi = null;
                    try {
                        pi = (PacketInfo) PacketCoder.decode(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pi == null || pi.mop == null)
                        return;
                    World w = DimensionManager.getWorld(pi.dimId);
                    PacketClient info = new PacketClient();
                    if (pi.mt == MopType.ENTITY) {
                        Entity en = pi.mop.entityHit;
                        if (en != null) {
                            try {
                                info.add(((EntityLiving) en).getHealth() + " \u2764 / "
                                        + ((EntityLiving) en).getMaxHealth() + " \u2764");
                            } catch (Throwable ignored) {
                            }

                            BlockHelperModSupport.addInfo(new BlockHelperEntityState(w, en), info);
                        }
                    } else if (pi.mt == MopType.BLOCK) {
                        TileEntity te = w.getBlockTileEntity(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        int id = w.getBlockId(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        if (id > 0) {
                            int meta = w.getBlockMetadata(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                            Block b = Block.blocksList[id];
                            BlockHelperModSupport.addInfo(new BlockHelperBlockState(w, b, te, id, meta), info);
                        }
                    } else {
                        return;
                    }

                    try {
                        PacketCoder.encode(os, info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] fieldData = buffer.toByteArray();
                    Packet250CustomPayload packet = new Packet250CustomPayload();
                    packet.channel = CHANNEL;
                    packet.data = fieldData;
                    packet.length = fieldData.length;
                    manager.addToSendQueue(packet);
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

    public static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

    @SuppressWarnings("unchecked")
    public static Entity getEntityByID(World w, int entityId) {
        for (Entity e : (List<Entity>) w.loadedEntityList) {
            if (e.entityId == entityId) {
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


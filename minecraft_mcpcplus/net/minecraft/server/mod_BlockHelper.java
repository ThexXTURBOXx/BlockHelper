package net.minecraft.server;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
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

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "1.2.5";
    public static final String CHANNEL = "BlockHelperInfo";
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
                        Entity en = pi.mop.entity;
                        if (en != null) {
                            try {
                                info.add(((EntityLiving) en).getHealth() + " \u2764 / "
                                        + ((EntityLiving) en).getMaxHealth() + " \u2764");
                            } catch (Throwable ignored) {
                            }

                            BlockHelperModSupport.addInfo(new BlockHelperEntityState(w, en), info);
                        }
                    } else if (pi.mt == MopType.BLOCK) {
                        int x = pi.mop.b;
                        int y = pi.mop.c;
                        int z = pi.mop.d;
                        TileEntity te = w.getTileEntity(x, y, z);
                        int id = w.getTypeId(x, y, z);
                        if (id > 0) {
                            int meta = w.getData(x, y, z);
                            Block b = Block.byId[id];
                            BlockHelperModSupport.addInfo(new BlockHelperBlockState(w, pi.mop, b, te, id, meta), info);
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
                    packet.tag = CHANNEL;
                    packet.data = fieldData;
                    packet.length = fieldData.length;
                    manager.queue(packet);
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
        for (Entity e : (List<Entity>) w.entityList) {
            if (e.id == entityId) {
                return e;
            }
        }
        return null;
    }

    public static String getItemDisplayName(ItemStack stack) {
        String var2 = stack.getItem().a(stack);
        return LocaleLanguage.a().b(var2 == null ? "" : (LocaleI18n.get(var2) + ".name")).trim();
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

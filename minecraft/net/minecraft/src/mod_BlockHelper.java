package net.minecraft.src;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.client.BlockHelperGui;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

public class mod_BlockHelper extends BaseModMp {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "b1.8.1";
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
    public String Version() {
        return VERSION;
    }

    @Override
    public void ModsLoaded() {
        INSTANCE = this;
        proxy = new BlockHelperClientProxy();
        proxy.load(this);
    }

    @Override
    public boolean OnTickInGame(float time, Minecraft mc) {
        return BlockHelperGui.getInstance().onTickInGame(mc);
    }

    @Override
    public void HandlePacket(Packet230ModLoader packetML) {
        try {
            String channel = packetML.dataString[0];
            byte[] data = packetML.dataString[1].getBytes("ISO-8859-1");
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pi == null || pi.mop == null)
                        return;

                    World w = ModLoader.getMinecraftInstance().theWorld;
                    PacketClient info = new PacketClient();
                    if (pi.mt == MopType.ENTITY) {
                        Entity en = pi.mop.entityHit;
                        if (en != null) {
                            try {
                                info.add(((EntityLiving) en).health + " \u2764");
                            } catch (Throwable ignored) {
                            }

                            BlockHelperModSupport.addInfo(new BlockHelperEntityState(w, en), info);
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
                    Packet230ModLoader packet = new Packet230ModLoader();
                    packet.modId = getId();
                    packet.dataString = new String[]{CHANNEL, buffer.toString("ISO-8859-1")};
                    if (w.multiplayerWorld) {
                        ModLoaderMp.SendPacket(this, packet);
                    } else {
                        HandlePacket(packet);
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

    public static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

    public static int damageDropped(Block b, World w, int x, int y,
                                    int z, int meta) {
        List<ItemStack> list = b.getBlockDropped(w, x, y, z, meta);
        if (!list.isEmpty()) {
            return list.get(0).getItemDamage();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static Entity getEntityByID(World w, int entityId) {
        if (w == null) {
            return null;
        }
        List<Entity> list = (List<Entity>) w.getLoadedEntityList();
        if (list != null) {
            for (Entity e : list) {
                if (e.entityId == entityId) {
                    return e;
                }
            }
        }
        if (w instanceof WorldClient) {
            try {
                Field f = ((WorldClient) w).getClass().getDeclaredField("entityList");
                f.setAccessible(true);
                list = (List<Entity>) f.get(w);
                for (Entity e : list) {
                    if (e.entityId == entityId) {
                        return e;
                    }
                }
            } catch (IllegalAccessException ignored) {
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    public static String getItemDisplayName(ItemStack stack) {
        String var2 = stack.getItem().getItemNameIS(stack);
        return StringTranslate.getInstance().translateKey(
                var2 == null ? "" : (StatCollector.translateToLocal(var2) + ".name")).trim();
    }

}
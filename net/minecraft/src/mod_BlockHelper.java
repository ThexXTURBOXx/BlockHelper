package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
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
import net.minecraftforge.common.DimensionManager;

public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    public static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "1.3.2";
    public static final String CHANNEL = "BlockHelperInfo";
    public static mod_BlockHelper INSTANCE;

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(FMLLog.getLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static boolean isClient;

    @SidedProxy(clientSide = PACKAGE + "BlockHelperClientProxy", serverSide = PACKAGE + "BlockHelperCommonProxy")
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
        proxy.load(this);
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
        return BlockHelperGui.getInstance().onTickInGame(mc);
    }

    @Override
    public void onPacketData(NetworkManager manager, Packet250CustomPayload packetGot, Player player) {
        try {
            if (packetGot.channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(packetGot.data);
                DataInputStream is = new DataInputStream(isRaw);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream os = new DataOutputStream(buffer);
                try {
                    if (isClient && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                        try {
                            BlockHelperGui.getInstance().setData(((PacketClient) PacketCoder.decode(is)).data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                        PacketInfo pi = null;
                        try {
                            pi = (PacketInfo) PacketCoder.decode(is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (pi == null || pi.mop == null)
                            return;

                        World w = DimensionManager.getProvider(pi.dimId).worldObj;
                        PacketClient info = new PacketClient();
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = getEntityByID(w, pi.entityId);
                            if (en != null) {
                                try {
                                    info.add(((EntityLiving) en).getHealth() + " ❤ / "
                                            + ((EntityLiving) en).getMaxHealth() + " ❤");
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
                                BlockHelperModSupport.addInfo(
                                        new BlockHelperBlockState(w, pi.mop, b, te, id, meta), info);
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
                        PacketDispatcher.sendPacketToPlayer(packet, player);
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
        List<ItemStack> list = b.getBlockDropped(w, x, y, z, meta, 0);
        if (!list.isEmpty()) {
            return list.get(0).getItemDamage();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static Entity getEntityByID(World w, int entityId) {
        List<Entity> list = (List<Entity>) w.loadedEntityList;
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

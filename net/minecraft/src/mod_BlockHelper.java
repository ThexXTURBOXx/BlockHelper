package net.minecraft.src;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
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
import net.minecraftforge.common.DimensionManager;

import static net.minecraft.src.mod_BlockHelper.CHANNEL;

@NetworkMod(channels = {CHANNEL}, packetHandler = mod_BlockHelper.class)
public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    public static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.2.0-pre1";
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
        if (mc.theWorld == null || mc.thePlayer == null) return true;
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
                    if (isClient && getEffectiveSide().isClient()) {
                        try {
                            BlockHelperGui.getInstance().setData(((PacketClient) PacketCoder.decode(is)).data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (getEffectiveSide().isServer()) {
                        PacketInfo pi = null;
                        try {
                            pi = (PacketInfo) PacketCoder.decode(is);
                        } catch (IOException ignored) {
                        }

                        PacketClient info = new PacketClient();

                        StringTranslate translator = player instanceof EntityPlayer
                                ? ((EntityPlayer) player).getTranslator()
                                : StringTranslate.getInstance();

                        if (pi != null && pi.mop != null) {
                            World w = DimensionManager.getProvider(pi.dimId).worldObj;
                            if (pi.mt == MopType.ENTITY) {
                                Entity en = getEntityByID(w, pi.entityId);
                                if (en != null) {
                                    if (BlockHelperCommonProxy.showHealth) {
                                        try {
                                            info.add(((EntityLiving) en).getHealth() + " ❤ / "
                                                     + ((EntityLiving) en).getMaxHealth() + " ❤");
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
        try {
            if (w instanceof WorldServer) {
                Entity e = ((WorldServer) w).getEntityByID(entityId);
                if (e != null) {
                    return e;
                }
            }
        } catch (Throwable ignored) {
        }
        List<Entity> list = (List<Entity>) w.loadedEntityList;
        if (list != null) {
            for (Entity e : list) {
                if (e.entityId == entityId) {
                    return e;
                }
            }
        }
        return null;
    }

    public static String getItemDisplayName(ItemStack stack) {
        String var2 = stack.getItem().getItemNameIS(stack);
        return StringTranslate.getInstance().translateKey(
                var2 == null ? "" : (StatCollector.translateToLocal(var2) + ".name")).trim();
    }

    // Replacement for FMLCommonHandler.instance().getEffectiveSide(), workaround for older Forge versions
    public Side getEffectiveSide() {
        Thread thr = Thread.currentThread();
        if ((thr instanceof ThreadServerApplication) || (thr instanceof ServerListenThread)) {
            return Side.SERVER;
        }
        return Side.CLIENT;
    }

}

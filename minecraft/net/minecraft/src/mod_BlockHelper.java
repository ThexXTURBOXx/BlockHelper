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

public class mod_BlockHelper extends BaseModMp {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.1.0";
    public static final String MC_VERSION = "1.0";
    public static final String CHANNEL = "BlockHelperInfo";
    public static final String CHANNEL_SSP = "BlockHelperInfoSSP";
    public static mod_BlockHelper INSTANCE;

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(ModLoader.getLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static BlockHelperCommonProxy proxy;

    // Configuration entries start
    @MLProp(name = "Size")
    public static String sizeStr = "1.0";
    @MLProp(name = "BackgroundColor")
    public static String backgroundStr = "cc100010";
    @MLProp(name = "BorderColor1")
    public static String gradient1Str = "cc5000ff";
    @MLProp(name = "BorderColor2")
    public static String gradient2Str = "cc28007f";
    @MLProp(name = "NotifyAboutFixers")
    public static String fixerNotifyStr = "true";
    @MLProp(name = "ShowItemID")
    public static String showItemIdStr = "true";
    @MLProp(name = "ShowHarvestability")
    public static String showHarvestStr = "true";
    @MLProp(name = "ShowBreakProgression")
    public static String showBreakProgStr = "true";
    @MLProp(name = "ShowMod")
    public static String showModStr = "true";
    @MLProp(name = "ShowHealth")
    public static String showHealthStr = "true";
    @MLProp(name = "ShowBlockInHud")
    public static String renderBlockStr = "true";
    @MLProp(name = "BuildCraftIntegration")
    public static String bcIntegrationStr = "true";
    @MLProp(name = "ForestryIntegration")
    public static String forestryIntegrationStr = "true";
    @MLProp(name = "Ic2Integration")
    public static String ic2IntegrationStr = "true";
    @MLProp(name = "RedPower2Integration")
    public static String redPower2IntegrationStr = "true";
    @MLProp(name = "VanillaIntegration")
    public static String vanillaIntegrationStr = "true";
    @MLProp(name = "ShouldHideFromDebug")
    public static String shouldHideFromDebugStr = "true";
    // Configuration entries end

    public static String getModId() {
        return MOD_ID;
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
                    } catch (IOException ignored) {
                    }

                    World w = ModLoader.getMinecraftInstance().theWorld;
                    PacketClient info = new PacketClient();

                    if (pi != null && pi.mop != null) {
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = pi.mop.entityHit;
                            if (en != null) {
                                if (BlockHelperCommonProxy.showHealth) {
                                    try {
                                        info.add(((EntityLiving) en).getEntityHealth() + " \u2764 / "
                                                + ((EntityLiving) en).getMaxHealth() + " \u2764");
                                    } catch (Throwable ignored) {
                                    }
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
                    } else {
                        info.add(I18n.format("server_side_error"));
                        info.add(I18n.format("version_mismatch"));
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

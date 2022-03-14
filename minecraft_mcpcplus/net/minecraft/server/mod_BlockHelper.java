package net.minecraft.server;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.i18n.I18n;
import forge.DimensionManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class mod_BlockHelper extends BaseModMp {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.1.0";
    public static final String MC_VERSION = "1.2.3";
    public static final String CHANNEL = "BlockHelperInfo";
    public static mod_BlockHelper INSTANCE;

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(ModLoader.getLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static BlockHelperCommonProxy proxy;

    // Configuration entries start
    @MLProp(name = "ShowHealth")
    public static String showHealthStr = "true";
    @MLProp(name = "AdvMachinesIntegration")
    public static String advMachinesIntegrationStr = "true";
    @MLProp(name = "BuildCraftIntegration")
    public static String bcIntegrationStr = "true";
    @MLProp(name = "FloraSomaIntegration")
    public static String floraSomaIntegrationStr = "true";
    @MLProp(name = "ForestryIntegration")
    public static String forestryIntegrationStr = "true";
    @MLProp(name = "Ic2Integration")
    public static String ic2IntegrationStr = "true";
    @MLProp(name = "RedPower2Integration")
    public static String redPower2IntegrationStr = "true";
    @MLProp(name = "VanillaIntegration")
    public static String vanillaIntegrationStr = "true";
    // Configuration entries end

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
    public void handlePacket(Packet230ModLoader packetML, EntityPlayer player) {
        try {
            String channel = packetML.dataString[0];
            byte[] data = packetML.dataString[1].getBytes("ISO-8859-1");
            if (channel.equals(CHANNEL)) {
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

                    PacketClient info = new PacketClient();

                    if (pi != null && pi.mop != null) {
                        World w = DimensionManager.getWorld(pi.dimId);
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = pi.mop.entity;
                            if (en != null) {
                                if (BlockHelperCommonProxy.showHealth) {
                                    try {
                                        info.add(((EntityLiving) en).getHealth() + " \u2764 / "
                                                + ((EntityLiving) en).getMaxHealth() + " \u2764");
                                    } catch (Throwable ignored) {
                                    }
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
                    ModLoaderMp.sendPacketTo(this, player, packet);
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

    @SuppressWarnings("unchecked")
    public static Entity getEntityByID(World w, int entityId) {
        if (w == null) {
            return null;
        }
        try {
            if (w instanceof WorldServer) {
                Entity e = ((WorldServer) w).getEntity(entityId);
                if (e != null) {
                    return e;
                }
            }
        } catch (Throwable ignored) {
        }
        List<Entity> list = (List<Entity>) w.entityList;
        if (list != null) {
            for (Entity e : list) {
                if (e.id == entityId) {
                    return e;
                }
            }
        }
        return null;
    }

    public static String getItemDisplayName(ItemStack stack) {
        String var2 = stack.getItem().a(stack);
        return LocaleLanguage.a().b(var2 == null ? "" : (LocaleI18n.get(var2) + ".name")).trim();
    }

}

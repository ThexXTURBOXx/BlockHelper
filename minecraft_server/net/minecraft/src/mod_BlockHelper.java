package net.minecraft.src;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class mod_BlockHelper extends BaseModMp {

    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.2.0-pre3";
    public static final String MC_VERSION = "b1.4_01";
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
    @MLProp(name = "IcIntegration")
    public static String icIntegrationStr = "true";
    @MLProp(name = "VanillaIntegration")
    public static String vanillaIntegrationStr = "true";
    // Configuration entries end

    static {
        try {
            setupProperties(mod_BlockHelper.class);
        } catch (IllegalAccessException e) {
            LOGGER.throwing(NAME, "setupProperties", e);
            ModLoader.ThrowException(NAME, e);
        } catch (IOException e) {
            LOGGER.throwing(NAME, "setupProperties", e);
            ModLoader.ThrowException(NAME, e);
        } catch (URISyntaxException e) {
            LOGGER.throwing(NAME, "setupProperties", e);
            ModLoader.ThrowException(NAME, e);
        }
    }

    public static String getModId() {
        return MOD_ID;
    }

    @Override
    public String Version() {
        return VERSION;
    }

    @Override
    public void ModsLoaded() {
        super.ModsLoaded();
        INSTANCE = this;
        proxy = new BlockHelperCommonProxy();
        proxy.load(this);
    }

    @Override
    public void HandlePacket(Packet200ModLoader packetML, EntityPlayerMP player) {
        try {
            String[] dataString = PacketCoder.toStrings(packetML.dataInt);
            String channel = dataString[0];
            byte[] data = dataString[1].getBytes("ISO-8859-1");
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
                        World w = ModLoader.getMinecraftServerInstance().worldMngr;
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = pi.mop.entityHit;
                            if (en != null) {
                                if (BlockHelperCommonProxy.showHealth) {
                                    try {
                                        info.add(((EntityLiving) en).health + " \u2764");
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
                    Packet200ModLoader packet = new Packet200ModLoader();
                    packet.modId = getId();
                    packet.dataInt = PacketCoder.toIntArray(CHANNEL, buffer.toString("ISO-8859-1"));
                    ModLoaderMp.SendPacketTo(this, player, packet);
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
                Entity e = ((WorldServer) w).func_6158_a(entityId);
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
        return stack.getItem().getItemName();
    }

    private static void setupProperties(Class<? extends BaseMod> class1)
            throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException,
            URISyntaxException {
        String s0 = ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        s0 = s0.substring(0, s0.lastIndexOf('/'));
        File cfgdir = new File(s0, "/config/");
        cfgdir.mkdir();
        Properties properties = new Properties();
        File file = new File(cfgdir, class1.getName() + ".cfg");
        if (file.exists() && file.canRead()) {
            properties.load(new FileInputStream(file));
        }
        StringBuilder stringbuilder = new StringBuilder();
        Field[] afield;
        int j = (afield = class1.getFields()).length;
        for (int i = 0; i < j; i++) {
            Field field = afield[i];
            if ((field.getModifiers() & 8) == 0 || !field.isAnnotationPresent(MLProp.class)) {
                continue;
            }
            Class<?> class2 = field.getType();
            MLProp mlprop = (MLProp) field.getAnnotation(MLProp.class);
            String s = !mlprop.name().isEmpty() ? mlprop.name() : field.getName();
            Object obj = field.get(null);
            StringBuilder stringbuilder1 = new StringBuilder();
            if (mlprop.min() != Double.NEGATIVE_INFINITY) {
                stringbuilder1.append(String.format(",>=%.1f", mlprop.min()));
            }
            if (mlprop.max() != Double.POSITIVE_INFINITY) {
                stringbuilder1.append(String.format(",<=%.1f", mlprop.max()));
            }
            StringBuilder stringbuilder2 = new StringBuilder();
            if (!mlprop.info().isEmpty()) {
                stringbuilder2.append(" -- ");
                stringbuilder2.append(mlprop.info());
            }
            stringbuilder.append(String.format("%s (%s:%s%s)%s\n", s, class2.getName(), obj, stringbuilder1,
                    stringbuilder2));
            if (properties.containsKey(s)) {
                String s1 = properties.getProperty(s);
                Object obj1 = null;
                if (class2.isAssignableFrom(String.class)) {
                    obj1 = s1;
                } else if (class2.isAssignableFrom(Integer.TYPE)) {
                    obj1 = Integer.parseInt(s1);
                } else if (class2.isAssignableFrom(Short.TYPE)) {
                    obj1 = Short.parseShort(s1);
                } else if (class2.isAssignableFrom(Byte.TYPE)) {
                    obj1 = Byte.parseByte(s1);
                } else if (class2.isAssignableFrom(Boolean.TYPE)) {
                    obj1 = Boolean.parseBoolean(s1);
                } else if (class2.isAssignableFrom(Float.TYPE)) {
                    obj1 = Float.parseFloat(s1);
                } else if (class2.isAssignableFrom(Double.TYPE)) {
                    obj1 = Double.parseDouble(s1);
                }
                if (obj1 == null) {
                    continue;
                }
                if (obj1 instanceof Number) {
                    double d = ((Number) obj1).doubleValue();
                    if (mlprop.min() != Double.NEGATIVE_INFINITY && d < mlprop.min() || mlprop.max() != Double.POSITIVE_INFINITY && d > mlprop.max()) {
                        continue;
                    }
                }
                LOGGER.finer(s + " set to " + obj1);
                if (!obj1.equals(obj)) {
                    field.set(null, obj1);
                }
            } else {
                LOGGER.finer(s + " not in config, using default: " + obj);
                properties.setProperty(s, obj.toString());
            }
        }

        if (!properties.isEmpty() && (file.exists() || file.createNewFile()) && file.canWrite()) {
            properties.store(new FileOutputStream(file), stringbuilder.toString());
        }
    }

}

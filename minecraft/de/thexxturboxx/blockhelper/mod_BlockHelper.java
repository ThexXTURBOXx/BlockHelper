package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.FMLCommonHandler;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Gui;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraft.src.forge.IConnectionHandler;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.forge.MessageManager;
import net.minecraft.src.forge.NetworkMod;
import org.lwjgl.opengl.GL11;

import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.size;
import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.sizeInv;

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    private static final String MOD_ID = "mod_BlockHelper";
    static final String NAME = "Block Helper";
    static final String VERSION = "0.9";
    static final String CHANNEL = "BlockHelperInfo";
    static final String CHANNEL_SSP = "BlockHelperInfoSSP";

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(FMLCommonHandler.instance().getFMLLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    private static final Random rnd = new Random();

    public static boolean isClient;

    private static boolean firstTick = true;

    private boolean isHidden = false;

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
        proxy = new BlockHelperClientProxy();
        proxy.load(this);
        ModIdentifier.load();
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
    public boolean onTickInGame(float time, Minecraft mc) {
        try {
            GL11.glScaled(size, size, size);

            if (firstTick) {
                ModIdentifier.firstTick();
                BlockHelperUpdater.notifyUpdater(mc);
                firstTick = false;
            }

            updateKeyState();
            if (mc.currentScreen != null || isHidden)
                return true;
            MopType result = getRayTraceResult(mc);
            if (result == MopType.AIR)
                return true;
            MovingObjectPosition mop = mc.objectMouseOver;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(buffer);
            try {
                if (result == MopType.ENTITY) {
                    PacketCoder.encode(os, new PacketInfo(mc.theWorld.worldProvider.worldType, mop, MopType.ENTITY,
                            mop.entityHit.entityId));
                } else {
                    PacketCoder.encode(os,
                            new PacketInfo(mc.theWorld.worldProvider.worldType, mop, result));
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            byte[] fieldData = buffer.toByteArray();
            if (proxy.getWorld().isRemote) {
                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = CHANNEL;
                packet.data = fieldData;
                packet.length = fieldData.length;
                ModLoader.sendPacket(packet);
            } else {
                onPacketData(null, CHANNEL_SSP, fieldData);
            }
            switch (result) {
            case BLOCK:
                int meta = mc.theWorld.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                int id = mc.theWorld.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                Block b = Block.blocksList[id];
                TileEntity te = mc.theWorld.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                ItemStack is = BlockHelperModSupport.getItemStack(b, te, id, meta);
                if (is == null) {
                    is = new ItemStack(b, 1, meta);
                }
                String itemId = is.itemID + ":" + is.getItemDamage();
                if (is.getItem() == null)
                    return true;

                String mod = BlockHelperModSupport.getMod(b, te, id, meta);
                mod = mod == null ? ModIdentifier.identifyMod(b) : mod;
                mod = mod == null ? ModIdentifier.MINECRAFT : mod;

                String name = BlockHelperModSupport.getName(b, te, id, meta);
                name = name == null ? "" : name;
                if (name.isEmpty()) {
                    try {
                        name = is.getItem().getItemDisplayName(is);
                        if (name.isEmpty())
                            throw new IllegalArgumentException();
                    } catch (Throwable e) {
                        try {
                            ItemStack isNew = new ItemStack(b);
                            name = isNew.getItem().getItemDisplayName(isNew);
                            if (name.isEmpty())
                                throw new IllegalArgumentException();
                        } catch (Throwable e1) {
                            try {
                                if (b != null) {
                                    Item it = Item.itemsList[b.idDropped(meta, rnd, 0)];
                                    ItemStack stack = new ItemStack(it, 1,
                                            damageDropped(b, mc.theWorld, mop.blockX, mop.blockY, mop.blockZ, meta));
                                    name = it.getItemDisplayName(stack);
                                }
                                if (name.isEmpty())
                                    throw new IllegalArgumentException();
                            } catch (Throwable e2) {
                                if (b != null) {
                                    name = b.translateBlockName();
                                } else {
                                    name = "Please report this!";
                                }
                            }
                        }
                    }
                }

                String harvest = "Please report this!";
                boolean harvestable = false;
                if (b != null) {
                    float hardness = b.getHardness(meta);
                    if (hardness == -1.0F || hardness == -1.0D || hardness == -1) {
                        harvest = "Unbreakable";
                    } else if (b.canHarvestBlock(proxy.getPlayer(), meta)) {
                        harvestable = true;
                        harvest = "Currently harvestable";
                    } else {
                        harvest = "Currently not harvestable";
                    }
                }

                infos.clear();
                addInfo(name);
                addInfo(itemId);
                addInfo((harvestable ? "\u00a7a\u2714" : "\u00a74\u2718") + " \u00a7r\u00a77" + harvest);
                addAdditionalInfo(packetInfos);
                addInfo("\u00a79\u00a7o" + mod);
                int x = drawBox(mc);
                drawInfo(x, mc);
                break;
            case ENTITY:
                Entity e = mop.entityHit;
                String nameEntity = EntityList.getEntityString(e);
                if (e instanceof IMob) {
                    nameEntity = "\u00a74" + nameEntity;
                }
                mod = ModIdentifier.identifyMod(e);
                mod = mod == null ? ModIdentifier.MINECRAFT : mod;
                infos.clear();
                addInfo(nameEntity);
                addAdditionalInfo(packetInfos);
                addInfo("\u00a79\u00a7o" + mod);
                x = drawBox(mc);
                drawInfo(x, mc);
                break;
            default:
                break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            GL11.glScaled(sizeInv, sizeInv, sizeInv);
        }
        return true;
    }

    private void updateKeyState() {
        if (BlockHelperClientProxy.showHide.isPressed()) {
            isHidden = !isHidden;
        }
    }

    private int getStringMid(int x, String s, Minecraft mc) {
        return x - mc.fontRenderer.getStringWidth(s) / 2;
    }

    private MopType getRayTraceResult(Minecraft mc) {
        MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null)
            return MopType.AIR;
        switch (mop.typeOfHit) {
        case ENTITY:
            return MopType.ENTITY;
        case TILE:
            Material b = mc.theWorld.getBlockMaterial(mop.blockX, mop.blockY, mop.blockZ);
            if (b != null)
                return MopType.BLOCK;
            else
                return MopType.AIR;
        default:
            return MopType.AIR;
        }
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

    private static List<String> packetInfos = new ArrayList<String>();
    private static final List<String> infos = new ArrayList<String>();

    private void addAdditionalInfo(List<String> info) {
        for (String s : info) {
            addInfo("\u00a77" + s);
        }
    }

    private void addInfo(String info) {
        if (info != null && !info.isEmpty()) {
            infos.add(info);
        }
    }

    private static final int PADDING = 12;
    private static final int dark = new Color(17, 2, 16).getRGB();
    private static final int light = new Color(52, 18, 102).getRGB();

    private void drawInfo(int x, Minecraft mc) {
        int currLine = PADDING;
        for (String s : infos) {
            mc.fontRenderer.drawString(s, getStringMid(x, s, mc), currLine, 0xffffffff);
            currLine += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    private int drawBox(Minecraft mc) {
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = (int) (res.getScaledWidth() * sizeInv);
        if (BlockHelperClientProxy.mode != 1) {
            int infoWidth = 0;
            int currLine = PADDING;
            for (String s : infos) {
                infoWidth = Math.max(mc.fontRenderer.getStringWidth(s) + PADDING, infoWidth);
                currLine += mc.fontRenderer.FONT_HEIGHT;
            }
            int minusHalf = (width - infoWidth) / 2;
            int plusHalf = (width + infoWidth) / 2;
            Gui.drawRect(minusHalf + 2, 7, plusHalf - 2, currLine + 5, dark);
            Gui.drawRect(minusHalf + 1, 8, plusHalf - 1, currLine + 4, dark);
            Gui.drawRect(minusHalf + 2, 8, plusHalf - 2, currLine + 4, light);
            Gui.drawRect(minusHalf + 3, 9, plusHalf - 3, currLine + 3, dark);
        }
        return width / 2;
    }

    @Override
    public void onPacketData(NetworkManager network, String channel, byte[] data) {
        try {
            if (channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(data);
                DataInputStream is = new DataInputStream(isRaw);
                try {
                    packetInfos = ((PacketClient) PacketCoder.decode(is)).data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (channel.equals(CHANNEL_SSP)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(data);
                DataInputStream is = new DataInputStream(isRaw);
                PacketInfo pi = null;
                try {
                    pi = (PacketInfo) PacketCoder.decode(is);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                World w = mod_BlockHelper.proxy.getWorld();
                if (pi == null || pi.mop == null || w == null)
                    return;
                if (pi.mt == MopType.ENTITY) {
                    Entity en = pi.mop.entityHit;
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
                    if (w.isRemote) {
                        Packet250CustomPayload packet = new Packet250CustomPayload();
                        packet.channel = CHANNEL;
                        packet.data = fieldData;
                        packet.length = fieldData.length;
                        ModLoader.sendPacket(packet);
                    } else {
                        onPacketData(null, CHANNEL, fieldData);
                    }
                } else if (pi.mt == MopType.BLOCK) {
                    TileEntity te = w.getBlockTileEntity(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                    int bId = w.getBlockId(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    DataOutputStream os = new DataOutputStream(buffer);
                    PacketClient info = new PacketClient();
                    if (bId > 0) {
                        int meta = w.getBlockMetadata(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        Block b = Block.blocksList[bId];
                        BlockHelperModSupport.addInfo(info, b, bId, meta, te);
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
                        ModLoader.sendPacket(packet);
                    } else {
                        onPacketData(null, CHANNEL, fieldData);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

    private static int damageDropped(Block b, World w, int x, int y,
                                     int z, int meta) {
        List<ItemStack> list = b.getBlockDropped(w, x, y, z, meta, 0);
        if (!list.isEmpty()) {
            return list.get(0).getItemDamage();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    static Entity getEntityByID(World w, int entityId) {
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

}


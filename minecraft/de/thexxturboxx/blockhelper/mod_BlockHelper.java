package de.thexxturboxx.blockhelper;

import buildcraft.transport.TileGenericPipe;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import factorization.common.TileEntityCommon;
import ic2.common.Ic2Items;
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

public class mod_BlockHelper extends NetworkMod implements IConnectionHandler, IPacketHandler {

    private static final String MOD_ID = "BlockHelper";
    static final String NAME = "Block Helper";
    static final String VERSION = "0.9";
    static final String CHANNEL = "BlockHelperInfo";
    static final String CHANNEL_SSP = "BlockHelperInfoSSP";

    public static final MopType[] MOP_TYPES = MopType.values();

    private static final Random rnd = new Random();

    public static boolean isClient;

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
            BlockHelperUpdater.notifyUpdater(mc);
            updateKeyState();
            if (mc.currentScreen != null || isHidden)
                return true;
            int i = isLookingAtBlock(mc);
            if (i == 0)
                return true;
            MovingObjectPosition mop = mc.objectMouseOver;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(buffer);
            try {
                if (MOP_TYPES[i] == MopType.ENTITY) {
                    PacketCoder.encode(os, new PacketInfo(mc.theWorld.worldProvider.worldType, mop, MOP_TYPES[i],
                            mop.entityHit.entityId));
                } else {
                    PacketCoder.encode(os,
                            new PacketInfo(mc.theWorld.worldProvider.worldType, mop, MOP_TYPES[i]));
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
            switch (i) {
            case 1:
                int meta = mc.theWorld.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                int id = mc.theWorld.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                ItemStack is = new ItemStack(Block.blocksList[id], 1, meta);
                TileEntity te = mc.theWorld.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                String itemId = is.itemID + ":" + is.getItemDamage();
                String ct = null;
                String name = "";
                if (te != null) {
                    if (iof(te, "thermalexpansion.transport.tileentity.TileConduitLiquid")) {
                        is.setItemDamage(4096);
                    } else if (iof(te, "ic2.common.TileEntityCable")) {
                        is = new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, meta);
                    } else if (iof(te, "factorization.common.TileEntityCommon")) {
                        ct = "Factorization";
                        is.setItemDamage(((TileEntityCommon) te).getFactoryType().md);
                    } else if (iof(te, "codechicken.chunkloader.TileChunkLoaderBase")) {
                        ct = "ChickenChunks";
                    } else if (iof(te, "buildcraft.transport.TileGenericPipe")) {
                        TileGenericPipe pipe = (TileGenericPipe) te;
                        ct = "BuildCraft";
                        if (pipe.pipe != null) {
                            is = new ItemStack(Item.itemsList[pipe.pipe.itemID], te.blockMetadata);
                        }
                    }
                }
                Block b = Block.blocksList[id];
                if (is.getItem() == null)
                    return true;
                if (ct == null) {
                    ct = "Minecraft";
                }
                infoWidth = 0;
                int[] xy = drawBox(mc);
                currLine = 12;
                infos.clear();
                try {
                    name = is.getItem().getItemDisplayName(is);
                    if (name.equals(""))
                        throw new IllegalArgumentException();
                } catch (Throwable e) {
                    try {
                        ItemStack isNew = new ItemStack(b);
                        name = isNew.getItem().getItemDisplayName(isNew);
                        if (name.equals(""))
                            throw new IllegalArgumentException();
                    } catch (Throwable e1) {
                        try {
                            if (b != null) {
                                Item it = Item.itemsList[b.idDropped(meta, rnd, 0)];
                                ItemStack stack = new ItemStack(it, 1,
                                        damageDropped(b, mc.theWorld, mop.blockX, mop.blockY, mop.blockZ, meta));
                                name = it.getItemDisplayName(stack);
                            }
                            if (name.equals(""))
                                throw new IllegalArgumentException();
                        } catch (Throwable e2) {
                            name = "Please report this!";
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
                addInfo(name);
                addInfo(itemId);
                addInfo("§o" + ct.replaceAll("§.", ""), 0x000000ff);
                addInfo(packetInfos);
                addInfo((harvestable ? "§a\u2714" : "§4\u2718") + " §r" + harvest);
                drawInfo(xy, mc);
                break;
            case 2:
                Entity e = mop.entityHit;
                infoWidth = 0;
                xy = drawBox(mc);
                currLine = 12;
                infos.clear();
                String nameEntity = EntityList.getEntityString(e);
                if (e instanceof IMob) {
                    nameEntity = "§4" + nameEntity;
                }
                addInfo(nameEntity);
                addInfo(packetInfos);
                drawInfo(xy, mc);
                break;
            default:
                break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private void updateKeyState() {
        if (BlockHelperClientProxy.showHide.isPressed()) {
            isHidden = !isHidden;
        }
    }

    private int getStringMid(int[] xy, String s, Minecraft mc) {
        return xy[0] - mc.fontRenderer.getStringWidth(s) / 2;
    }

    private int isLookingAtBlock(Minecraft mc) {
        MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null)
            return 0;
        switch (mop.typeOfHit) {
        case ENTITY:
            return 2;
        case TILE:
            Material b = mc.theWorld.getBlockMaterial(mop.blockX, mop.blockY, mop.blockZ);
            if (b != null)
                return 1;
            else
                return 0;
        default:
            return 0;
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

    private static class FormatString {
        private final String str;
        private final int color;

        FormatString(String str, int color) {
            this.str = str;
            this.color = color;
        }
    }

    private static List<String> packetInfos = new ArrayList<String>();
    private static final List<FormatString> infos = new ArrayList<FormatString>();

    private void addInfo(List<String> info) {
        for (String s : info) {
            addInfo(s);
        }
    }

    private void addInfo(String info) {
        addInfo(info, 0xffffffff);
    }

    private void addInfo(String info, int color) {
        if (info != null && !info.equals("")) {
            infos.add(new FormatString(info, color));
        }
    }

    private static int currLine;
    private static int infoWidth = 0;
    private static final int dark = new Color(17, 2, 16).getRGB();
    public static int light = new Color(52, 18, 102).getRGB();

    private void drawInfo(int[] xy, Minecraft mc) {
        for (FormatString s : infos) {
            mc.fontRenderer.drawString(s.str, getStringMid(xy, s.str, mc), currLine, s.color);
            currLine = currLine + 8;
        }
    }

    private int[] drawBox(Minecraft mc) {
        int[] xy = new int[2];
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        if (BlockHelperClientProxy.mode != 1) {
            for (FormatString s : infos) {
                infoWidth = Math.max(mc.fontRenderer.getStringWidth(s.str) + 12, infoWidth);
            }
            infoWidth *= BlockHelperClientProxy.size;
            int minusHalf = (width - infoWidth) / 2;
            int plusHalf = (width + infoWidth) / 2;
            Gui.drawRect(minusHalf + 2, 7, plusHalf - 2, currLine + 5, dark);
            Gui.drawRect(minusHalf + 1, 8, plusHalf - 1, currLine + 4, dark);
            Gui.drawRect(minusHalf + 2, 8, plusHalf - 2, currLine + 4, light);
            Gui.drawRect(((width - infoWidth) / 2) + 3, 9, ((width + infoWidth) / 2) - 3, currLine + 3, dark);
        }
        xy[0] = width / 2;
        xy[1] = height / 2;
        return xy;
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
                World w = proxy.getWorld();
                if (pi == null || pi.mop == null || w == null)
                    return;
                if (pi.mt == MopType.ENTITY) {
                    Entity en = getEntityByID(w, pi.entityId);
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


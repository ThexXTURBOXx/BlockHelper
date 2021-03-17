package de.thexxturboxx.blockhelper;

import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
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
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Gui;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraftforge.common.DimensionManager;

public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    private static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    private static final String MOD_ID = "BlockHelper";
    static final String NAME = "Block Helper";
    static final String VERSION = "0.9";
    static final String CHANNEL = "BlockHelperInfo";

    public static final MopType[] MOP_TYPES = MopType.values();

    public static boolean isClient;

    private boolean isHidden = false;

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
        proxy.load(this);
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
        try {
            BlockHelperUpdater.notifyUpdater(mc);
            if (mc.theWorld.isRemote) {
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
                        PacketCoder.encode(os, new PacketInfo(mc.theWorld.provider.dimensionId, mop, MOP_TYPES[i],
                                mop.entityHit.entityId));
                    } else {
                        PacketCoder.encode(os,
                                new PacketInfo(mc.theWorld.provider.dimensionId, mop, MOP_TYPES[i]));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                byte[] fieldData = buffer.toByteArray();
                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = CHANNEL;
                packet.data = fieldData;
                packet.length = fieldData.length;
                PacketDispatcher.sendPacketToServer(packet);
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
                        } else if (iof(te, "ic2.core.block.wiring.TileEntityCable")) {
                            is = new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, meta);
                        } else if (iof(te, "codechicken.chunkloader.TileChunkLoaderBase")) {
                            ct = "ChickenChunks";
                        } else if (iof(te, "buildcraft.transport.TileGenericPipe")) {
                            TileGenericPipe pipe = (TileGenericPipe) te;
                            ct = "BuildCraft";
                            if (pipe.pipe != null && pipe.initialized) {
                                is = new ItemStack(Item.itemsList[pipe.pipe.itemID], te.blockMetadata);
                            }
                        }
                    }
                    Block b = Block.blocksList[id];
                    if (b != null) {
                        if (iof(b, "net.meteor.common.BlockMeteorShieldTorch")) {
                            is = new ItemStack(b.idDropped(0, null, 0), 1, meta);
                        }
                    }
                    if (is.getItem() == null)
                        return true;
                    if (ct == null) {
                        if (is.getItem().getCreativeTab() != null) {
                            if (is.getItem().getCreativeTab().getTabIndex() < 12) {
                                ct = "Minecraft";
                            } else {
                                ct = is.getItem().getCreativeTab().getTranslatedTabLabel();
                            }
                        } else {
                            ct = "Unknown";
                        }
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
                            ItemStack s = new ItemStack(b);
                            name = s.getItem().getItemDisplayName(s);
                            if (name.equals(""))
                                throw new IllegalArgumentException();
                        } catch (Throwable e1) {
                            try {
                                if (b != null) {
                                    ItemStack s = new ItemStack(Item.itemsList[b.idDropped(meta, new Random(), 0)], 1,
                                            damageDropped(b, mc.theWorld, mop.blockX, mop.blockY, mop.blockZ, meta));
                                    name = s.getItem().getItemDisplayName(s);
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
                        float hardness = b.getBlockHardness(proxy.getWorld(), mop.blockX, mop.blockY, mop.blockZ);
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
                    addAdditionalInfo(packetInfos);
                    addInfo((harvestable ? "§a✔" : "§4✘") + " §r" + harvest);
                    drawInfo(xy, mc);
                    break;
                case 2:
                    Entity e = mop.entityHit;
                    infoWidth = 0;
                    xy = drawBox(mc);
                    currLine = 12;
                    infos.clear();
                    String nameEntity = e.getEntityName();
                    if (e instanceof IMob) {
                        nameEntity = "§4" + nameEntity;
                    }
                    addInfo(nameEntity);
                    addAdditionalInfo(packetInfos);
                    drawInfo(xy, mc);
                    break;
                default:
                    break;
                }
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

    private void addAdditionalInfo(List<String> info) {
        for (String s : info) {
            addInfo("§7" + s);
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
    public void onPacketData(NetworkManager manager, Packet250CustomPayload packetGot, Player player) {
        try {
            if (packetGot.channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(packetGot.data);
                DataInputStream is = new DataInputStream(isRaw);
                if (isClient && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    try {
                        packetInfos = ((PacketClient) PacketCoder.decode(is)).data;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                    PacketInfo pi = null;
                    try {
                        pi = (PacketInfo) PacketCoder.decode(is);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (pi == null || pi.mop == null)
                        return;
                    World w = DimensionManager.getProvider(pi.dimId).worldObj;
                    if (pi.mt == MopType.ENTITY) {
                        Entity en = getEntityByID(w, pi.entityId);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        DataOutputStream os = new DataOutputStream(buffer);
                        PacketClient pc = new PacketClient();
                        if (en != null) {
                            try {
                                pc.add(((EntityLiving) en).getHealth() + " ❤ / "
                                        + ((EntityLiving) en).getMaxHealth() + " ❤");
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
                        Packet250CustomPayload packet = new Packet250CustomPayload();
                        packet.channel = CHANNEL;
                        packet.data = fieldData;
                        packet.length = fieldData.length;
                        PacketDispatcher.sendPacketToPlayer(packet, player);
                    } else if (pi.mt == MopType.BLOCK) {
                        TileEntity te = w.getBlockTileEntity(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        int id = w.getBlockId(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        DataOutputStream os = new DataOutputStream(buffer);
                        PacketClient info = new PacketClient();
                        if (id > 0) {
                            int meta = w.getBlockMetadata(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                            Block b = Block.blocksList[id];
                            BlockHelperModSupport.addInfo(info, b, id, meta, te);
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


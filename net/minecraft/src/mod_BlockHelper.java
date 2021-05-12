package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.BlockHelperUpdater;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;

import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.size;
import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.sizeInv;

public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    private static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    private static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "1.5.2";
    public static final String CHANNEL = "BlockHelperInfo";

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(FMLLog.getLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static boolean isClient;

    private static boolean firstTick = true;

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
        ModIdentifier.load();
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

            World w = mc.theWorld;
            if (w.isRemote) {
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
                        PacketCoder.encode(os, new PacketInfo(w.provider.dimensionId, mop, MopType.ENTITY,
                                mop.entityHit.entityId));
                    } else {
                        PacketCoder.encode(os,
                                new PacketInfo(w.provider.dimensionId, mop, result));
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
                switch (result) {
                case BLOCK:
                    int meta = w.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                    int id = w.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                    Block b = Block.blocksList[id];
                    TileEntity te = w.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                    ItemStack is = BlockHelperModSupport.getItemStack(new BlockHelperState(w, b, te, id, meta));
                    if (is == null) {
                        is = new ItemStack(b, 1, meta);
                    }
                    String itemId = is.itemID + ":" + is.getItemDamage();
                    if (is.getItem() == null)
                        return true;

                    String mod = BlockHelperModSupport.getMod(new BlockHelperState(w, b, te, id, meta));
                    mod = mod == null ? ModIdentifier.identifyMod(b) : mod;
                    mod = mod == null ? ModIdentifier.MINECRAFT : mod;

                    String name = BlockHelperModSupport.getName(new BlockHelperState(w, b, te, id, meta));
                    name = name == null ? "" : name;
                    if (name.isEmpty()) {
                        try {
                            name = is.getDisplayName();
                            if (name.isEmpty())
                                throw new IllegalArgumentException();
                        } catch (Throwable e) {
                            try {
                                name = new ItemStack(b).getDisplayName();
                                if (name.isEmpty())
                                    throw new IllegalArgumentException();
                            } catch (Throwable e1) {
                                try {
                                    if (b != null) {
                                        name = new ItemStack(Item.itemsList[b.idDropped(meta, new Random(), 0)], 1,
                                                b.damageDropped(meta)).getDisplayName();
                                    }
                                    if (name.isEmpty())
                                        throw new IllegalArgumentException();
                                } catch (Throwable e2) {
                                    try {
                                        if (b != null) {
                                            ItemStack s = b.getPickBlock(mop, w,
                                                    mop.blockX, mop.blockY, mop.blockZ);
                                            name = s.getItem().getItemDisplayName(s);
                                        }
                                        if (name.isEmpty())
                                            throw new IllegalArgumentException();
                                    } catch (Throwable e3) {
                                        if (b != null) {
                                            name = b.getLocalizedName();
                                        } else {
                                            name = "Please report this!";
                                        }
                                    }
                                }
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

                    infos.clear();
                    addInfo(name);
                    addInfo(itemId);
                    addInfo((harvestable ? "§a✔" : "§4✘") + " §r§7" + harvest);
                    addAdditionalInfo(packetInfos);
                    addInfo("§9§o" + mod);
                    int x = drawBox(mc);
                    drawInfo(x, mc);
                    break;
                case ENTITY:
                    Entity e = mop.entityHit;
                    String nameEntity = e.getEntityName();
                    if (e instanceof IMob) {
                        nameEntity = "§4" + nameEntity;
                    }
                    mod = ModIdentifier.identifyMod(e);
                    mod = mod == null ? ModIdentifier.MINECRAFT : mod;
                    infos.clear();
                    addInfo(nameEntity);
                    addAdditionalInfo(packetInfos);
                    addInfo("§9§o" + mod);
                    x = drawBox(mc);
                    drawInfo(x, mc);
                    break;
                default:
                    break;
                }
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

    private static List<String> packetInfos = new ArrayList<String>();
    private static final List<String> infos = new ArrayList<String>();

    private void addAdditionalInfo(List<String> info) {
        for (String s : info) {
            addInfo("§7" + s);
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
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packetGot, Player player) {
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
                        Entity en = w.getEntityByID(pi.entityId);
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
                            BlockHelperModSupport.addInfo(new BlockHelperState(w, b, te, id, meta), info);
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

    public static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

}


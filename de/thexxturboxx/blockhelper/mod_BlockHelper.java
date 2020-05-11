package de.thexxturboxx.blockhelper;

import appeng.me.basetiles.TilePoweredBase;
import buildcraft.energy.TileEngine;
import buildcraft.factory.TileMachine;
import buildcraft.factory.TileTank;
import buildcraft.transport.TileGenericPipe;
import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import factorization.common.TileEntityCommon;
import ic2.core.Ic2Items;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityElectricBlock;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.BaseMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import thermalexpansion.block.conduit.TileConduitLiquid;
import thermalexpansion.block.device.TileEnergyCell;
import thermalexpansion.block.device.TileTankPortable;
import thermalexpansion.block.engine.TileEngineRoot;
import thermalexpansion.block.machine.TileMachinePower;

public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    private static final String PACKAGE = "de.thexxturboxx.blockhelper.";

    @SidedProxy(clientSide = PACKAGE + "BlockHelperClientProxy", serverSide = PACKAGE + "BlockHelperCommonProxy")
    public static BlockHelperCommonProxy proxy;

    private static final String MOD_ID = "BlockHelper";
    static final String NAME = "Block Helper";
    static final String VERSION = "0.8.2";
    static final String CHANNEL = "BlockHelperInfo";
    public static boolean isClient;
    private static boolean firstTickUpdater = true;

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
            if (firstTickUpdater) {
                if (!BlockHelperUpdater.isLatestVersion()) {
                    if (BlockHelperUpdater.getLatestVersion().equals(VERSION)) {
                        mc.thePlayer.addChatMessage("§7[§6" + NAME + "§7] §4Update Check failed.");
                    } else {
                        mc.thePlayer.addChatMessage("§7[§6" + NAME + "§7] §bNew version available: §c" + VERSION + " §6==> §2"
                                + BlockHelperUpdater.getLatestVersion());
                    }
                    firstTickUpdater = false;
                } else if (!BlockHelperUpdater.getLatestVersionOrEmpty().equals("")) {
                    firstTickUpdater = false;
                }
            }
            if (mc.theWorld.isRemote) {
                if (mc.currentScreen != null)
                    return true;
                int i = isLookingAtBlock(mc);
                if (i == 0)
                    return true;
                MovingObjectPosition mop = mc.objectMouseOver;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream os = new DataOutputStream(buffer);
                try {
                    if (MopType.values()[i] == MopType.ENTITY) {
                        PacketCoder.encode(os, new PacketInfo(mc.theWorld.provider.dimensionId, mop, MopType.values()[i],
                                mop.entityHit.entityId));
                    } else {
                        PacketCoder.encode(os,
                                new PacketInfo(mc.theWorld.provider.dimensionId, mop, MopType.values()[i]));
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
                        } else if (iof(te, "factorization.common.TileEntityCommon")) {
                            ct = "Factorization";
                            is.setItemDamage(((TileEntityCommon) te).getFactoryType().md);
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
                    infosl.clear();
                    try {
                        name = is.getDisplayName();
                        if (name.equals(""))
                            throw new IllegalArgumentException();
                    } catch (Throwable e) {
                        try {
                            name = new ItemStack(b).getDisplayName();
                            if (name.equals(""))
                                throw new IllegalArgumentException();
                        } catch (Throwable e1) {
                            try {
                                if (b != null) {
                                    name = new ItemStack(Item.itemsList[b.idDropped(meta, new Random(), 0)], 1,
                                            b.damageDropped(meta)).getDisplayName();
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
                    String newharvest = (harvestable ? "§a✔" : "§4✘") + " §r" + harvest;
                    addInfo(name);
                    addInfo(itemId);
                    addInfo("§o" + ct.replaceAll("§.", ""), 0x000000ff);
                    addInfo(BlockHelperPackets.infosl);
                    addInfo(newharvest);
                    drawInfo(xy, mc);
                    break;
                case 2:
                    Entity e = mop.entityHit;
                    infoWidth = 0;
                    xy = drawBox(mc);
                    currLine = 12;
                    infos.clear();
                    infosl.clear();
                    addInfo(e.getEntityName());
                    addInfo(BlockHelperPackets.infosl);
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

    private static final Map<String, Font> infos = new HashMap<String, Font>();
    private static final List<String> infosl = new ArrayList<String>();

    private void addInfo(List<String> info) {
        for (String s : info) {
            addInfo(s);
        }
    }

    private void addInfo(String info) {
        addInfo(info, 0xffffffff);
    }

    private void addInfo(String info, int color) {
        if (!info.equals("")) {
            infos.put(info, new Font(color));
            infosl.add(info);
        }
    }

    private static int currLine;
    private static int infoWidth = 0;
    private static final int dark = new Color(17, 2, 16).getRGB();
    public static int light = new Color(52, 18, 102).getRGB();

    private void drawInfo(int[] xy, Minecraft mc) {
        for (String s : infosl) {
            mc.fontRenderer.drawString(s, getStringMid(xy, s, mc), currLine, infos.get(s).color);
            currLine = currLine + 8;
        }
    }

    private int[] drawBox(Minecraft mc) {
        int[] xy = new int[2];
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        if (BlockHelperClientProxy.mode != 1) {
            for (String s : infosl) {
                infoWidth = Math.max(mc.fontRenderer.getStringWidth(s) + 12, infoWidth);
            }
            int minushalf = ((width - infoWidth) / 2);
            int plushalf = ((width + infoWidth) / 2);
            Gui.drawRect(minushalf + 2, 7, plushalf - 2, currLine + 5, dark);
            Gui.drawRect(minushalf + 1, 8, plushalf - 1, currLine + 4, dark);
            Gui.drawRect(minushalf + 2, 8, plushalf - 2, currLine + 4, light);
            Gui.drawRect(((width - infoWidth) / 2) + 3, 9, ((width + infoWidth) / 2) - 3, currLine + 3, dark);
        }
        xy[0] = width / 2;
        xy[1] = height / 2;
        return xy;
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packetgot, Player player) {
        try {
            if (packetgot.channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(packetgot.data);
                DataInputStream is = new DataInputStream(isRaw);
                if (isClient && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    try {
                        BlockHelperPackets.setInfo((PacketClient) PacketCoder.decode(is));
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
                                pc.add((byte) 0, (((EntityLiving) en).getHealth() + " ❤ / "
                                        + ((EntityLiving) en).getMaxHealth() + " ❤"));
                                PacketCoder.encode(os, pc);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Throwable e) {
                                try {
                                    PacketCoder.encode(os, pc.add((byte) 0, ""));
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                PacketCoder.encode(os, pc.add((byte) 0, ""));
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
                        Block bt = null;
                        if (te != null) {
                            bt = te.getBlockType();
                        }
                        int id = w.getBlockId(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        DataOutputStream os = new DataOutputStream(buffer);
                        PacketClient info = new PacketClient();
                        if (id > 0) {
                            int meta = w.getBlockMetadata(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                            Block b = Block.blocksList[id];
                            if (iof(te, "ic2.core.block.machine.tileentity.TileEntityElectricMachine")) {
                                info.add(1, ((TileEntityElectricMachine) te).energy + " EU / "
                                        + ((TileEntityElectricMachine) te).maxEnergy + " EU");
                                if (iof(te, "ic2.core.block.machine.tileentity.TileEntityMatter")) {
                                    info.add(4, "Progress: " + ((TileEntityMatter) te).getProgressAsString());
                                }
                            } else if (iof(te, "ic2.core.block.wiring.TileEntityElectricBlock")) {
                                info.add(1, ((TileEntityElectricBlock) te).energy + " EU / "
                                        + ((TileEntityElectricBlock) te).maxStorage + " EU");
                            }
                            if (iof(bt, "thermalexpansion.block.machine.TileMachinePower")) {
                                info.add(1, ((TileMachinePower) bt).getPowerProvider().getEnergyStored() + " MJ / "
                                        + ((TileMachinePower) bt).getPowerProvider().getMaxEnergyStored() + " MJ");
                            } else if (iof(bt, "thermalexpansion.block.device.TileEnergyCell")) {
                                info.add(1, ((TileEnergyCell) bt).getPowerProvider().getEnergyStored() + " MJ / "
                                        + ((TileEnergyCell) bt).getPowerProvider().getMaxEnergyStored() + " MJ");
                            } else if (iof(bt, "thermalexpansion.block.engine.TileEngineRoot")) {
                                info.add(1, ((TileEngineRoot) bt).getPowerProvider().getEnergyStored() + " MJ / "
                                        + ((TileEngineRoot) bt).getPowerProvider().getMaxEnergyStored() + " MJ");
                            } else if (iof(bt, "thermalexpansion.block.device.TileTankPortable")) {
                                if (((TileTankPortable) bt).myTank.getLiquid() != null) {
                                    String name = getLiquidName(
                                            ((TileTankPortable) bt).myTank.getLiquid().asItemStack().itemID);
                                    if (name.equals("")) {
                                        info.add(1, "0 mB / " + ((TileTankPortable) bt).myTank.getCapacity() + " mB");
                                    } else {
                                        info.add(1, ((TileTankPortable) bt).getTankLiquid().amount + " mB / "
                                                + ((TileTankPortable) bt).myTank.getCapacity() + " mB of " + name);
                                    }
                                } else {
                                    info.add(1, "0 mB / " + ((TileTankPortable) bt).myTank.getCapacity() + " mB");
                                }
                            } else if (iof(bt, "thermalexpansion.block.conduit.TileConduitLiquid")) {
                                if (((TileConduitLiquid) bt).getRenderLiquid() != null) {
                                    String name = getLiquidName(((TileConduitLiquid) bt).liquidID);
                                    if (name.equals("")) {
                                        info.add(1,
                                                "0 mB / " + ((TileConduitLiquid) bt).myGrid.myTank.getCapacity() + " mB");
                                    } else {
                                        String liquid = ((((TileConduitLiquid) bt).myGrid.myTank.getCapacity() / 6.0D)
                                                * ((TileConduitLiquid) bt).liquidLevel) + "";
                                        info.add(1, liquid.substring(0, liquid.indexOf(".")) + " mB / "
                                                + ((TileConduitLiquid) bt).myGrid.myTank.getCapacity() + " mB of " + name);
                                    }
                                } else {
                                    info.add(1, "0 mB / " + ((TileConduitLiquid) bt).myGrid.myTank.getCapacity() + " mB");
                                }
                            }
                            if (iof(te, "appeng.me.basetiles.TilePoweredBase")) {
                                info.add(1, ((TilePoweredBase) te).storedPower + " AE / "
                                        + ((TilePoweredBase) te).maxStoredPower + " AE");
                            }
                            if (iof(te, "buildcraft.energy.TileEngine")) {
                                if (((TileEngine) te).engine != null) {
                                    info.add(1, ((TileEngine) te).engine.getEnergyStored() + " MJ / "
                                            + ((TileEngine) te).engine.maxEnergy + " MJ");
                                }
                            } else if (iof(te, "buildcraft.factory.TileMachine")) {
                                info.add(1, ((TileMachine) te).getPowerProvider().getEnergyStored() + " MJ / "
                                        + ((TileMachine) te).getPowerProvider().getMaxEnergyStored() + " MJ");
                            } else if (iof(te, "buildcraft.factory.TileTank")) {
                                if (((TileTank) te).tank.getLiquid() != null) {
                                    String name = getLiquidName(((TileTank) te).tank.getLiquid().asItemStack().itemID);
                                    if (name.equals("")) {
                                        info.add(1, "0 mB / " + ((TileTank) te).tank.getCapacity() + " mB");
                                    } else {
                                        info.add(1, ((TileTank) te).tank.getLiquid().amount + " mB / "
                                                + ((TileTank) te).tank.getCapacity() + " mB of " + name);
                                    }
                                } else {
                                    info.add(1, "0 mB / " + ((TileTank) te).tank.getCapacity() + " mB");
                                }
                            }
                            if (iof(te, "codechicken.chunkloader.TileChunkLoaderBase")) {
                                info.add(0, "Owner: " + ((TileChunkLoaderBase) te).getOwner());
                                info.add(1, "Active: " + firstUp(Boolean.toString(((TileChunkLoaderBase) te).active)));
                                if (iof(te, "codechicken.chunkloader.TileChunkLoader")) {
                                    int radius = ((TileChunkLoader) te).radius;
                                    info.add(2, "Radius: " + radius);
                                    info.add(3, "Shape: " + ((TileChunkLoader) te).shape.toString());
                                }
                            }
                            if (iof(te, "buildcraft.energy.TileEngine")) {
                                info.add(1, ((TileEngine) te).engine.getEnergyStored() + " MJ / "
                                        + ((TileEngine) te).engine.maxEnergy + " MJ");
                            }
                            if (iof(te, "net.meteor.common.tileentity.TileEntityMeteorShield")) {
                                if (meta == 0) {
                                    info.add(0, "State: Charging");
                                } else {
                                    info.add(0, "Radius: " + meta * 4 + "x" + meta * 4 + " Chunks");
                                }
                            }
                            if (iof(b, "net.meteor.common.block.BlockMeteorShieldTorch")) {
                                info.add(0, "State: "
                                        + (id == MeteorsMod.torchMeteorShieldActive.blockID ? "Protected" : "Unprotected"));
                            }
                            if (iof(b, "florasoma.crops.blocks.BerryBush")) {
                                int newmeta = MathHelper.floor_double(meta / 4d);
                                if (newmeta < 3) {
                                    String grow = ((int) ((newmeta / 2d) * 100)) + "";
                                    if (grow.equals("100")) {
                                        grow = "Mature";
                                    } else {
                                        grow = grow + "%";
                                    }
                                    info.add(2, "Growth State: " + grow);
                                } else {
                                    info.add(2, "Growth State: Ripe");
                                }
                            }
                            boolean crop = b instanceof BlockCrops;
                            double max_stage = 7d;
                            try {
                                if (!crop) {
                                    for (Method method : b.getClass().getDeclaredMethods()) {
                                        if (method.getName().equals("getGrowthRate")) {
                                            crop = true;
                                        }
                                    }
                                }
                            } catch (Throwable e) {
                            }
                            try {
                                if (crop) {
                                    if (iof(b, "florasoma.crops.blocks.FloraCropBlock")) {
                                        max_stage = 3d;
                                    } else {
                                        for (Field field : b.getClass().getFields()) {
                                            if (containsIgnoreCase(field.getName(), "max")
                                                    && containsIgnoreCase(field.getName(), "stage")) {
                                                field.setAccessible(true);
                                                max_stage = field.getInt(Block.blocksList[id]);
                                                break;
                                            }
                                        }
                                        for (Field field : b.getClass().getDeclaredFields()) {
                                            if (containsIgnoreCase(field.getName(), "max")
                                                    && containsIgnoreCase(field.getName(), "stage")) {
                                                field.setAccessible(true);
                                                max_stage = field.getInt(Block.blocksList[id]);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } catch (Throwable e) {
                            }
                            if (crop) {
                                String grow = ((int) ((meta / max_stage) * 100)) + "";
                                if (grow.equals("100")) {
                                    grow = "Mature";
                                } else {
                                    grow = grow + "%";
                                }
                                info.add(2, "Growth State: " + grow);
                            }
                            if (id == 55) {
                                info.add(3, "Strength: " + meta);
                            }
                            if (id == 69) {
                                String state = "Off";
                                if (meta >= 8) {
                                    state = "On";
                                }
                                info.add(3, "State: " + state);
                            }
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

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    private static String firstUp(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    static boolean iof(Object obj, String clazz) {
        if (obj == null)
            return false;
        return BlockHelperModSupport.isLoadedAndInstanceOf(obj, clazz);
    }

    private static String getLiquidName(int id) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (map.get(name).itemID == id)
                return name;
        }
        return "";
    }

}


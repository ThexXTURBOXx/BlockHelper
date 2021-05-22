package de.thexxturboxx.blockhelper.client;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperUpdater;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.fix.FixDetector;
import de.thexxturboxx.blockhelper.integration.MicroblockIntegration;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.Gui;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.opengl.GL11;

import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.size;
import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.sizeInv;

public class BlockHelperGui {

    public static final int PADDING = 12;

    public static final int DARK = new Color(17, 2, 16).getRGB();

    public static final int LIGHT = new Color(52, 18, 102).getRGB();

    private static final Random rnd = new Random();

    private static BlockHelperGui instance;

    private final List<String> infos;

    private List<String> packetInfos;

    private boolean firstTick;

    private boolean isHidden;

    private BlockHelperGui() {
        this.infos = new ArrayList<String>();
        this.packetInfos = new ArrayList<String>();
        this.firstTick = true;
        this.isHidden = false;
    }

    public boolean onTickInGame(Minecraft mc) {
        try {
            GL11.glScaled(size, size, size);

            if (firstTick) {
                ModIdentifier.firstTick();
                FixDetector.detectFixes(mc);
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
            World w = mc.theWorld;
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
            if (mod_BlockHelper.proxy.getWorld().isRemote) {
                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = mod_BlockHelper.CHANNEL;
                packet.data = fieldData;
                packet.length = fieldData.length;
                ModLoader.getMinecraftInstance().getSendQueue().addToSendQueue(packet);
            } else {
                mod_BlockHelper.INSTANCE.onPacketData(null, mod_BlockHelper.CHANNEL_SSP, fieldData);
            }
            switch (result) {
            case BLOCK:
                int meta = w.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                int id = w.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                Block b = Block.blocksList[id];
                TileEntity te = w.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                ItemStack is = BlockHelperModSupport.getItemStack(new BlockHelperBlockState(w, b, te, id, meta));
                if (is == null) {
                    is = new ItemStack(b, 1, meta);
                }

                // Microblocks support here, not in Mod support classes as they need extra data
                try {
                    ItemStack microblock = MicroblockIntegration.getMicroblock(w, mc.thePlayer, mop, te);
                    is = microblock == null ? is : microblock;
                } catch (Throwable ignored) {
                }

                String mod = BlockHelperModSupport.getMod(new BlockHelperBlockState(w, b, te, id, meta));
                mod = mod == null ? ModIdentifier.identifyMod(b) : mod;
                mod = mod == null ? ModIdentifier.MINECRAFT : mod;

                String itemId = is.itemID + ":" + is.getItemDamage();
                if (is.getItem() == null)
                    return true;

                String name = BlockHelperModSupport.getName(new BlockHelperBlockState(w, b, te, id, meta));
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
                                            mod_BlockHelper.damageDropped(b, w, mop.blockX, mop.blockY, mop.blockZ,
                                                    meta));
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
                    } else if (b.canHarvestBlock(mod_BlockHelper.proxy.getPlayer(), meta)) {
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
            Gui.drawRect(minusHalf + 2, 7, plusHalf - 2, currLine + 5, DARK);
            Gui.drawRect(minusHalf + 1, 8, plusHalf - 1, currLine + 4, DARK);
            Gui.drawRect(minusHalf + 2, 8, plusHalf - 2, currLine + 4, LIGHT);
            Gui.drawRect(minusHalf + 3, 9, plusHalf - 3, currLine + 3, DARK);
        }
        return width / 2;
    }

    public void setData(List<String> packetInfos) {
        this.packetInfos = packetInfos;
    }

    public static BlockHelperGui getInstance() {
        if (instance == null) {
            instance = new BlockHelperGui();
        }
        return instance;
    }

}

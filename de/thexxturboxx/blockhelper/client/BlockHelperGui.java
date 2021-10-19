package de.thexxturboxx.blockhelper.client;

import cpw.mods.fml.common.network.PacketDispatcher;
import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperUpdater;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.size;
import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.sizeInv;

public class BlockHelperGui {

    public static final int PADDING = 12;

    public static final int DARK = new Color(17, 2, 16).getRGB();

    public static final int LIGHT = new Color(52, 18, 102).getRGB();

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] fieldData = buffer.toByteArray();
                Packet250CustomPayload packet = new Packet250CustomPayload();
                packet.channel = mod_BlockHelper.CHANNEL;
                packet.data = fieldData;
                packet.length = fieldData.length;
                PacketDispatcher.sendPacketToServer(packet);
                switch (result) {
                case BLOCK:
                    int meta = w.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                    int id = w.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
                    Block b = Block.blocksList[id];
                    TileEntity te = w.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                    ItemStack is = BlockHelperModSupport.getItemStack(new BlockHelperBlockState(w, b, te, id, meta));
                    if (is == null) {
                        if (b == null) {
                            is = new ItemStack(id, 1, meta);
                        } else {
                            is = new ItemStack(b, 1, meta);
                        }
                    }
                    String itemId = is.itemID + ":" + is.getItemDamage();
                    if (is.getItem() == null)
                        return true;

                    String mod = BlockHelperModSupport.getMod(new BlockHelperBlockState(w, mop, b, te, id, meta));
                    mod = mod == null ? ModIdentifier.identifyMod(b) : mod;
                    mod = mod == null ? ModIdentifier.MINECRAFT : mod;

                    String name = BlockHelperModSupport.getName(new BlockHelperBlockState(w, mop, b, te, id, meta));
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
                        float hardness = b.getBlockHardness(mod_BlockHelper.proxy.getWorld(),
                                mop.blockX, mop.blockY, mop.blockZ);
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

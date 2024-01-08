package de.thexxturboxx.blockhelper.client;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.BlockHelperUpdater;
import de.thexxturboxx.blockhelper.ConstantRandom;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.fix.FixDetector;
import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.MicroblockIntegration;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiChat;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet230ModLoader;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.size;
import static de.thexxturboxx.blockhelper.BlockHelperClientProxy.sizeInv;

public class BlockHelperGui {

    public static final int PADDING = 12;

    private static final Random RND = new ConstantRandom();

    private static final RenderItem RENDER_ITEM = new RenderItem();

    private static BlockHelperGui instance;

    private final List<String> infos;

    private volatile List<String> packetInfos;

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
            GL11.glPushMatrix();
            GL11.glScaled(size, size, size);

            if (firstTick) {
                ModIdentifier.firstTick();
                FixDetector.detectFixes(mc);
                BlockHelperUpdater.notifyUpdater(mc);
                firstTick = false;
            }

            updateKeyState();

            if ((mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) // No open screen, except chat
                || isHidden // Key bind allows Block Helper to be hidden
                || (mc.gameSettings.showDebugInfo && BlockHelperClientProxy.shouldHideFromDebug) // F3 screen
                || !Minecraft.isGuiEnabled() // Cinema mode
                || (mc.thePlayer instanceof EntityClientPlayerMP // Together with next line fix player list
                    && mc.gameSettings.keyBindPlayerList.pressed))
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
                    PacketCoder.encode(os, new PacketInfo(w.worldProvider.worldType, mop, MopType.ENTITY,
                            mop.entityHit.entityId));
                } else {
                    PacketCoder.encode(os,
                            new PacketInfo(w.worldProvider.worldType, mop, result));
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Packet230ModLoader packet = new Packet230ModLoader();
            packet.modId = mod_BlockHelper.INSTANCE.getId();
            if (w.multiplayerWorld) {
                packet.dataInt = PacketCoder.toIntArray(mod_BlockHelper.CHANNEL, buffer.toString("ISO-8859-1"));
                ModLoaderMp.SendPacket(mod_BlockHelper.INSTANCE, packet);
            } else {
                packet.dataInt = PacketCoder.toIntArray(mod_BlockHelper.CHANNEL_SSP, buffer.toString("ISO-8859-1"));
                mod_BlockHelper.INSTANCE.HandlePacket(packet);
            }
            StringTranslate translator = StringTranslate.getInstance();
            switch (result) {
            case BLOCK:
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;
                int meta = w.getBlockMetadata(x, y, z);
                int id = w.getBlockId(x, y, z);
                Block b = Block.blocksList[id];
                TileEntity te = w.getBlockTileEntity(x, y, z);
                ItemStack is = BlockHelperModSupport.getItemStack(
                        new BlockHelperBlockState(translator, w, mop, b, te, id, meta));
                if (is == null) {
                    if (b == null) {
                        is = new ItemStack(id, 1, meta);
                    } else {
                        is = new ItemStack(b, 1, meta);
                    }
                }

                // Microblocks support here, not in Mod support classes as they need extra data
                try {
                    ItemStack microblock = MicroblockIntegration.getMicroblock(w, mc.thePlayer, mop, te);
                    is = microblock == null ? is : microblock;
                } catch (Throwable ignored) {
                }

                String mod = BlockHelperModSupport.getMod(new BlockHelperBlockState(translator,
                        w, mop, b, te, id, meta));
                mod = mod == null ? ModIdentifier.identifyMod(b) : mod;
                mod = mod == null ? ModIdentifier.MINECRAFT : mod;

                String itemId = is.itemID + ":" + is.getItemDamage();
                if (is.getItem() == null && b != null) {
                    is = new ItemStack(b.idDropped(meta, RND, id), 1, meta);
                }
                if (is.getItem() == null) {
                    return true;
                }

                String name = BlockHelperModSupport.getName(new BlockHelperBlockState(translator,
                        w, mop, b, te, id, meta));
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
                                    Item it = Item.itemsList[b.idDropped(meta, RND, 0)];
                                    ItemStack stack = new ItemStack(it, 1,
                                            mod_BlockHelper.damageDropped(b, meta));
                                    name = it.getItemDisplayName(stack);
                                }
                                if (name.isEmpty())
                                    throw new IllegalArgumentException();
                            } catch (Throwable e2) {
                                if (b != null) {
                                    name = b.translateBlockName();
                                } else {
                                    name = I18n.format("please_report");
                                }
                            }
                        }
                    }
                }

                String harvest = I18n.format("please_report");
                if (b != null) {
                    if (getHardness(b, meta) < 0.0F) {
                        harvest = I18n.format("unbreakable");
                    } else if (canHarvestBlock(b, mc.thePlayer, meta)) {
                        harvest = I18n.format("harvestable");
                    } else {
                        harvest = I18n.format("not_harvestable");
                    }
                }

                String breakProgression = null;
                if (mc.renderGlobal.damagePartialTime > 0) {
                    String progress = MathHelper.floor_float(100 * mc.renderGlobal.damagePartialTime) + "%";
                    breakProgression = I18n.format("break_progression", progress);
                }

                infos.clear();
                addInfo(name + (BlockHelperClientProxy.showItemId ? " (" + itemId + ")" : ""));
                if (BlockHelperClientProxy.showHarvest) {
                    addInfo(harvest);
                }
                if (BlockHelperClientProxy.showBreakProg) {
                    addInfo(breakProgression);
                }
                addAdditionalInfo(packetInfos);
                if (BlockHelperClientProxy.showMod) {
                    addInfo("\u00a79\u00a7o" + mod);
                }
                if (BlockHelperClientProxy.showBlock) {
                    int xBox = drawBox(mc, 22);
                    int yBox = drawInfo(xBox, mc, 22);
                    renderItem(mc, is, xBox + 3, (yBox + PADDING) / 2 - 8);
                } else {
                    int xBox = drawBox(mc, 0);
                    drawInfo(xBox, mc, 3);
                }
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
                if (BlockHelperClientProxy.showMod) {
                    addInfo("\u00a79\u00a7o" + mod);
                }
                int xBox = drawBox(mc, 0);
                drawInfo(xBox, mc, 3);
                break;
            default:
                break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            GL11.glPopMatrix();
        }
        return true;
    }

    private void updateKeyState() {
        if (BlockHelperClientProxy.showHide.isPressed()) {
            isHidden = !isHidden;
        }
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

    private int drawInfo(int x, Minecraft mc, int leftPadding) {
        int currLine = PADDING;
        for (String s : infos) {
            mc.fontRenderer.drawString(s, x + leftPadding, currLine, 0xffffffff);
            currLine += mc.fontRenderer.FONT_HEIGHT;
        }
        return currLine;
    }

    private int drawBox(Minecraft mc, int showcaseSize) {
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = (int) (res.getScaledWidth() * sizeInv);
        int infoWidth = 0;
        int currLine = PADDING;
        for (String s : infos) {
            infoWidth = Math.max(mc.fontRenderer.getStringWidth(s) + PADDING, infoWidth);
            currLine += mc.fontRenderer.FONT_HEIGHT;
        }
        infoWidth += showcaseSize;
        currLine = Math.max(currLine, showcaseSize);
        int minusHalf = (width - infoWidth) / 2;
        int plusHalf = (width + infoWidth) / 2;

        int bg = BlockHelperClientProxy.background;
        int grad1 = BlockHelperClientProxy.gradient1;
        int grad2 = BlockHelperClientProxy.gradient2;

        // Outer Borders
        drawGradientRect(minusHalf + 1, 8, minusHalf + 2, currLine + 4, bg, bg); // Left
        drawGradientRect(plusHalf - 2, 8, plusHalf - 1, currLine + 4, bg, bg); // Right
        drawGradientRect(minusHalf + 2, 7, plusHalf - 2, 8, bg, bg); // Top
        drawGradientRect(minusHalf + 2, currLine + 4, plusHalf - 2, currLine + 5, bg, bg); // Bottom

        // Center
        drawGradientRect(minusHalf + 3, 9, plusHalf - 3, currLine + 3, bg, bg);

        // Inner Borders
        drawGradientRect(minusHalf + 2, 8, minusHalf + 3, currLine + 4, grad1, grad2); // Left
        drawGradientRect(plusHalf - 3, 8, plusHalf - 2, currLine + 4, grad1, grad2); // Right
        drawGradientRect(minusHalf + 3, 8, plusHalf - 3, 9, grad1, grad1); // Top
        drawGradientRect(minusHalf + 3, currLine + 3, plusHalf - 3, currLine + 4, grad2, grad2); // Bottom

        return minusHalf + 3;
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

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float zLevel = 0.0F;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, zLevel);
        tessellator.addVertex(left, top, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, zLevel);
        tessellator.addVertex(right, bottom, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void renderItem(Minecraft mc, ItemStack is, int x, int y) {
        try {
            GL11.glPushMatrix();
            GL11.glRotatef(120.0f, 1.0f, 0.0f, 0.0f);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            RENDER_ITEM.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, is, x, y);
            GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            RenderHelper.disableStandardItemLighting();
            GL11.glPopMatrix();
        } catch (Throwable ignored) {
        }
    }

    public static boolean canHarvestBlock(Block b, EntityPlayer player, int meta) {
        try {
            return b.canHarvestBlock(player, meta);
        } catch (Throwable ignored) {
        }

        if (b.blockMaterial.getIsHarvestable())
            return true;
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack == null)
            return false;
        return stack.canHarvestBlock(b);
    }

    public static float getHardness(Block b, int meta) {
        try {
            return b.getHardness(meta);
        } catch (Throwable ignored) {
        }
        return b.getHardness();
    }

}

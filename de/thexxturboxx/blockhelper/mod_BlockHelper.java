package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.client.BlockHelperGui;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.BaseMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import static de.thexxturboxx.blockhelper.mod_BlockHelper.CHANNEL;

@NetworkMod(channels = {CHANNEL}, packetHandler = mod_BlockHelper.class)
public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    public static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.1.2";
    public static final String MC_VERSION = "1.5.2";
    public static final String CHANNEL = "BlockHelperInfo";
    public static mod_BlockHelper INSTANCE;

    public static final Logger LOGGER = Logger.getLogger(NAME);

    static {
        LOGGER.setParent(FMLLog.getLogger());
    }

    public static final MopType[] MOP_TYPES = MopType.values();

    public static boolean isClient;

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
        INSTANCE = this;
        proxy.load(this);
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return true;
        return BlockHelperGui.getInstance().onTickInGame(mc);
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packetGot, Player player) {
        try {
            if (packetGot.channel.equals(CHANNEL)) {
                ByteArrayInputStream isRaw = new ByteArrayInputStream(packetGot.data);
                DataInputStream is = new DataInputStream(isRaw);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                DataOutputStream os = new DataOutputStream(buffer);
                try {
                    if (isClient && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                        try {
                            BlockHelperGui.getInstance().setData(((PacketClient) PacketCoder.decode(is)).data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                        PacketInfo pi = null;
                        try {
                            pi = (PacketInfo) PacketCoder.decode(is);
                        } catch (IOException ignored) {
                        }

                        PacketClient info = new PacketClient();

                        if (pi != null && pi.mop != null) {
                            World w = DimensionManager.getProvider(pi.dimId).worldObj;
                            if (pi.mt == MopType.ENTITY) {
                                Entity en = w.getEntityByID(pi.entityId);
                                if (en != null) {
                                    if (BlockHelperCommonProxy.showHealth) {
                                        try {
                                            info.add(((EntityLiving) en).getHealth() + " ❤ / "
                                                    + ((EntityLiving) en).getMaxHealth() + " ❤");
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

                        byte[] fieldData = buffer.toByteArray();
                        Packet250CustomPayload packet = new Packet250CustomPayload();
                        packet.channel = CHANNEL;
                        packet.data = fieldData;
                        packet.length = fieldData.length;
                        PacketDispatcher.sendPacketToPlayer(packet, player);
                    }
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

}

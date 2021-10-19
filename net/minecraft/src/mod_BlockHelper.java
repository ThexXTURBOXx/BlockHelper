package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.MopType;
import de.thexxturboxx.blockhelper.PacketClient;
import de.thexxturboxx.blockhelper.PacketCoder;
import de.thexxturboxx.blockhelper.PacketInfo;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperEntityState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import de.thexxturboxx.blockhelper.client.BlockHelperGui;
import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class mod_BlockHelper extends BaseMod implements IPacketHandler {

    public static final String PACKAGE = "de.thexxturboxx.blockhelper.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "1.0.0";
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
        ModIdentifier.load();
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (pi == null || pi.mop == null)
                            return;

                        World w = DimensionManager.getProvider(pi.dimId).worldObj;
                        PacketClient info = new PacketClient();
                        if (pi.mt == MopType.ENTITY) {
                            Entity en = w.getEntityByID(pi.entityId);
                            if (en != null) {
                                try {
                                    info.add(((EntityLiving) en).getHealth() + " ❤ / "
                                            + ((EntityLiving) en).getMaxHealth() + " ❤");
                                } catch (Throwable ignored) {
                                }

                                BlockHelperModSupport.addInfo(new BlockHelperEntityState(w, en), info);
                            }
                        } else if (pi.mt == MopType.BLOCK) {
                            TileEntity te = w.getBlockTileEntity(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                            int id = w.getBlockId(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                            if (id > 0) {
                                int meta = w.getBlockMetadata(pi.mop.blockX, pi.mop.blockY, pi.mop.blockZ);
                                Block b = Block.blocksList[id];
                                BlockHelperModSupport.addInfo(
                                        new BlockHelperBlockState(w, pi.mop, b, te, id, meta), info);
                            }
                        } else {
                            return;
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

    public static boolean iof(Object obj, String clazz) {
        return BlockHelperInfoProvider.isLoadedAndInstanceOf(obj, clazz);
    }

}


package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;

public class Packet0x03NBTData implements IWailaPacket {

    private NBTTagCompound tag;

    public Packet0x03NBTData() {
    }

    public Packet0x03NBTData(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        NBTUtil.writeNBTTagCompound(tag, target);
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            this.tag = NBTUtil.readNBTTagCompound(dat);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass(), null);
        }
    }

    @Override
    public void handleClient() {
        DataAccessorCommon.INSTANCE.setNBTData(tag);
    }

    @Override
    public void handleServer(Player rawSender) {
    }

}

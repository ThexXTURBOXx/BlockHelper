package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.nbt.NBTTagCompound;

public class Packet0x04EntNBTData implements IWailaPacket {

    NBTTagCompound tag;

    public Packet0x04EntNBTData() {
    }

    public Packet0x04EntNBTData(NBTTagCompound tag) {
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
        } catch (Exception e) {
            WailaExceptionHandler.handleErr(e, this.getClass().toString(), null);
        }
    }

    @Override
    public void handle(Player player) {
        DataAccessorCommon.instance.setNBTData(tag);
    }

}

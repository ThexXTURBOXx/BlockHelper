package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.List;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class Packet0x03EntRequest implements IWailaPacket {

    public int dim;
    public int id;
    public HashSet<String> keys = new HashSet<String>();

    public Packet0x03EntRequest() {
    }

    public Packet0x03EntRequest(Entity ent, HashSet<String> keys) {
        this.dim = ent.worldObj.provider.dimensionId;
        this.id = ent.entityId;
        this.keys = keys;
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeInt(dim);
        target.writeInt(id);
        target.writeInt(this.keys.size());

        for (String key : keys)
            Packet.writeString(key, target);
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            dim = dat.readInt();
            id = dat.readInt();


            int nkeys = dat.readInt();

            for (int i = 0; i < nkeys; i++)
                this.keys.add(Packet.readString(dat, 250));

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass().toString(), null);
        }
    }

    @Override
    public void handle(Player player) {
        World world = DimensionManager.getWorld(dim);
        Entity entity = world.getEntityByID(id);

        if (entity != null) {
            try {
                NBTTagCompound tag = new NBTTagCompound();

                if (WailaRegistrar.instance().hasNBTEntityProviders(entity)) {
                    for (List<IEntityProvider> providersList :
                            WailaRegistrar.instance().getNBTEntityProviders(entity).values()) {
                        for (IEntityProvider provider : providersList) {
                            try {
                                tag = provider.getNBTData((EntityPlayerMP) player, entity, tag, world);
                            } catch (AbstractMethodError ame) {
                                tag = NBTUtil.getNBTData(provider, entity, tag);
                            }
                        }
                    }

                } else {
                    entity.writeToNBT(tag);
                    tag = NBTUtil.createTag(tag, keys);
                }

                tag.setInteger("WailaEntityID", entity.entityId);

                WailaPacketHandler.sendPacketToPlayer(new Packet0x04EntNBTData(tag), player);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, entity.getClass().toString(), null);
            }
        }
    }

}

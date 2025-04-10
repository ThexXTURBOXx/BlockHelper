package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.utils.AccessHelper;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class Packet0x01TERequest {

	private static Field classToNameMap = null;

	static{
		try{
			classToNameMap = TileEntity.class.getDeclaredField("classToNameMap");
			classToNameMap.setAccessible(true);
		} catch (Exception e){

			try{
				classToNameMap = TileEntity.class.getDeclaredField("field_70323_b");
				classToNameMap.setAccessible(true);
			} catch (Exception f){
				throw new RuntimeException(f);
			}

		}
	}

	public byte header;
	public int worldID;
	public int posX;
	public int posY;
	public int posZ;
	public HashSet<String> keys = new HashSet<String> ();

	public Packet0x01TERequest(Packet250CustomPayload packet){
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		keys.clear();
		try{
			this.header  = inputStream.readByte();
			this.worldID = inputStream.readInt();
			this.posX    = inputStream.readInt();
			this.posY    = inputStream.readInt();
			this.posZ    = inputStream.readInt();

			int nkeys    = inputStream.readInt();
			for (int i = 0; i < nkeys; i++)
				keys.add(Packet.readString(inputStream, 250));

		} catch (IOException e){}
	}

	public static Packet250CustomPayload create(World world, MovingObjectPosition mop, HashSet<String> keys){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1 + 4 + 4 + 4 + 4);
		DataOutputStream outputStream = new DataOutputStream(bos);

		keys.add("x"); keys.add("y"); keys.add("z");

		try{
			outputStream.writeByte(0x01);
			outputStream.writeInt(world.provider.dimensionId);
			outputStream.writeInt(mop.blockX);
			outputStream.writeInt(mop.blockY);
			outputStream.writeInt(mop.blockZ);
			outputStream.writeInt(keys.size());

			for (String key : keys)
				Packet.writeString(key, outputStream);

		}catch(IOException e){}

		packet.channel = "Waila";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();

		return packet;
	}

	public static void handle(Packet250CustomPayload packet, Player player) {
		Packet0x01TERequest castedPacket = new Packet0x01TERequest(packet);
		World world  = DimensionManager.getWorld(castedPacket.worldID);
		TileEntity entity = world.getBlockTileEntity(castedPacket.posX, castedPacket.posY, castedPacket.posZ);
		Block block  = Block.blocksList[world.getBlockId(castedPacket.posX, castedPacket.posY, castedPacket.posZ)];
		if (entity != null){
			try{
				NBTTagCompound tag = new NBTTagCompound();
				boolean hasNBTBlock = ModuleRegistrar.instance().hasNBTProviders(block);
				boolean hasNBTEnt   = ModuleRegistrar.instance().hasNBTProviders(entity);

				if (hasNBTBlock || hasNBTEnt){
					tag.setInteger("x", castedPacket.posX);
					tag.setInteger("y", castedPacket.posY);
					tag.setInteger("z", castedPacket.posZ);
					tag.setString ("id", (String)((HashMap)classToNameMap.get(null)).get(entity.getClass()));

					for (List<IWailaDataProvider> providersList : ModuleRegistrar.instance().getNBTProviders(block).values()){
						for (IWailaDataProvider provider : providersList){
							try{
								tag = provider.getNBTData((EntityPlayerMP) player, entity, tag, world, castedPacket.posX, castedPacket.posY, castedPacket.posZ);
							} catch (Throwable t){
								tag = AccessHelper.getNBTData(provider, entity, tag, world, castedPacket.posX, castedPacket.posY, castedPacket.posZ);
							}
						}
					}


					for (List<IWailaDataProvider> providersList : ModuleRegistrar.instance().getNBTProviders(entity).values()){
						for (IWailaDataProvider provider : providersList){
							try{
								tag = provider.getNBTData((EntityPlayerMP) player, entity, tag, world, castedPacket.posX, castedPacket.posY, castedPacket.posZ);
							} catch (Throwable t){
								tag = AccessHelper.getNBTData(provider, entity, tag, world, castedPacket.posX, castedPacket.posY, castedPacket.posZ);
							}
						}
					}

				} else {
					entity.writeToNBT(tag);
					tag = NBTUtil.createTag(tag, castedPacket.keys);
				}

				tag.setInteger("WailaX", castedPacket.posX);
				tag.setInteger("WailaY", castedPacket.posY);
				tag.setInteger("WailaZ", castedPacket.posZ);
				tag.setString ("WailaID", (String)((HashMap)classToNameMap.get(null)).get(entity.getClass()));
				PacketDispatcher.sendPacketToPlayer(Packet0x02TENBTData.create(tag), player);
			}catch(Throwable e){
				WailaExceptionHandler.handleErr(e, entity.getClass().toString(), null);
			}
		}
	}

}

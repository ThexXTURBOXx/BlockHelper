package mcp.mobius.waila.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to the game
 * engine.<br>
 * It will also return things that are unmodified by the overriding systems (like getWailaStack).<br>
 * Common accessor for both Entity and Block/TileEntity.<br>
 * Available data depends on what it is called upon (i.e. getEntity() will return null if looking at a block, etc.).<br>
 */
public interface ICommonAccessor {

    World getWorld();

    EntityPlayer getPlayer();

    MovingObjectPosition getPosition();

    Vec3 getRenderingPosition();

    NBTTagCompound getNBTData();

    int getNBTInteger(String keyname);

    int getNBTInteger(NBTTagCompound tag, String keyname);

    double getPartialFrame();

}

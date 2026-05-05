package mcp.mobius.waila.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to the game
 * engine.</br>
 * It will also return things that are unmodified by the overriding systems (like getStack).
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

    void clear();

}

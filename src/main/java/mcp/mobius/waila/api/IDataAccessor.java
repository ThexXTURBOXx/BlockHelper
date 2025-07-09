package mcp.mobius.waila.api;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to the game
 * engine.</br>
 * It will also return things that are unmodified by the overriding systems (like getWailaStack).</br>
 * An instance of this interface is passed to most of Waila Block/TileEntity callbacks.
 */
public interface IDataAccessor extends ICommonAccessor {

    Block getBlock();

    int getBlockID();

    int getMetadata();

    TileEntity getTileEntity();

    WailaDirection getSide();

    ItemStack getStack();

}

package mcp.mobius.waila.api.impl;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.WailaDirection;
import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class DataAccessorCommon implements ICommonAccessor, IDataAccessor, IEntityAccessor {

    public World world;
    public EntityPlayer player;
    public MovingObjectPosition mop;
    public Vec3 renderingvec = null;
    public Block block;
    public int blockID;
    public int metadata;
    public TileEntity tileEntity;
    public Entity entity;
    public NBTTagCompound remoteNbt = new NBTTagCompound();
    public long timeLastUpdate = System.currentTimeMillis();
    public double partialFrame;
    public ItemStack stack;

    public static final DataAccessorCommon INSTANCE = new DataAccessorCommon();

    public void set(World _world, EntityPlayer _player, MovingObjectPosition _mop) {
        this.set(_world, _player, _mop, null, 0.0);
    }

    public void set(World _world, EntityPlayer _player, MovingObjectPosition _mop, EntityLiving viewEntity,
                    double partialTicks) {
        this.world = _world;
        this.player = _player;
        this.mop = _mop;

        if (this.mop == null) {
            this.renderingvec = null;
            this.block = null;
            this.blockID = 0;
            this.metadata = 0;
            this.tileEntity = null;
            this.entity = null;
            this.remoteNbt = new NBTTagCompound();
            this.timeLastUpdate = System.currentTimeMillis();
            this.partialFrame = 0;
            this.stack = null;
        } else {
            if (this.mop.typeOfHit == EnumMovingObjectType.TILE) {
                this.blockID = world.getBlockId(this.mop.blockX, this.mop.blockY, this.mop.blockZ);
                this.metadata = world.getBlockMetadata(this.mop.blockX, this.mop.blockY, this.mop.blockZ);
                this.block = Block.blocksList[this.blockID];
                this.tileEntity = world.getBlockTileEntity(this.mop.blockX, this.mop.blockY, this.mop.blockZ);
                this.entity = null;
                try {
                    this.stack = new ItemStack(this.block, 1, this.metadata);
                } catch (Throwable ignored) {
                }
            } else if (this.mop.typeOfHit == EnumMovingObjectType.ENTITY) {
                this.block = null;
                this.metadata = -1;
                this.tileEntity = null;
                this.stack = null;
                this.entity = this.mop.entityHit;
            }

            if (viewEntity != null) {
                double px = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
                double py = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
                double pz = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
                this.renderingvec = Vec3.createVectorHelper(
                        this.mop.blockX - px, this.mop.blockY - py, this.mop.blockZ - pz);
                this.partialFrame = partialTicks;
            }
        }
    }

    public void clear() {
        this.set(null, null, null);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public int getBlockID() {
        return this.blockID;
    }

    @Override
    public int getMetadata() {
        return this.metadata;
    }

    @Override
    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public MovingObjectPosition getPosition() {
        return this.mop;
    }

    @Override
    public Vec3 getRenderingPosition() {
        return this.renderingvec;
    }

    @Override
    public NBTTagCompound getNBTData() {
        if (this.tileEntity != null && this.isTagCorrectTileEntity(this.remoteNbt))
            return remoteNbt;

        if (this.entity != null && this.isTagCorrectEntity(this.remoteNbt))
            return remoteNbt;

        if (this.tileEntity != null) {
            NBTTagCompound tag = new NBTTagCompound();
            try {
                this.tileEntity.writeToNBT(tag);
            } catch (Throwable ignored) {
            }
            return tag;
        }

        if (this.entity != null) {
            NBTTagCompound tag = new NBTTagCompound();
            try {
                this.entity.writeToNBT(tag);
            } catch (Throwable ignored) {
            }
            return tag;
        }

        return new NBTTagCompound();
    }

    public void setNBTData(NBTTagCompound tag) {
        if (tag != null)
            this.remoteNbt = tag;
    }

    private boolean isTagCorrectTileEntity(NBTTagCompound tag) {
        if (tag == null || !tag.hasKey("WailaX") || !tag.hasKey("WailaY") || !tag.hasKey("WailaZ")) {
            this.timeLastUpdate = System.currentTimeMillis() - 250;
            return false;
        }

        int x = tag.getInteger("WailaX");
        int y = tag.getInteger("WailaY");
        int z = tag.getInteger("WailaZ");

        if (x == this.mop.blockX && y == this.mop.blockY && z == this.mop.blockZ)
            return true;
        else {
            this.timeLastUpdate = System.currentTimeMillis() - 250;
            return false;
        }
    }

    private boolean isTagCorrectEntity(NBTTagCompound tag) {
        if (tag == null || !tag.hasKey("WailaEntityID")) {
            this.timeLastUpdate = System.currentTimeMillis() - 250;
            return false;
        }

        int id = tag.getInteger("WailaEntityID");

        if (id == this.entity.entityId)
            return true;
        else {
            this.timeLastUpdate = System.currentTimeMillis() - 250;
            return false;
        }
    }

    @Override
    public int getNBTInteger(String keyname) {
        return getNBTInteger(getNBTData(), keyname);
    }

    @Override
    public int getNBTInteger(NBTTagCompound tag, String keyname) {
        return NBTUtil.getNBTInteger(tag, keyname);
    }

    @Override
    public double getPartialFrame() {
        return this.partialFrame;
    }

    @Override
    public WailaDirection getSide() {
        return WailaDirection.getOrientation(this.getPosition().sideHit);
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    public boolean isTimeElapsed(long time) {
        return System.currentTimeMillis() - this.timeLastUpdate >= time;
    }

    public void resetTimer() {
        this.timeLastUpdate = System.currentTimeMillis();
    }

}

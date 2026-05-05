package mcp.mobius.waila.overlay;

import forge.Configuration;
import forge.IShearable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public class RayTracing {

    private static RayTracing _instance;

    private MovingObjectPosition target = null;

    private RayTracing() {
        _instance = this;
    }

    public static RayTracing instance() {
        return _instance == null ? new RayTracing() : _instance;
    }

    public void fire() {
        final Minecraft mc = ModLoader.getMinecraftInstance();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY
            && shouldShowEntity(mc.objectMouseOver.entityHit)) {
            this.target = mc.objectMouseOver;
            return;
        }
        EntityLiving viewpoint = mc.renderViewEntity;
        if (viewpoint == null) return;
        this.target = this.rayTrace(viewpoint, mc.playerController.getBlockReachDistance(), 0);
    }

    public void clear() {
        this.target = null;
    }

    private static boolean shouldShowEntity(Entity entity) {
        return true;
    }

    public MovingObjectPosition getTarget() {
        return this.target;
    }

    public ItemStack getTargetStack() {
        return this.getIdentifierStack();
    }

    public Entity getTargetEntity() {
        return this.target.typeOfHit == EnumMovingObjectType.ENTITY ? this.getIdentifierEntity() : null;
    }

    public MovingObjectPosition rayTrace(EntityLiving entity, double par1, float par3) {
        Vec3D vec3 = entity.getPosition(par3);
        Vec3D vec31 = entity.getLook(par3);
        Vec3D vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);

        //if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, false))
        if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, false))
            return entity.worldObj.rayTraceBlocks_do(vec3, vec32, true);
        else
            return entity.worldObj.rayTraceBlocks_do(vec3, vec32, false);
    }

    public ItemStack getIdentifierStack() {
        List<ItemStack> items = null;
        try {
            items = this.getIdentifierItems();
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "RayTracing#getIdentifierStack", null);
        }

        if (items == null || items.isEmpty()) return null;

        Collections.sort(items, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack stack0, ItemStack stack1) {
                return stack1.getItemDamage() - stack0.getItemDamage();
            }
        });

        return items.get(0);
    }

    public Entity getIdentifierEntity() {
        List<Entity> ents = new ArrayList<Entity>();

        if (this.target == null) return null;

        if (WailaRegistrar.instance().hasOverrideEntityProviders(this.target.entityHit)) {
            for (List<IEntityProvider> listProviders :
                    WailaRegistrar.instance().getOverrideEntityProviders(this.target.entityHit).values()) {
                for (IEntityProvider provider : listProviders) {
                    ents.add(provider.getOverride(DataAccessorCommon.INSTANCE, PluginConfig.instance()));
                }
            }
        }

        return !ents.isEmpty() ? ents.get(0) : this.target.entityHit;
    }

    public List<ItemStack> getIdentifierItems() {
        List<ItemStack> items = new ArrayList<ItemStack>();

        if (this.target == null) return items;

        switch (this.target.typeOfHit) {
        case ENTITY:
            if (this.target.entityHit != null && WailaRegistrar.instance().hasStackEntityProviders(this.target.entityHit)) {
                for (List<IEntityProvider> providersList :
                        WailaRegistrar.instance().getStackEntityProviders(this.target.entityHit).values()) {
                    for (IEntityProvider provider : providersList) {
                        ItemStack providerStack = provider.getDisplayItem(DataAccessorCommon.INSTANCE,
                                PluginConfig.instance());
                        if (providerStack != null) {
                            if (providerStack.getItem() == null)
                                return new ArrayList<ItemStack>();
                            items.add(providerStack);
                        }
                    }
                }
            }
            break;
        case TILE:
            World world = ModLoader.getMinecraftInstance().theWorld;
            int x = this.target.blockX;
            int y = this.target.blockY;
            int z = this.target.blockZ;
            int blockID = world.getBlockId(x, y, z);
            Block mouseoverBlock = Block.blocksList[blockID];
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (mouseoverBlock == null) return items;

            if (WailaRegistrar.instance().hasStackProviders(mouseoverBlock)) {
                for (List<IDataProvider> providersList :
                        WailaRegistrar.instance().getStackProviders(mouseoverBlock).values()) {
                    for (IDataProvider provider : providersList) {
                        ItemStack providerStack = provider.getStack(DataAccessorCommon.INSTANCE,
                                PluginConfig.instance());
                        if (providerStack != null) {
                            if (providerStack.getItem() == null)
                                return new ArrayList<ItemStack>();
                            items.add(providerStack);
                        }
                    }
                }
            }

            if (tileEntity != null && WailaRegistrar.instance().hasStackProviders(tileEntity)) {
                for (List<IDataProvider> providersList :
                        WailaRegistrar.instance().getStackProviders(tileEntity).values()) {

                    for (IDataProvider provider : providersList) {
                        ItemStack providerStack = provider.getStack(DataAccessorCommon.INSTANCE,
                                PluginConfig.instance());
                        if (providerStack != null) {

                            if (providerStack.getItem() == null)
                                return new ArrayList<ItemStack>();

                            items.add(providerStack);
                        }
                    }
                }
            }

            if (!items.isEmpty()) return items;

            if (world.getBlockTileEntity(x, y, z) == null) {
                try {
                    ItemStack block = new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z));

                    if (block.getItem() != null)
                        items.add(block);
                    //else
                    //	items.add(new ItemStack(new ItemBlock(mouseoverBlock)));
                    //else
                    //	items.add(new ItemStack(Item.getItemFromBlock(mouseoverBlock)));


                } catch (Throwable ignored) {
                }
            }

            if (!items.isEmpty()) return items;

            try {
                items.add(new ItemStack(blockID, 1, world.getBlockMetadata(x, y, z)));
            } catch (Throwable ignored) {
            }

            if (!items.isEmpty()) return items;

            /*
            try
            {
                items.addAll(mouseoverBlock.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0));
            }
            catch(Exception e){}

            if(items.size() > 0)
                return items;
            */

            if (mouseoverBlock instanceof IShearable) {
                IShearable shearable = (IShearable) mouseoverBlock;
                if (shearable.isShearable(new ItemStack(Item.shears), world, x, y, z)) {
                    items.addAll(shearable.onSheared(new ItemStack(Item.shears), world, x, y, z, 0));
                }
            }

            if (items.isEmpty())
                items.add(0, new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z)));
            break;
        }

        return items;
    }

}

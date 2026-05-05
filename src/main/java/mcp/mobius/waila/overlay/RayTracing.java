package mcp.mobius.waila.overlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.ConstantRandom;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.IShearable;

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
        final Minecraft mc = Minecraft.getMinecraft();
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
        // Check if entity is player with invisibility effect
        if (entity instanceof EntityPlayer) {
            boolean setting = PluginConfig.instance().get("general.invisibleplayers");
            return setting || !entity.isInvisible();
        }
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
        Vec3 vec3 = entity.getPosition(par3);
        Vec3 vec31 = entity.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);

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
                boolean valid0 = !DisplayUtil.itemDisplayNameShortUnformatted(stack0).equals(DisplayUtil.UNNAMED);
                boolean valid1 = !DisplayUtil.itemDisplayNameShortUnformatted(stack1).equals(DisplayUtil.UNNAMED);
                if (valid0 != valid1)
                    return valid0 ? -1 : 1;
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
            World world = Minecraft.getMinecraft().theWorld;
            int x = this.target.blockX;
            int y = this.target.blockY;
            int z = this.target.blockZ;
            int blockID = world.getBlockId(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
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

            if (listValid(items)) return items;

            if (world.getBlockTileEntity(x, y, z) == null) {
                try {
                    ItemStack block = new ItemStack(mouseoverBlock, 1, meta);

                    if (block.getItem() != null)
                        items.add(block);
                    //else
                    //	items.add(new ItemStack(new ItemBlock(mouseoverBlock)));
                    //else
                    //	items.add(new ItemStack(Item.getItemFromBlock(mouseoverBlock)));

                } catch (Throwable ignored) {
                }
            }

            if (listValid(items)) return items;

            try {
                ItemStack pick = mouseoverBlock.getPickBlock(this.target, world, x, y, z);
                if (pick != null)
                    items.add(pick);
            } catch (Throwable ignored) {
            }

            if (listValid(items)) return items;

            /*
            try
            {
                items.addAll(mouseoverBlock.getBlockDropped(world, x, y, z, meta, 0));
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

            if (listValid(items)) return items;

            ItemStack fallback = new ItemStack(mouseoverBlock.idDropped(meta, ConstantRandom.INSTANCE, blockID),
                    1, mouseoverBlock.damageDropped(meta));
            if (fallback.getItem() != null)
                items.add(fallback);

            break;
        }

        return items;
    }

    private static boolean listValid(List<ItemStack> list) {
        for (ItemStack stack : list) {
            if (stack == null) continue;
            String name = DisplayUtil.itemDisplayNameShortUnformatted(stack);
            if (!name.equals(DisplayUtil.UNNAMED)) return true;
        }
        return false;
    }

}

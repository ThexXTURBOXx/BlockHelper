package mcp.mobius.waila.addons.barrels;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.barrels.BarrelsPlugin.TileBarrel;
import static mcp.mobius.waila.addons.barrels.BarrelsPlugin.TileBarrel_getInventorySize;

public final class HUDHandlerBarrels implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBarrels();

    private HUDHandlerBarrels() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (TileBarrel.isInstance(accessor.getTileEntity())) {
            int itemID = accessor.getNBTInteger("item");

            ItemStack stored = null;
            if (itemID > 0 && Item.itemsList[itemID] != null)
                stored = new ItemStack(itemID,
                        accessor.getNBTInteger("size"),
                        accessor.getNBTInteger("metadata"));

            if (stored != null) {
                if (accessor.getNBTData().hasKey("tag"))
                    stored.setTagCompound(accessor.getNBTData().getCompoundTag("tag"));

                if (config.get("barrels.itemtype")) {
                    currenttip.add(DisplayUtil.itemDisplayNameShortUnformatted(stored));
                }
                if (config.get("barrels.itemnumb")) {
                    currenttip.add(stored.stackSize + " / " + accessor.getNBTInteger("invSize") +
                                   " " + I18n.translate("hud.msg.items"));
                }
                if (config.get("barrels.space")) {
                    currenttip.add(accessor.getNBTInteger("stackLimit") + " " +
                                   I18n.translate("hud.msg.stacks") + " max");
                }
            } else {
                if (config.get("barrels.itemtype")) {
                    currenttip.add(I18n.translate("hud.msg.empty"));
                }
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (TileBarrel.isInstance(accessor.getTileEntity())) {
            try {
                int itemID = tag.getInteger("item");
                if (itemID > 0 && Item.itemsList[itemID] != null) {
                    int invSize = (Integer) TileBarrel_getInventorySize.invoke(accessor.getTileEntity());
                    tag.setInteger("invSize", invSize);
                    int stackLimit = invSize / Item.itemsList[itemID].getItemStackLimit();
                    tag.setInteger("stackLimit", stackLimit);
                }
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
            }
        }
    }

}

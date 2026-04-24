package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import mcp.mobius.waila.addons.core.HUDHandlerEntities;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerICtntPrimed implements IEntityProvider {

    private final Field fuse;
    private final ItemStack renderStack;

    public HUDHandlerICtntPrimed(Field fuse, ItemStack renderStack) {
        this.fuse = fuse;
        this.renderStack = renderStack;
    }

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        return renderStack;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        currenttip.replaceFirstTagEntry(WHITE + DisplayUtil.itemDisplayNameShort(renderStack),
                HUDHandlerEntities.ENTITY_NAME_TAG);
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.tnt")) {
            String fuseSeconds = String.format("%.2f", accessor.getNBTInteger("Fuse") / 20f);
            currenttip.add(I18n.translate("hud.msg.fuse") + ": " +
                           I18n.translate("hud.msg.seconds_format", fuseSeconds));
        }
    }

    @Override
    public void modifyTail(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            tag.setInteger("Fuse", fuse.getInt(ent));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, ent.getClass(), null);
        }
    }

}

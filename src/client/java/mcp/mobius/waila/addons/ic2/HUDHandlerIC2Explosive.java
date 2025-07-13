package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.EntityIC2Explosive;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.EntityIC2Explosive_fuse;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.EntityIC2Explosive_renderBlock;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDHandlerIC2Explosive implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerIC2Explosive();

    private HUDHandlerIC2Explosive() {
    }

    @Override
    public Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public ItemStack getDisplayItem(IEntityAccessor accessor, IPluginConfig config) {
        Entity entity = accessor.getEntity();
        if (EntityIC2Explosive.isInstance(entity)) {
            try {
                Block renderBlock = (Block) EntityIC2Explosive_renderBlock.get(entity);
                if (renderBlock != null)
                    return new ItemStack(renderBlock);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, entity.getClass(), null);
            }
        }

        return null;
    }

    @Override
    public void modifyHead(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (EntityIC2Explosive.isInstance(entity)) {
            try {
                Block renderBlock = (Block) EntityIC2Explosive_renderBlock.get(entity);
                if (renderBlock == null) return;

                currenttip.set(0, WHITE + DisplayUtil.itemDisplayNameShort(new ItemStack(renderBlock)));
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, entity.getClass(), currenttip);
            }
        }
    }

    @Override
    public void modifyBody(Entity entity, ITaggedList<String, String> currenttip,
                           IEntityAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.tnt"))
            if (EntityIC2Explosive.isInstance(entity)) {
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
            int fuse = 0;

            if (EntityIC2Explosive.isInstance(ent)) {
                fuse = EntityIC2Explosive_fuse.getInt(ent);
            }

            tag.setInteger("Fuse", fuse);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, ent.getClass(), null);
        }
    }

}

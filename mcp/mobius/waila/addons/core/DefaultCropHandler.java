package mcp.mobius.waila.addons.core;

import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.api.ICropHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.item.ItemStack;

public class DefaultCropHandler implements ICropHandler {

    protected final int minStage;
    protected final int maxStage;

    public DefaultCropHandler(int maxStage) {
        this(0, maxStage);
    }

    public DefaultCropHandler(int minStage, int maxStage) {
        this.minStage = minStage;
        this.maxStage = maxStage;
    }

    public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return accessor.getMetadata() - minStage;
    }

    public int getMaxStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return maxStage - minStage;
    }

    @Override
    public List<String> getGrowthString(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        float growthValue = (getCurrentStage(itemStack, accessor, config) /
                             (float) getMaxStage(itemStack, accessor, config)) * 100;

        List<String> ret = new ArrayList<String>();
        if (growthValue < 100.0)
            ret.add(String.format("%s: %.0f %%", I18n.translate(GROWTH_STATE), growthValue));
        else
            ret.add(I18n.translate(GROWTH_STATE) + ": " + I18n.translate(MATURE));
        return ret;
    }

}

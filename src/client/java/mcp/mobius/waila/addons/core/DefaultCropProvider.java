package mcp.mobius.waila.addons.core;

import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.api.ICropProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ItemStack;

public class DefaultCropProvider implements ICropProvider {

    protected final int minStage;
    protected final int maxStage;
    protected final Integer ripeStage;

    public DefaultCropProvider(int maxStage) {
        this(maxStage, null);
    }

    public DefaultCropProvider(int maxStage, Integer ripeStage) {
        this(0, maxStage, ripeStage);
    }

    public DefaultCropProvider(int minStage, int maxStage, Integer ripeStage) {
        this.minStage = minStage;
        this.maxStage = maxStage;
        this.ripeStage = ripeStage;
    }

    public boolean shouldHandle(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return true;
    }

    public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return accessor.getMetadata() - minStage;
    }

    public int getMaxStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return maxStage - minStage;
    }

    public Integer getRipeStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return ripeStage == null ? null : (ripeStage - minStage);
    }

    public String getGrowthString(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config,
                                  float growthValue) {
        return String.format("%s: %.0f %%", I18n.translate(GROWTH_STATE), growthValue);
    }

    public String getMatureString(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return I18n.translate(GROWTH_STATE) + ": " + I18n.translate(MATURE);
    }

    public String getRipeString(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        return I18n.translate(GROWTH_STATE) + ": " + I18n.translate(RIPE);
    }

    @Override
    public List<String> getGrowthDetails(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        List<String> ret = new ArrayList<String>();
        if (!shouldHandle(itemStack, accessor, config)) return ret;

        int currentStage = getCurrentStage(itemStack, accessor, config);
        Integer ripeStage = getRipeStage(itemStack, accessor, config);

        if (ripeStage != null && currentStage >= ripeStage) {
            ret.add(getRipeString(itemStack, accessor, config));
        } else {
            float growthValue = (currentStage / (float) getMaxStage(itemStack, accessor, config)) * 100;
            ret.add(growthValue < 100.0
                    ? getGrowthString(itemStack, accessor, config, growthValue)
                    : getMatureString(itemStack, accessor, config));
        }
        return ret;
    }

}

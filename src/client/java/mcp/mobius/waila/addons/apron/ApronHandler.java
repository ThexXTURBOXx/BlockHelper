package mcp.mobius.waila.addons.apron;

import io.github.betterthanupdates.apron.stapi.blockhelper.TooltipRegistrar;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public class ApronHandler implements TooltipRegistrar.TooltipBuilder {

    @Override
    public void handleStack(ItemStack stack, TooltipRegistrar.LineAdder lineAdder) {
        if (ModLoader.getMinecraftInstance().thePlayer.inventory.getItemStack() != null) return;
        if (!PluginConfig.instance().get("nei.modtooltips")) return;

        String mod = ModIdentification.identifyMod(stack);
        if (mod != null)
            lineAdder.addLine("\u00a79\u00a7o" + mod);
    }

    public static void register() {
        TooltipRegistrar.registerTooltipBuilder(new ApronHandler());
        WailaRegistrar.instance().addConfig("Apron", "nei.modtooltips");
    }

}

package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int background;
    public static int gradient1;
    public static int gradient2;
    public static boolean fixerNotify;
    public static boolean showItemId;
    public static boolean showHarvest;
    public static boolean showBreakProg;
    public static boolean showMod;
    public static boolean showBlock;
    public static KeyBinding showHide;

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        mod_BlockHelper.isClient = true;
        ModLoader.setInGameHook(instance, true, false);
        ModIdentifier.load();
        size = cfg.get("General", "Size", 1D, "Size factor for the tooltip").getDouble(1);
        background = parseUnsignedInt(cfg.get("General", "BackgroundColor",
                "cc100010", "Background Color Hex Code").getString(), 16);
        gradient1 = parseUnsignedInt(cfg.get("General", "BorderColor1",
                "cc5000ff", "Border Color Hex Code 1").getString(), 16);
        gradient2 = parseUnsignedInt(cfg.get("General", "BorderColor2",
                "cc28007f", "Border Color Hex Code 2").getString(), 16);
        fixerNotify = cfg.get("General", "NotifyAboutFixers", true,
                "Notifies about the nice Fixer mods :)").getBoolean(true);
        showItemId = cfg.get("General", "ShowItemID", true,
                "Shows the Item ID in the HUD").getBoolean(true);
        showHarvest = cfg.get("General", "ShowHarvestability", true,
                "Shows the current block harvestability in the HUD").getBoolean(true);
        showBreakProg = cfg.get("General", "ShowBreakProgression", true,
                "Shows the current block break progression in the HUD").getBoolean(true);
        showMod = cfg.get("General", "ShowMod", true,
                "Shows the mod of the current block in the HUD").getBoolean(true);
        showBlock = cfg.get("General", "ShowBlockInHud", true,
                "Renders the current block in the HUD").getBoolean(true);
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("blockhelper.key_show_hide", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

}

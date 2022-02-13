package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
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
        ModLoader.setInGameHook(instance, true, false);
        ModIdentifier.load();
        size = Double.parseDouble(cfg.getOrCreateProperty("Size", "General", "1.0").value);
        background = parseUnsignedInt(cfg.getOrCreateProperty("BackgroundColor", "General", "cc100010").value, 16);
        gradient1 = parseUnsignedInt(cfg.getOrCreateProperty("BorderColor1", "General", "cc5000ff").value, 16);
        gradient2 = parseUnsignedInt(cfg.getOrCreateProperty("BorderColor2", "General", "cc28007f").value, 16);
        fixerNotify = parseBooleanTrueDefault(cfg.getOrCreateProperty("NotifyAboutFixers", "General", "true").value);
        showItemId = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowItemID", "General", "true").value);
        showHarvest = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowHarvestability", "General", "true").value);
        showBreakProg = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowBreakProgression", "General", "true").value);
        showMod = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowMod", "General", "true").value);
        showBlock = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowBlockInHud", "General", "true").value);
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("blockhelper.key_show_hide", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

}

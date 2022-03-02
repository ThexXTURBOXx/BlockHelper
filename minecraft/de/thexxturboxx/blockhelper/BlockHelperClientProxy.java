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
    public static boolean shouldHideFromDebug;
    public static KeyBinding showHide;

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        ModLoader.setInGameHook(instance, true, false);
        ModIdentifier.load();
        size = Double.parseDouble(mod_BlockHelper.sizeStr);
        background = parseUnsignedInt(mod_BlockHelper.backgroundStr, 16);
        gradient1 = parseUnsignedInt(mod_BlockHelper.gradient1Str, 16);
        gradient2 = parseUnsignedInt(mod_BlockHelper.gradient2Str, 16);
        fixerNotify = parseBooleanTrueDefault(mod_BlockHelper.fixerNotifyStr);
        showItemId = parseBooleanTrueDefault(mod_BlockHelper.showItemIdStr);
        showHarvest = parseBooleanTrueDefault(mod_BlockHelper.showHarvestStr);
        showBreakProg = parseBooleanTrueDefault(mod_BlockHelper.showBreakProgStr);
        showMod = parseBooleanTrueDefault(mod_BlockHelper.showModStr);
        showBlock = parseBooleanTrueDefault(mod_BlockHelper.renderBlockStr);
        shouldHideFromDebug = parseBooleanTrueDefault(mod_BlockHelper.shouldHideFromDebugStr);
        sizeInv = 1 / size;
        showHide = new KeyBinding("blockhelper.key_show_hide", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

}

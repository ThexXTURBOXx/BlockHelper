package mcp.mobius.waila.utils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.src.mod_BlockHelper;

import static mcp.mobius.waila.api.SpecialChars.MCStyle;
import static mcp.mobius.waila.api.SpecialChars.MISFORMATTED_PAR;

public final class ModIdentification {

    public static Map<String, String> modSource = new HashMap<String, String>();
    public static Map<Integer, String> itemMap = new HashMap<Integer, String>();
    private static boolean useOldIdentifier = false;

    private ModIdentification() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        try {
            NBTTagList itemDataList = new NBTTagList();
            GameData.writeItemData(itemDataList);

            for (int i = 0; i < itemDataList.tagCount(); i++) {
                ItemData itemData = new ItemData((NBTTagCompound) itemDataList.tagAt(i));
                itemMap.put(itemData.getItemId(), itemData.getModId());
            }

            for (ModContainer mod : Loader.instance().getModList())
                modSource.put(mod.getSource().getName(), formatModName(mod.getName()));

            modSource.put("minecraft.jar", "Minecraft");
            modSource.put("Forge", "Minecraft");
            modSource.put("Forge Mod Loader", "Minecraft");
            modSource.put("Minecraft Forge", "Minecraft");
            modSource.put("Minecraft Coder Pack", "Minecraft");
            modSource.put("Mod Coder Pack", "Minecraft");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "Cannot load ModIdentifier. Either you are running an old " +
                                                   "version of Forge or MC 1.4.4 or something else happened. Loading " +
                                                   "fallback mod identifier...", t);
            useOldIdentifier = true;

            ModIdentificationOld.init();
        }
    }

    public static String nameFromObject(Object obj) {
        if (useOldIdentifier)
            return ModIdentificationOld.identifyMod(obj);

        String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
            objPath = URLDecoder.decode(objPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "nameFromObject", e);
        }

        String modName = null;
        for (String s : modSource.keySet())
            if (objPath.contains(s)) {
                modName = modSource.get(s);
                break;
            }

        if (modName == null)
            modName = "<" + I18n.translate("hud.msg.unknown") + ">";
        else if (modName.equals("Minecraft Coder Pack"))
            modName = "Minecraft";

        return modName;
    }

    public static String nameFromStack(ItemStack stack) {
        if (useOldIdentifier)
            return ModIdentificationOld.identifyMod(stack);

        try {
            String modID = itemMap.get(stack.itemID);
            ModContainer mod = modID == null ? null : ModIdentification.findModContainer(modID);
            return mod == null ? "Minecraft" : formatModName(mod.getName());
        } catch (NullPointerException e) {
            mod_BlockHelper.LOG.log(Level.FINEST, "nameFromStack", e);
            return "";
        }
    }

    public static ModContainer findModContainer(String modID) {
        for (ModContainer mc : Loader.instance().getModList())
            if (mc != null && modID.equals(mc.getModId()))
                return mc;

        return null;
    }

    private static String formatModName(String name) {
        return name == null ? "Minecraft" :
                name.replaceFirst("^mod_", "")
                        .replaceAll(MISFORMATTED_PAR + ".", "")
                        .replaceAll(MCStyle + ".", "");
    }

}

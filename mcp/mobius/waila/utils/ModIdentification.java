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
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class ModIdentification {

    public static Map<String, String> modSource_Name = new HashMap<String, String>();
    public static Map<String, String> modSource_ID = new HashMap<String, String>();
    public static Map<Integer, String> itemMap = new HashMap<Integer, String>();

    private ModIdentification() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        NBTTagList itemDataList = new NBTTagList();
        GameData.writeItemData(itemDataList);

        for (int i = 0; i < itemDataList.tagCount(); i++) {
            ItemData itemData = new ItemData((NBTTagCompound) itemDataList.tagAt(i));
            itemMap.put(itemData.getItemId(), itemData.getModId());
        }

        for (ModContainer mod : Loader.instance().getModList()) {
            modSource_Name.put(mod.getSource().getName(), formatModName(mod.getName()));
            modSource_ID.put(mod.getSource().getName(), mod.getModId());
        }

        modSource_Name.put("minecraft.jar", "Minecraft");
        modSource_Name.put("Forge", "Minecraft");
        modSource_Name.put("Forge Mod Loader", "Minecraft");
        modSource_Name.put("Minecraft Forge", "Minecraft");
        modSource_Name.put("Minecraft Coder Pack", "Minecraft");
        modSource_Name.put("Mod Coder Pack", "Minecraft");
        modSource_ID.put("minecraft.jar", "Minecraft");
        modSource_ID.put("Forge", "Minecraft");
        modSource_ID.put("Forge Mod Loader", "Minecraft");
        modSource_ID.put("Minecraft Forge", "Minecraft");
        modSource_ID.put("Minecraft Coder Pack", "Minecraft");
        modSource_ID.put("Mod Coder Pack", "Minecraft");
    }

    public static String nameFromObject(Object obj) {
        String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
            objPath = URLDecoder.decode(objPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "nameFromObject", e);
        }

        String modName = "<Unknown>";
        for (String s : modSource_Name.keySet())
            if (objPath.contains(s)) {
                modName = modSource_Name.get(s);
                break;
            }

        if (modName.equals("Minecraft Coder Pack"))
            modName = "Minecraft";

        return modName;
    }

    public static String idFromObject(Object obj) {
        String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
            objPath = URLDecoder.decode(objPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "idFromObject", e);
        }

        String modName = "<Unknown>";
        for (String s : modSource_ID.keySet())
            if (objPath.contains(s)) {
                modName = modSource_ID.get(s);
                break;
            }

        if (modName.equals("Minecraft Coder Pack"))
            modName = "Minecraft";

        return modName;
    }

    public static String nameFromStack(ItemStack stack) {
        try {
            String modID = itemMap.get(stack.itemID);
            ModContainer mod = ModIdentification.findModContainer(modID);
            return mod == null ? "Minecraft" : formatModName(mod.getName());
        } catch (NullPointerException e) {
            mod_BlockHelper.LOG.log(Level.FINEST, "nameFromStack", e);
            return "";
        }
    }

    public static ModContainer findModContainer(String modID) {
        for (ModContainer mc : Loader.instance().getModList())
            if (modID.equals(mc.getModId()))
                return mc;

        return null;
    }

    private static String formatModName(String name) {
        return name == null ? "Minecraft" :
                name.replaceFirst("^mod_", "")
                        .replaceAll("\u00C2\u00A7.", "")
                        .replaceAll("\u00A7.", "");
    }

}

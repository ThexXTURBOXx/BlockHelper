package mcp.mobius.waila.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;

import static mcp.mobius.waila.api.SpecialChars.MCStyle;

public final class ModIdentification {

    public static final String MINECRAFT = "Minecraft";
    private static final Map<Object, String> classToMod = new HashMap<Object, String>();
    private static Set<ModInfo> modInfos;

    private ModIdentification() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        modInfos = new HashSet<ModInfo>();
        String minecraftUri = new File("bin/minecraft.jar").getAbsoluteFile().toString();
        try {
            minecraftUri = formatURI(Minecraft.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t);
        }
        modInfos.add(new ModInfo(minecraftUri, MINECRAFT));

        try {
            baseModLoop:
            for (BaseMod mod : (List<BaseMod>) ModLoader.getLoadedMods()) {
                try {
                    String uri = formatURI(mod.getClass().getProtectionDomain().getCodeSource()
                            .getLocation().toURI());
                    for (ModInfo info : modInfos)
                        if (info.uri.equals(uri))
                            continue baseModLoop;
                    modInfos.add(new ModInfo(uri, formatModName(mod.getClass().getSimpleName())));
                } catch (Throwable t) {
                    mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t);
                }
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t);
        }
    }

    public static String identifyMod(Object object) {
        if (object instanceof ItemStack)
            object = ((ItemStack) object).getItem();
        if (object instanceof ItemBlock)
            object = Block.blocksList[((ItemBlock) object).func_35435_b()];
        if (object == null)
            return "";

        Class<?> clazz = object.getClass();
        String mod = classToMod.get(clazz);
        if (mod != null)
            return mod;
        mod = lookupMod(clazz);
        if (mod != null) {
            classToMod.put(clazz, mod);
            return mod;
        } else {
            classToMod.put(clazz, MINECRAFT);
        }
        return MINECRAFT;
    }

    private static String lookupMod(Class<?> clazz) {
        try {
            String modFile = formatURI(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            for (ModInfo modInfo : modInfos)
                if (modFile.contains(modInfo.uri))
                    return modInfo.name;
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static String formatModName(String name) {
        return name == null ? "Minecraft" :
                name.replaceFirst("^mod_", "")
                        .replaceAll("\u00C2\u00A7.", "")
                        .replaceAll(MCStyle + ".", "");
    }

    private static String formatURI(URI uri) {
        String uriStr = uri.toString();

        try {
            JarURLConnection connection = (JarURLConnection) uri.toURL().openConnection();
            uriStr = connection.getJarFileURL().toURI().toString();
        } catch (Throwable ignored) {
        }

        try {
            uriStr = URLDecoder.decode(uriStr, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }

        return uriStr;
    }

    private static class ModInfo {
        private final String uri;
        private final String name;

        private ModInfo(String uri, String name) {
            this.uri = uri;
            this.name = name;
        }
    }

}

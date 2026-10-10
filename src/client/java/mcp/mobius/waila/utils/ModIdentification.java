package mcp.mobius.waila.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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
import static mcp.mobius.waila.api.SpecialChars.MISFORMATTED_PAR;

public final class ModIdentification {

    public static final String MINECRAFT = "Minecraft";
    public static final String MINECRAFT_FABRIC_NAMESPACE = "minecraft";
    private static final Map<Object, String> classToMod = new HashMap<Object, String>();
    private static Set<ModInfo> modInfos;
    private static Field blockIdField;

    private ModIdentification() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        try {
            blockIdField = AccessHelper.getDeclaredField(ItemBlock.class, "a", "field_330_a", "field_2216", "blockID");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

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
            List<BaseMod> modList = (List<BaseMod>) AccessHelper.getDeclaredField(ModLoader.class, "modList").get(null);

            baseModLoop:
            for (BaseMod mod : modList) {
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
        try {
            if (object instanceof ItemBlock)
                object = Block.blocksList[blockIdField.getInt(object)];
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, object.getClass(), null);
        }
        if (object == null)
            return "";

        Class<?> clazz = object.getClass();
        String mod = classToMod.get(clazz);
        if (mod != null)
            return mod;
        mod = lookupMod(clazz, object);
        if (mod != null) {
            classToMod.put(clazz, mod);
            return mod;
        } else {
            classToMod.put(clazz, MINECRAFT);
        }
        return MINECRAFT;
    }

    private static String lookupMod(Class<?> clazz, Object object) {
        try {
            String modFile = formatURI(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            for (ModInfo modInfo : modInfos)
                if (modInfo.uri != null && modFile.contains(modInfo.uri))
                    return modInfo.name;
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static String formatModName(String name) {
        return name == null ? "Minecraft" :
                name.replaceFirst("^mod_", "")
                        .replaceAll(MISFORMATTED_PAR + ".", "")
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
        private final String namespace;
        private final String name;

        private ModInfo(String uri, String name) {
            this.uri = uri;
            this.namespace = null;
            this.name = name;
        }

        private ModInfo(String namespace, String name, boolean fabric) {
            this.uri = null;
            this.namespace = namespace;
            this.name = name;
        }
    }

}

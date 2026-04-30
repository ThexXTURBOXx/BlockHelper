package mcp.mobius.waila.utils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
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
            containerLoop:
            for (ModContainer container : Loader.getModList()) {
                try {
                    File source = container.getSource();
                    if (source.isFile()) {
                        String uri = formatURI(source.toURI());
                        for (ModInfo info : modInfos) {
                            if (info.uri.equals(uri)) {
                                continue containerLoop;
                            }
                        }
                        modInfos.add(new ModInfo(uri, getModName(container)));
                    }
                } catch (Throwable t) {
                    mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t);
                }
            }
        } catch (Throwable t) {
            try {
                baseModLoop:
                for (BaseMod mod : ModLoader.getLoadedMods()) {
                    try {
                        String uri = formatURI(mod.getClass().getProtectionDomain().getCodeSource()
                                .getLocation().toURI());
                        for (ModInfo info : modInfos)
                            if (info.uri.equals(uri))
                                continue baseModLoop;
                        modInfos.add(new ModInfo(uri, formatModName(mod.getName())));
                    } catch (Throwable t1) {
                        mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t1);
                    }
                }
            } catch (Throwable t1) {
                mod_BlockHelper.LOG.log(Level.WARNING, "ModIdentification#init", t1);
            }
        }
    }

    public static String identifyMod(Object object) {
        if (object instanceof ItemStack)
            object = ((ItemStack) object).getItem();
        if (object instanceof ItemBlock)
            object = Block.blocksList[((ItemBlock) object).getBlockID()];
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

    private static String getModName(ModContainer container) {
        if (container != null) {
            ModMetadata metadata = container.getMetadata();
            if (metadata != null && metadata.name != null)
                return formatModName(metadata.name);
            return formatModName(container.getName());
        }
        return MINECRAFT;
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
        private final String name;

        private ModInfo(String uri, String name) {
            this.uri = uri;
            this.name = name;
        }
    }

}

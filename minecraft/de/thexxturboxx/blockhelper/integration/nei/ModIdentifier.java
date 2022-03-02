package de.thexxturboxx.blockhelper.integration.nei;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;

public final class ModIdentifier {

    public static final String MINECRAFT = "Minecraft";
    private static final Map<Object, String> objectToMod = new HashMap<Object, String>();
    private static Set<ModInfo> modInfos;

    private ModIdentifier() {
        throw new UnsupportedOperationException();
    }

    public static void load() {
        try {
            Class.forName("codechicken.nei.API");
            Class.forName("codechicken.nei.IHandleTooltip");
            NEIIntegration.register();
            mod_BlockHelper.LOGGER.info("NotEnoughItems loaded, Item Tooltips enabled.");
        } catch (Throwable t) {
            mod_BlockHelper.LOGGER.warning("NotEnoughItems not loaded, Item Tooltips disabled.");
        }
    }

    public static void firstTick() {
        modInfos = new HashSet<ModInfo>();
        String minecraftUri = new File("bin/minecraft.jar").getAbsoluteFile().toString();
        try {
            minecraftUri = formatURI(Minecraft.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
        } catch (Throwable t) {
            t.printStackTrace();
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
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            try {
                baseModLoop:
                for (BaseMod mod : ModLoader.getLoadedMods()) {
                    try {
                        String uri = formatURI(mod.getClass().getProtectionDomain().getCodeSource()
                                .getLocation().toURI());
                        for (ModInfo info : modInfos) {
                            if (info.uri.equals(uri)) {
                                continue baseModLoop;
                            }
                        }
                        modInfos.add(new ModInfo(uri, formatName(mod.getName())));
                    } catch (Throwable t1) {
                        t1.printStackTrace();
                    }
                }
            } catch (Throwable t1) {
                t1.printStackTrace();
            }
        }
    }

    public static String identifyMod(Object object) {
        if (object instanceof ItemStack) {
            object = ((ItemStack) object).getItem();
        }
        if (object instanceof ItemBlock) {
            object = Block.blocksList[((ItemBlock) object).getBlockID()];
        }
        if (object == null) {
            return null;
        }

        String mod = objectToMod.get(object);
        if (mod != null) {
            return mod;
        }
        mod = lookupMod(object);
        if (mod != null) {
            objectToMod.put(object, mod);
            return mod;
        } else {
            objectToMod.put(object, MINECRAFT);
        }
        return MINECRAFT;
    }

    private static String lookupMod(Object object) {
        String mod = BlockHelperModSupport.getMod(object);
        if (mod != null) {
            return mod;
        }

        try {
            String modFile = formatURI(object.getClass().getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
            for (ModInfo modInfo : modInfos) {
                if (modFile.contains(modInfo.uri)) {
                    return modInfo.name;
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static String getModName(ModContainer container) {
        if (container != null) {
            ModMetadata metadata = container.getMetadata();
            if (metadata != null && metadata.name != null) {
                return formatName(metadata.name);
            }
            return formatName(container.getName());
        }
        return MINECRAFT;
    }

    private static String formatName(String name) {
        return name.replaceFirst("^mod_", "").replaceAll("\u00a7.", "");
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

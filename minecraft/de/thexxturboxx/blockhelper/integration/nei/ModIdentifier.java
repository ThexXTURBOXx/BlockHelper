package de.thexxturboxx.blockhelper.integration.nei;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.mod_BlockHelper;

public final class ModIdentifier {

    public static final String MINECRAFT = "Minecraft";
    private static final Map<Object, String> objectToMod = new HashMap<Object, String>();
    private static List<ModInfo> modInfos;

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
        try {
            modInfos = new ArrayList<ModInfo>();
            for (ModContainer container : Loader.getModList()) {
                if (container.getName().equals("mod_CodeChickenCore")) {
                    modInfos.add(new ModInfo(formatURI(container.getSource().toURI()), MINECRAFT));
                } else {
                    modInfos.add(new ModInfo(formatURI(container.getSource().toURI()), getModName(container)));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
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
        String mod = null;
        try {
            String modFile = formatURI(object.getClass().getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
            for (ModInfo modInfo : modInfos) {
                if (modFile.contains(modInfo.uri)) {
                    mod = modInfo.name;
                    break;
                }
            }
        } catch (Throwable ignored) {
        }
        return mod;
    }

    private static String getModName(ModContainer container) {
        if (container != null) {
            ModMetadata metadata = container.getMetadata();
            if (metadata != null && metadata.name != null) {
                return metadata.name.replaceAll("\u00a7.", "");
            }
            return container.getName().replaceFirst("^mod_", "").replaceAll("\u00a7.", "");
        }
        return MINECRAFT;
    }

    private static String formatURI(URI uri) {
        String uriStr = uri.toString();
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

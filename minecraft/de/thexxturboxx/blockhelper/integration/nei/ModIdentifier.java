package de.thexxturboxx.blockhelper.integration.nei;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    @SuppressWarnings("unchecked")
    public static void firstTick() {
        modInfos = new HashSet<ModInfo>();
        String minecraftUri = new File("./bin/minecraft.jar").getAbsoluteFile().toString();
        try {
            minecraftUri = formatURI(Minecraft.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            for (BaseMod mod : (List<BaseMod>) ModLoader.getLoadedMods()) {
                try {
                    String uri = formatURI(mod.getClass().getProtectionDomain().getCodeSource()
                            .getLocation().toURI());
                    if (uri.contains(minecraftUri)) {
                        modInfos.add(new ModInfo(uri, MINECRAFT));
                    } else {
                        modInfos.add(new ModInfo(uri, formatName(mod.getName())));
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
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

    private static String formatName(String name) {
        return name.replaceFirst("^mod_", "").replaceAll("\u00a7.", "");
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

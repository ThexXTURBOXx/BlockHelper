package de.thexxturboxx.blockhelper.integration.nei;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public final class ModIdentifier {

    public static final String MINECRAFT = "Minecraft";
    public static final String MINECRAFT_FABRIC_NAMESPACE = "minecraft";
    private static final Map<Object, String> objectToMod = new HashMap<Object, String>();
    private static Set<ModInfo> modInfos;

    private static Field blockIdField;

    private ModIdentifier() {
        throw new UnsupportedOperationException();
    }

    public static void load() {
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
                        modInfos.add(new ModInfo(uri, formatName(mod.getClass().getSimpleName())));
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            modInfos.add(new ModInfo(MINECRAFT_FABRIC_NAMESPACE, MINECRAFT, true));
            FabricLoader instance =
                    (FabricLoader) BlockHelperInfoProvider.getMethod(FabricLoader.class, "getInstance").invoke(null);
            for (ModContainer container : instance.getAllMods()) {
                ModMetadata meta = container.getMetadata();
                modInfos.add(new ModInfo(meta.getId(), meta.getName(), true));
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
            try {
                if (blockIdField == null) {
                    blockIdField = ItemBlock.class.getDeclaredField("a");
                    blockIdField.setAccessible(true);
                }
                object = Block.blocksList[(Integer) blockIdField.get(object)];
            } catch (Throwable ignored) {
            }
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
                if (modInfo.uri != null && modFile.contains(modInfo.uri)) {
                    return modInfo.name;
                }
            }
        } catch (Throwable ignored) {
        }

        try {
            Identifier id = null;
            if (object instanceof Block) {
                id = BlockRegistry.INSTANCE.getIdentifier((Block) object);
            } else if (object instanceof Item) {
                id = ItemRegistry.INSTANCE.getIdentifier((Item) object);
            }
            if (id == null) {
                return MINECRAFT;
            }
            String idStr = id.modID.getMetadata().getId();
            for (ModInfo modInfo : modInfos) {
                if (modInfo.namespace != null && modInfo.namespace.equals(idStr)) {
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

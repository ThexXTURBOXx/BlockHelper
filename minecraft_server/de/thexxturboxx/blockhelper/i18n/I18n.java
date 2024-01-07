package de.thexxturboxx.blockhelper.i18n;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.mod_BlockHelper;

public final class I18n {

    private static final String PREFIX = "blockhelper.";

    private static final String[] LANGUAGES = {"en_US", "de_DE"};

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        String currentLang = BlockHelperInfoProvider.getDeclaredField(
                StringTranslate.class, StringTranslate.getInstance(), "d");
        String langToReload = LANGUAGES[0];
        for (String lang : LANGUAGES) {
            loadLanguage(lang);
            if (lang.equals(currentLang)) langToReload = lang;
        }
        // Load translations again in current language in order to fix
        // stupid bug in Forge, reported through Discord...
        loadLanguage(langToReload);
    }

    public static void loadLanguage(String lang) {
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = I18n.class.getResourceAsStream("/de/thexxturboxx/blockhelper/i18n/" + lang + ".properties");
            if (stream == null) {
                throw new IOException("Couldn't load language file.");
            }

            reader = new InputStreamReader(stream, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            for (String key : props.stringPropertyNames()) {
                addLocalization(key, props.getProperty(key));
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOGGER.severe("Error loading language " + lang + ".");
            t.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    private static Properties translateTable;

    static {
        try {
            translateTable = (Properties) ModLoader.getPrivateValue(
                    StringTranslate.class, StringTranslate.getInstance(), 1);
        } catch (SecurityException e) {
            ModLoader.getLogger().throwing("I18n", "<clinit>", e);
            ModLoader.throwException("Exception occurred in BlockHelper", e);
        } catch (NoSuchFieldException e) {
            ModLoader.getLogger().throwing("I18n", "<clinit>", e);
            ModLoader.throwException("Exception occurred in BlockHelper", e);
        }
    }

    private static void addLocalization(String key, String value) {
        if (translateTable != null) {
            translateTable.put(key, value);
        }
    }

    public static String format(boolean b) {
        return format(null, b);
    }

    public static String format(StringTranslate translator, boolean b) {
        return format(translator, b ? "true" : "false");
    }

    public static String format(String key, Object... args) {
        return format(null, key, args);
    }

    public static String format(StringTranslate translator, String key, Object... args) {
        if (translator == null) return StatCollector.translateToLocalFormatted(PREFIX + key, args);
        return translator.translateKeyFormat(PREFIX + key, args);
    }

}

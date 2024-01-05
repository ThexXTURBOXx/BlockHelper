package de.thexxturboxx.blockhelper.i18n;

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
        String currentLang = StringTranslate.getInstance().getCurrentLanguage();
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
                ModLoader.addLocalization(key, lang, props.getProperty(key));
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

    public static String format(String key, Object... args) {
        return StatCollector.translateToLocalFormatted(PREFIX + key, args);
    }

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }

}

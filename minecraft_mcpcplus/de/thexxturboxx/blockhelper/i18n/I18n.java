package de.thexxturboxx.blockhelper.i18n;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.minecraft.server.LocaleLanguage;
import net.minecraft.server.mod_BlockHelper;

public final class I18n {

    private static final String PREFIX = "blockhelper.";

    private static final String[] LANGUAGES = {"en_US", "de_DE"};

    private static final Map<String, Properties> TRANSLATIONS = new HashMap<String, Properties>();

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        String currentLang = BlockHelperInfoProvider.getDeclaredField(LocaleLanguage.class, LocaleLanguage.a(), "d");
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
            TRANSLATIONS.put(lang, props);
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

    private static Field currentLanguage;

    public static String format(String key, Object... args) {
        try {
            if (currentLanguage == null) {
                currentLanguage = LocaleLanguage.class.getDeclaredField("d");
                currentLanguage.setAccessible(true);
            }
            String language = (String) currentLanguage.get(LocaleLanguage.a());
            language = language == null || TRANSLATIONS.get(language) == null ? LANGUAGES[0] : language;
            return String.format(TRANSLATIONS.get(language).getProperty(PREFIX + key), args);
        } catch (Throwable ignored) {
        }
        return key;
    }

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }

}

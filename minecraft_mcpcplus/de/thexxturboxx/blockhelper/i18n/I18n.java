package de.thexxturboxx.blockhelper.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.minecraft.server.mod_BlockHelper;

public final class I18n {

    private static final String PREFIX = "blockhelper.";

    private static final Properties TRANSLATIONS = new Properties();

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        loadLanguage("en_US");
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
            TRANSLATIONS.load(reader);
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
        try {
            return String.format(TRANSLATIONS.getProperty(PREFIX + key), args);
        } catch (Throwable ignored) {
        }
        return key;
    }

}

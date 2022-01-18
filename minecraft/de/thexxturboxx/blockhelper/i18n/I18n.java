package de.thexxturboxx.blockhelper.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;

public final class I18n {

    private static final String PREFIX = "blockhelper.";

    private static final Properties TRANSLATIONS = new Properties();

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = I18n.class.getResourceAsStream("/de/thexxturboxx/blockhelper/i18n/en_US.properties");
            if (stream == null) {
                throw new IOException("Couldn't load language file.");
            }

            reader = new InputStreamReader(stream, "UTF-8");
            TRANSLATIONS.load(reader);
            for (String key : TRANSLATIONS.stringPropertyNames()) {
                ModLoader.AddLocalization(key, TRANSLATIONS.getProperty(key));
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOGGER.severe("Error loading language files.");
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

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }

}

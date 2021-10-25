package de.thexxturboxx.blockhelper.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.minecraft.src.ModLoader;
import de.thexxturboxx.blockhelper.mod_BlockHelper;
import net.minecraft.util.StatCollector;

public final class I18n {

    private static final String PREFIX = "blockhelper.";

    private static final String[] LANGUAGES = {"en_US", "de_DE"};

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        for (String lang : LANGUAGES) {
            loadLanguage(lang);
        }
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

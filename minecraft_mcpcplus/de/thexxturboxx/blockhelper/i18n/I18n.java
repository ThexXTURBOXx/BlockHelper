package de.thexxturboxx.blockhelper.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import net.minecraft.server.ModLoader;
import net.minecraft.server.StatisticCollector;
import net.minecraft.server.StatisticStorage;
import net.minecraft.server.mod_BlockHelper;

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
                addLocalization(key, TRANSLATIONS.getProperty(key));
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

    private static Properties translateTable;

    static {
        try {
            translateTable = (Properties) ModLoader.getPrivateValue(
                    StatisticStorage.class, StatisticStorage.a(), 1);
        } catch (SecurityException e) {
            ModLoader.getLogger().throwing("I18n", "<clinit>", e);
            ModLoader.ThrowException("Exception occurred in BlockHelper", e);
        } catch (NoSuchFieldException e) {
            ModLoader.getLogger().throwing("I18n", "<clinit>", e);
            ModLoader.ThrowException("Exception occurred in BlockHelper", e);
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

    public static String format(StatisticStorage translator, boolean b) {
        return format(translator, b ? "true" : "false");
    }

    public static String format(String key, Object... args) {
        return format(null, key, args);
    }

    public static String format(StatisticStorage translator, String key, Object... args) {
        if (translator == null) return StatisticCollector.a(PREFIX + key, args);
        return translator.a(PREFIX + key, args);
    }

}

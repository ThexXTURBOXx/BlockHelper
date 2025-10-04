package mcp.mobius.waila.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

public class I18n {

    public static final I18n INSTANCE = new I18n(null);
    public final String prefix;

    private static final Field translateMapField;

    static {
        try {
            translateMapField = AccessHelper.getDeclaredField(StringTranslate.class, "b", "field_20164_b");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public I18n(String prefix) {
        this.prefix = prefix;
    }

    public static String getKeyDisplayString(int keyCode) {
        return keyCode < 0 ? translate("key.mouseButton", keyCode + 101) : Keyboard.getKeyName(keyCode);
    }

    public static String translate(String s, Object... format) {
        return I18n.INSTANCE.translateL(s, format);
    }

    public String translateL(String s, Object... format) {
        if (this.prefix != null && !s.startsWith(this.prefix + "."))
            s = this.prefix + "." + s;

        String ret = StringTranslate.func_20162_a().func_20163_a(s);
        if (ret == null || ret.isEmpty()) return s;
        if (format.length == 0) return ret;

        try {
            return String.format(ret, format);
        } catch (Throwable t) {
            return ret;
        }
    }

    public I18n addLangDirectory(Class<?> mod) {
        String dir = (this.prefix == null) ? "lang" : ("lang/" + this.prefix);
        return this.addLangDirectory(mod, dir);
    }

    public I18n addLangDirectory(Class<?> mod, String dir) {
        this.addLangDirectory(this.hostFile(mod), dir);
        return this;
    }

    public void addLangFile(InputStream resource) throws IOException {
        Properties translateMap;
        try {
            translateMap = (Properties) translateMapField.get(StringTranslate.func_20162_a());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        Properties prop = new Properties();
        prop.load(reader);
        reader.close();

        for (String key : prop.stringPropertyNames()) {
            if (key == null) continue;
            String value = prop.getProperty(key);
            if (this.prefix != null) {
                key = this.prefix + "." + key;
            }
            translateMap.put(key, value);
        }
    }

    public void addLangDirectory(File host, String dir) {
        if (host.isFile()) {
            this.addLangDirFromJar(host, dir);
        } else {
            File hostdir = new File(host, dir);
            if (!hostdir.exists()) {
                mod_BlockHelper.LOG.warning("Lang directory \"" + dir + "\" not found in " + host.getPath());
            } else if (hostdir.isDirectory()) {
                this.addLangDir(hostdir);
            } else if (hostdir.getName().endsWith(".lang") || hostdir.getName().endsWith(".properties")) {
                this.addLangFile(hostdir);
            } else {
                mod_BlockHelper.LOG.warning("Lang file \"" + hostdir + "\" has wrong file ending.");
            }
        }
    }

    public void addLangDir(File dir) {
        File[] listFiles = dir.listFiles();
        if (listFiles == null) return;
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            File child = listFiles[i];
            if (child.isDirectory()) {
                this.addLangDir(child);
            } else if (child.getName().endsWith(".lang") || child.getName().endsWith(".properties")) {
                this.addLangFile(child);
            }
        }
    }

    public void addLangFile(File child) {
        try {
            FileInputStream fin = new FileInputStream(child);
            this.addLangFile(fin);
            fin.close();
        } catch (IOException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "Error occurred while loading lang file: " + child.getPath(), e);
        }
    }

    public void addLangDirFromJar(File jar, String dir) {
        while (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        try {
            ZipFile zf = new ZipFile(jar);
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith(dir) &&
                    (name.endsWith(".lang") || name.endsWith(".properties"))) {
                    this.addLangFile(zf.getInputStream(entry));
                }
            }
        } catch (IOException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "Error while reading lang zip file: " + jar.getPath(), e);
        }
    }

    public File hostFile(Class<?> clazz) {
        URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        try {
            String p = url.getPath();
            if (url.getProtocol().equals("jar")) {
                p = p.substring(0, url.getPath().lastIndexOf('!'));
            }
            if (p.startsWith("file:")) {
                p = p.substring(5);
            }
            return new File(URLDecoder.decode(p, "UTF-8"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void addLangDirFromHost(Class<?> clazz, String dir) {
        try {
            this.addLangDirFromJar(hostFile(clazz), dir);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "Error occurred while loading lang directory: " + dir, t);
            addDefaultLangFromHost(clazz, dir);
        }
    }

    public void addDefaultLangFromHost(Class<?> clazz, String dir) {
        try {
            InputStream stream = clazz.getResourceAsStream(dir + "/en_US.properties");
            if (stream == null) stream = clazz.getResourceAsStream(dir + "/en_US.lang");
            this.addLangFile(stream);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE, "Critical error occurred while loading default language!", t);
        }
    }

}

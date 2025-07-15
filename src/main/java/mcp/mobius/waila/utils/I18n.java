package mcp.mobius.waila.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.item.ItemDye;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.util.StatCollector;

public class I18n {

    public static final I18n INSTANCE = new I18n(null);
    public final String prefix;

    public I18n(String prefix) {
        this.prefix = prefix;
    }

    public static String color(int dyeColorId) {
        return I18n.INSTANCE.colorL(dyeColorId);
    }

    public static String translate(String s, Object... format) {
        return I18n.INSTANCE.translateL(s, format);
    }

    public String colorL(int dyeColorId) {
        return translateL("hud.msg." + ItemDye.dyeColorNames[dyeColorId]);
    }

    public String translateL(String s, Object... format) {
        if (this.prefix != null && !s.startsWith(this.prefix + "."))
            s = this.prefix + "." + s;

        String ret = StatCollector.translateToLocal(s);
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

    public void addLangFile(InputStream resource, String lang) throws IOException {
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
            ModLoader.addLocalization(key, lang, value);
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
            String lang = child.getName().substring(0, child.getName().lastIndexOf('.'));
            FileInputStream fin = new FileInputStream(child);
            this.addLangFile(fin, lang);
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
                    this.addLangFile(zf.getInputStream(entry), name.substring(name.lastIndexOf('/') + 1,
                            name.lastIndexOf('.')));
                }
            }
            zf.close();
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
            try {
                mod_BlockHelper.LOG.log(Level.WARNING, "Error occurred while loading lang directory: " + dir, t);
                InputStream stream = clazz.getResourceAsStream(dir + "/en_US.properties");
                if (stream == null) stream = clazz.getResourceAsStream(dir + "/en_US.lang");
                this.addLangFile(stream, "en_US");
            } catch (Throwable t1) {
                mod_BlockHelper.LOG.log(Level.SEVERE, "Critical error occurred while loading fallback!", t1);
            }
        }
    }

}

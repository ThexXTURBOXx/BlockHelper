package mcp.mobius.waila.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.src.ModLoader;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

public class LangUtil {

    public static final LangUtil INSTANCE = new LangUtil(null);
    public static final String FALLBACK_LANGUAGE = "en_US";
    public final String prefix;

    public LangUtil(String prefix) {
        this.prefix = prefix;
    }

    public static String translateG(String s, Object... format) {
        return LangUtil.INSTANCE.translate(s, format);
    }

    public static String translateG(StringTranslate translator, String s, Object... format) {
        return LangUtil.INSTANCE.translate(translator, s, format);
    }

    public String translate(String s, Object... format) {
        return translate(StringTranslate.getInstance(), s, format);
    }

    public String translate(StringTranslate translator, String s, Object... format) {
        if (this.prefix != null && !s.startsWith(this.prefix + "."))
            s = this.prefix + "." + s;

        String ret = translator == null
                ? StatCollector.translateToLocal(s)
                : translator.translateKey(s);
        if (ret.isEmpty()) return s;
        return format.length > 0 ? String.format(ret, format) : ret;
    }

    public LangUtil addLangDirectory(Class<?> mod) {
        String dir = (this.prefix == null) ? "lang" : ("lang/" + this.prefix);
        return this.addLangDirectory(mod, dir);
    }

    public LangUtil addLangDirectory(Class<?> mod, String dir) {
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
                System.err.println("Lang directory \"" + dir + "\" not found in " + host.getPath());
            } else if (hostdir.isDirectory()) {
                this.addLangDir(hostdir);
            } else if (hostdir.getName().endsWith(".lang") || hostdir.getName().endsWith(".properties")) {
                this.addLangFile(hostdir);
            } else {
                System.err.println("Lang file \"" + hostdir + "\"does not end in .lang");
            }
        }
    }

    public void addLangDir(File dir) {
        File[] listFiles;
        for (int length = (listFiles = dir.listFiles()).length, i = 0; i < length; ++i) {
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
            System.err.println("Error occurred while loading lang file: " + child.getPath());
            e.printStackTrace();
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
            System.err.println("Error while reading lang zip file: " + jar.getPath());
            e.printStackTrace();
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
                p = p.substring(6);
            }
            return new File(URLDecoder.decode(p, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLangDirFromHost(Class<?> clazz, String dir) {
        this.addLangDirFromJar(hostFile(clazz), dir);
    }

}

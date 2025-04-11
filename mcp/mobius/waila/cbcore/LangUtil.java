package mcp.mobius.waila.cbcore;

import cpw.mods.fml.common.registry.LanguageRegistry;
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

public class LangUtil {

    public static LangUtil instance;
    public String prefix;

    static {
        LangUtil.instance = new LangUtil(null);
    }

    public LangUtil(final String prefix) {
        this.prefix = prefix;
    }

    public static String translateG(final String s, final Object... format) {
        return LangUtil.instance.translate(s, format);
    }

    public String translate(String s, final Object... format) {
        if (this.prefix != null && !s.startsWith(this.prefix + ".")) {
            s = this.prefix + "." + s;
        }
        String ret = LanguageRegistry.instance().getStringLocalization(s);
        if (ret.isEmpty()) {
            ret = LanguageRegistry.instance().getStringLocalization(s, "en_US");
        }
        if (ret.isEmpty()) {
            return s;
        }
        if (format.length > 0) {
            ret = String.format(ret, format);
        }
        return ret;
    }

    public LangUtil addLangDirectory(final Class<?> mod) {
        final String dir = (this.prefix == null) ? "lang" : ("lang/" + this.prefix);
        return this.addLangDirectory(mod, dir);
    }

    public LangUtil addLangDirectory(final Class<?> mod, final String dir) {
        this.addLangDirectory(this.hostFile(mod), dir);
        return this;
    }

    public void addLangFile(final InputStream resource, final String lang) throws IOException {
        final LanguageRegistry reg = LanguageRegistry.instance();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        Properties prop = new Properties();
        prop.load(reader);
        reader.close();
        for (String key : prop.stringPropertyNames()) {
            if (key == null) continue;
            final String value = prop.getProperty(key);
            if (this.prefix != null) {
                key = this.prefix + "." + key;
            }
            reg.addStringLocalization(key, lang, value);
        }
    }

    public void addLangDirectory(final File host, final String dir) {
        if (host.isFile()) {
            this.addLangDirFromJar(host, dir);
        } else {
            final File hostdir = new File(host, dir);
            if (!hostdir.exists()) {
                System.err.println("Lang directory \"" + dir + "\" not found in " + host.getPath());
            } else if (hostdir.isDirectory()) {
                this.addLangDir(hostdir);
            } else if (hostdir.getName().endsWith(".lang")) {
                this.addLangFile(hostdir);
            } else {
                System.err.println("Lang file \"" + hostdir + "\"does not end in .lang");
            }
        }
    }

    public void addLangDir(final File dir) {
        File[] listFiles;
        for (int length = (listFiles = dir.listFiles()).length, i = 0; i < length; ++i) {
            final File child = listFiles[i];
            if (child.isDirectory()) {
                this.addLangDir(child);
            } else if (child.getName().endsWith(".lang")) {
                this.addLangFile(child);
            }
        }
    }

    public void addLangFile(final File child) {
        try {
            final String lang = child.getName().substring(0, child.getName().lastIndexOf('.'));
            final FileInputStream fin = new FileInputStream(child);
            this.addLangFile(fin, lang);
            fin.close();
        } catch (final IOException e) {
            System.err.println("Error occurred while loading lang file: " + child.getPath());
            e.printStackTrace();
        }
    }

    public void addLangDirFromJar(final File jar, String dir) {
        while (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        try {
            final ZipFile zf = new ZipFile(jar);
            final Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith(dir) && name.endsWith(".lang")) {
                    this.addLangFile(zf.getInputStream(entry), name.substring(name.lastIndexOf('/') + 1,
                            name.lastIndexOf('.')));
                }
            }
            zf.close();
        } catch (final IOException e) {
            System.err.println("Error while reading lang zip file: " + jar.getPath());
            e.printStackTrace();
        }
    }

    public File hostFile(final Class<?> clazz) {
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

}

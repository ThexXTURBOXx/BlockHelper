package mcp.mobius.waila.utils.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Configuration {

    public static final String CATEGORY_GENERAL = "general";

    private final File file;

    public final Map<String, Map<String, Property>> categories = new TreeMap<String, Map<String, Property>>();
    public final TreeMap<String, Property> generalProperties = new TreeMap<String, Property>();

    public Configuration(File file) {
        this.file = file;
        categories.put(CATEGORY_GENERAL, generalProperties);
    }

    public Property getOrCreateIntProperty(String key, String category, int defaultValue) {
        Property prop = getOrCreateProperty(key, category, Integer.toString(defaultValue));
        try {
            Integer.parseInt(prop.value);
            return prop;
        } catch (NumberFormatException e) {
            prop.value = Integer.toString(defaultValue);
            return prop;
        }
    }

    public Property getOrCreateBooleanProperty(String key, String category, boolean defaultValue) {
        Property prop = getOrCreateProperty(key, category, Boolean.toString(defaultValue));
        if (!"true".equalsIgnoreCase(prop.value) && !"false".equalsIgnoreCase(prop.value))
            prop.value = Boolean.toString(defaultValue);
        return prop;
    }

    public Property getOrCreateProperty(String key, String category, String defaultValue) {
        category = category.toLowerCase();
        Map<String, Property> source = categories.get(category);

        if (source == null) {
            source = new TreeMap<String, Property>();
            categories.put(category, source);
        }

        if (source.containsKey(key)) {
            return source.get(key);
        } else if (defaultValue != null) {
            Property property = new Property();

            source.put(key, property);
            property.name = key;

            property.value = defaultValue;
            return property;
        } else {
            return null;
        }
    }

    public void load() {
        try {
            if (file.getParentFile() != null)
                file.getParentFile().mkdirs();

            if (!file.exists() && !file.createNewFile())
                return;

            if (file.canRead()) {
                FileInputStream fileinputstream = new FileInputStream(file);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(fileinputstream, "UTF-8"));

                String line;
                Map<String, Property> currentMap = null;

                while (true) {
                    line = buffer.readLine();

                    if (line == null)
                        break;

                    int nameStart = -1, nameEnd = -1;
                    boolean skip = false;

                    for (int i = 0; i < line.length() && !skip; ++i) {
                        if (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '.') {
                            if (nameStart == -1)
                                nameStart = i;

                            nameEnd = i;
                        } else if (!Character.isWhitespace(line.charAt(i))) {
                            // ignore space charaters
                            switch (line.charAt(i)) {
                            case '#':
                                skip = true;
                                continue;
                            case '{':
                                String scopeName = line.substring(nameStart, nameEnd + 1);

                                currentMap = categories.get(scopeName);
                                if (currentMap == null) {
                                    currentMap = new TreeMap<String, Property>();
                                    categories.put(scopeName, currentMap);
                                }

                                break;
                            case '}':
                                currentMap = null;
                                break;
                            case '=':
                                String propertyName = line.substring(nameStart, nameEnd + 1);

                                if (currentMap == null) {
                                    throw new RuntimeException("property " + propertyName + " has no scope");
                                }

                                Property prop = new Property();
                                prop.name = propertyName;
                                prop.value = line.substring(i + 1);
                                i = line.length();

                                currentMap.put(propertyName, prop);

                                break;
                            default:
                                throw new RuntimeException("unknown character " + line.charAt(i));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            if (file.getParentFile() != null)
                file.getParentFile().mkdirs();

            if (!file.exists() && !file.createNewFile())
                return;

            if (file.canWrite()) {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

                buffer.write("# Configuration file\n");
                buffer.write("# Generated on " + DateFormat.getInstance().format(new Date()) + "\n");
                buffer.write("\n");

                for (Map.Entry<String, Map<String, Property>> category : categories.entrySet()) {
                    buffer.write("####################\n");
                    buffer.write("# " + category.getKey() + " \n");
                    buffer.write("####################\n\n");

                    buffer.write(category.getKey() + " {\n");
                    writeProperties(buffer, category.getValue().values());
                    buffer.write("}\n\n");
                }

                buffer.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeProperties(BufferedWriter buffer, Collection<Property> props) throws IOException {
        for (Property property : props) {
            if (property.comment != null)
                buffer.write("   # " + property.comment + "\n");

            buffer.write("   " + property.name + "=" + property.value);
            buffer.write("\n");
        }
    }

}

package mcp.mobius.waila.addons.vanilla;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import mcp.mobius.waila.api.INameResolver;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.mod_BlockHelper;

public class VanillaNameResolver implements INameResolver {

    public static final INameResolver INSTANCE = new VanillaNameResolver();

    private final Map<Integer, String> vanillaNames = new HashMap<Integer, String>();

    private VanillaNameResolver() {
        try {
            InputStream is = mod_BlockHelper.class.getResourceAsStream("/assets/waila/mc_lang/ids.properties");
            if (is == null) {
                mod_BlockHelper.LOG.severe("Failed to load Vanilla translations. " +
                                           "Vanilla blocks and items will have no names.");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            Properties prop = new Properties();
            prop.load(reader);
            reader.close();

            for (String key : prop.stringPropertyNames()) {
                vanillaNames.put(Integer.parseInt(key), prop.getProperty(key));
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE, "Critical error occurred while loading Vanilla translations!" +
                                                  "Vanilla blocks and items will have no names.", t);
        }
    }

    @Override
    public String getName(ItemStack stack) {
        String name = vanillaNames.get(stack.itemID);
        if (name == null) return null;
        return I18n.translate(name + ".name");
    }

}

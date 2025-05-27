package mcp.mobius.waila.utils.config;

public class Property {

    public String name;
    public String value;
    public String comment;

    public int getInt() {
        return getInt(-1);
    }

    public int getInt(int _default) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return _default;
        }
    }

    public boolean isIntValue() {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean getBoolean(boolean _default) {
        if (isBooleanValue()) {
            return Boolean.parseBoolean(value);
        } else {
            return _default;
        }
    }

    public boolean isBooleanValue() {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

}

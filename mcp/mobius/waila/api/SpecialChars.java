package mcp.mobius.waila.api;

import java.util.regex.Pattern;

public final class SpecialChars {

    public static final String MCStyle = "\u00A7";

    public static final String BLACK = MCStyle + "0";
    public static final String DBLUE = MCStyle + "1";
    public static final String DGREEN = MCStyle + "2";
    public static final String DAQUA = MCStyle + "3";
    public static final String DRED = MCStyle + "4";
    public static final String DPURPLE = MCStyle + "5";
    public static final String GOLD = MCStyle + "6";
    public static final String GRAY = MCStyle + "7";
    public static final String DGRAY = MCStyle + "8";
    public static final String BLUE = MCStyle + "9";
    public static final String GREEN = MCStyle + "a";
    public static final String AQUA = MCStyle + "b";
    public static final String RED = MCStyle + "c";
    public static final String LPURPLE = MCStyle + "d";
    public static final String YELLOW = MCStyle + "e";
    public static final String WHITE = MCStyle + "f";

    public static final String OBF = MCStyle + "k";
    public static final String BOLD = MCStyle + "l";
    public static final String STRIKE = MCStyle + "m";
    public static final String UNDER = MCStyle + "n";
    public static final String ITALIC = MCStyle + "o";
    public static final String RESET = MCStyle + "r";

    public static final String WailaStyle = "\u00A4";
    public static final String WailaIcon = "\u00A5";
    public static final String WailaRenderer = "\u00A6";
    public static final String TAB = WailaStyle + WailaStyle + "a";
    public static final String ALIGNRIGHT = WailaStyle + WailaStyle + "b";
    public static final String ALIGNCENTER = WailaStyle + WailaStyle + "c";
    public static final String HEART = WailaStyle + WailaIcon + "a";
    public static final String HHEART = WailaStyle + WailaIcon + "b";
    public static final String EHEART = WailaStyle + WailaIcon + "c";
    public static final String RENDER = WailaStyle + WailaRenderer + "a";

    public static final Pattern patternMinecraft = Pattern.compile("(?i)" + MCStyle + "[0-9A-FK-OR]");
    public static final Pattern patternWaila = Pattern.compile("(?i)" + WailaStyle + "(..)");
    public static final Pattern patternRender = Pattern.compile("(?i)" + RENDER + "\\{([^,}]*),?([^}]*)}");
    public static final Pattern patternTab = Pattern.compile("(?i)" + TAB);
    public static final Pattern patternRight = Pattern.compile("(?i)" + ALIGNRIGHT);
    public static final Pattern patternCenter = Pattern.compile("(?i)" + ALIGNCENTER);
    public static final Pattern patternIcon = Pattern.compile("(?i)" + WailaStyle + WailaIcon + "([0-9a-z])");
    public static final Pattern patternLineSplit =
            Pattern.compile("(?i)" + WailaStyle + WailaStyle + "[^" + WailaStyle + "]+|" + WailaStyle + WailaIcon +
                            "[0-9A-Z]|" + WailaStyle + WailaRenderer + "a\\{([^,}]*),?([^}]*)}|[^" + WailaStyle + "]+");

    /**
     * Helper method to get a proper RENDER string. Just put the name of the renderer and the params in, and it will
     * give back a directly usable String for the tooltip.
     *
     * @param name   The name of the renderer
     * @param params The parameters to pass to the renderer
     * @return The special RENDER string
     */
    public static String getRenderString(String name, String... params) {
        StringBuilder result = new StringBuilder(RENDER + "{" + name);
        for (String s : params) {
            result.append(",").append(s);
        }
        result.append("}");
        return result.toString();
    }

    private SpecialChars() {
        throw new UnsupportedOperationException();
    }

}

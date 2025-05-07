package mcp.mobius.waila.overlay;

import java.util.HashMap;
import java.util.Map;

import static mcp.mobius.waila.api.SpecialChars.WailaIcon;
import static mcp.mobius.waila.api.SpecialChars.WailaStyle;

public enum IconUI {
    HEART("/gui/icons.png", 52, 0, 9, 9, 16, 0, 9, 9, "a"),
    HHEART("/gui/icons.png", 61, 0, 9, 9, 16, 0, 9, 9, "b"),
    EHEART("/gui/icons.png", 16, 0, 9, 9, "c"),
    ARMOR("/gui/icons.png", 34, 9, 9, 9, "d"),
    HARMOR("/gui/icons.png", 25, 9, 9, 9, "e"),
    EARMOR("/gui/icons.png", 16, 9, 9, 9, "f"),
    BUBBLE("/gui/icons.png", 16, 18, 9, 9, "o"),
    BUBBLEEXP("/gui/icons.png", 25, 18, 9, 9, "p"),
    CURSOR("/gui/icons.png", 0, 0, 16, 16, "z");

    private static final Map<String, IconUI> lk = new HashMap<String, IconUI>();

    static {
        for (IconUI icon : IconUI.values())
            lk.put(icon.symbol, icon);
    }

    public final String texture;
    public final int u, v, su, sv;
    public final int bu, bv, bsu, bsv;
    public final String symbol;

    IconUI(String texture, int u, int v, int su, int sv, String symbol) {
        this(texture, u, v, su, sv, -1, -1, -1, -1, symbol);
    }

    IconUI(String texture, int u, int v, int su, int sv, int bu, int bv, int bsu, int bsv, String symbol) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.su = su;
        this.sv = sv;
        this.bu = bu;
        this.bv = bv;
        this.bsu = bsu;
        this.bsv = bsv;
        this.symbol = WailaStyle + WailaIcon + symbol;
    }

    public static IconUI bySymbol(String s) {
        IconUI iconUI = lk.get(s);
        return iconUI == null ? BUBBLEEXP : iconUI;
    }

}

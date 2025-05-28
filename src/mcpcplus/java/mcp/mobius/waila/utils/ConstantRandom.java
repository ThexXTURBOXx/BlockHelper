package mcp.mobius.waila.utils;

import java.util.Random;

public final class ConstantRandom extends Random {

    public static final ConstantRandom INSTANCE = new ConstantRandom();

    private static final long serialVersionUID = 2065235430889955492L;

    private ConstantRandom() {
        super(0);
    }

    @Override
    protected int next(int bits) {
        return 0;
    }

}

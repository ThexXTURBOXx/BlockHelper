package mcp.mobius.waila.utils;

import java.util.Random;

public class ConstantRandom extends Random {

    public static final ConstantRandom INSTANCE = new ConstantRandom();

    public ConstantRandom() {
        super(0);
    }

    @Override
    protected int next(int bits) {
        return 0;
    }

}

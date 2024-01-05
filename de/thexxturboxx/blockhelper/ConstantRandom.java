package de.thexxturboxx.blockhelper;

import java.util.Random;

public class ConstantRandom extends Random {

    public ConstantRandom() {
        super(0);
    }

    @Override
    protected int next(int bits) {
        return 0;
    }

}

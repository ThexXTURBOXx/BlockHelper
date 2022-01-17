package de.thexxturboxx.blockhelper;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;

public class BlockHelperKeyBinding extends KeyBinding {

    private static final Set<BlockHelperKeyBinding> BINDINGS = new HashSet<BlockHelperKeyBinding>();

    public int pressTime = 0;

    public BlockHelperKeyBinding(String s, int i) {
        super(s, i);
        BINDINGS.add(this);
    }

    public boolean isPressed() {
        return pressTime != 0;
    }

    public boolean isClicked() {
        return pressTime == 1;
    }

    public static void onTick() {
        for (BlockHelperKeyBinding b : BINDINGS) {
            if (Keyboard.isKeyDown(b.keyCode)) {
                b.pressTime++;
            } else {
                b.pressTime = 0;
            }
        }
    }

}

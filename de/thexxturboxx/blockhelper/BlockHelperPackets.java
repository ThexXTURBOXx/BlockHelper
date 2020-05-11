package de.thexxturboxx.blockhelper;

import java.util.ArrayList;
import java.util.List;

public class BlockHelperPackets {

    static List<String> infosl = new ArrayList<String>();

    public static void setInfo(PacketClient pc) {
        for (byte t : pc.data.keySet()) {
            try {
                infosl.set(t, pc.data.get(t));
            } catch (IndexOutOfBoundsException e) {
                infosl.add(t, null);
                infosl.set(t, pc.data.get(t));
            }
        }
    }

}
package de.thexxturboxx.blockhelper;

import java.util.HashMap;
import java.util.Map;

class PacketClient implements InfoHolder {

    public Map<Byte, String> data;

    PacketClient() {
        data = new HashMap<Byte, String>();
    }

    @Override
    public PacketClient add(int i, String data) {
        return add((byte) i, data);
    }

    @Override
    public PacketClient add(byte i, String data) {
        this.data.put(i, data);
        return this;
    }

    public String get(int type) {
        return get((byte) type);
    }

    public String get(byte type) {
        return data.get(type);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

}

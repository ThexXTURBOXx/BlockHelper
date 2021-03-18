package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.ArrayList;
import java.util.List;

class PacketClient implements InfoHolder {

    public List<String> data;

    PacketClient() {
        data = new ArrayList<String>();
    }

    @Override
    public PacketClient add(String data) {
        this.data.add(data);
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

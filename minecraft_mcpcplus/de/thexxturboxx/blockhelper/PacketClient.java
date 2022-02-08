package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PacketClient implements InfoHolder, Iterable<String> {

    public List<String> data;

    public PacketClient() {
        data = new ArrayList<String>();
    }

    @Override
    public PacketClient add(String data) {
        this.data.add(data);
        return this;
    }

    public String get(int type) {
        return data.get(type);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return data.iterator();
    }

}

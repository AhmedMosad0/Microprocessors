package Components;

import java.util.ArrayList;

import Helpers.CacheEntry;
import Helpers.RegFileEntry;

public class Cache {

    ArrayList<CacheEntry> cache;
    int size = 64;

    public Cache(ArrayList<CacheEntry> cache) {
        this.cache = cache;
    }

    public void initializeCache() {
        for (int i = 0; i < 64; i++) {
            CacheEntry entry = new CacheEntry(i);
            cache.add(entry);
        }
    }

    public void entry(CacheEntry entry) {

            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i).getAddress() == entry.getAddress()) {
                    cache.get(i).setValue(entry.getValue());
                    break;
                }
            }

    }

    public float getAddressValue(int address) throws Exception {

        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getAddress() == address) {
                return cache.get(i).getValue();
            }
        }

        throw new Exception("Address not Found");

    }

    public void preLoadValue(int address, float value) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getAddress() == address)
                cache.get(i).setValue(value);
        }
    }

    public String toString() {

        String str = "Cache\n";

        for (int i = 2; i < 4; i++) {
            str += "-------------------------\n" +
                    "Address: " + cache.get(i).getAddress() +
                    "\nValue: " + cache.get(i).getValue() +
                    "\n-------------------------\n";
        }

        return str;
    }

}
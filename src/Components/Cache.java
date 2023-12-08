package Components;

import java.util.ArrayList;

import Helpers.CacheEntry;

public class Cache {

    ArrayList<CacheEntry> cache;
    int size = 64;

    public Cache(ArrayList<CacheEntry> cache) {
        this.cache = cache;
    }

    public boolean entry(CacheEntry entry) {

        boolean contains = false;
        boolean enterdFlag = false;

        if (cache.size() < size) {
            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i).getAddress() == entry.getAddress()) {
                    cache.get(i).setValue(entry.getValue());
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                cache.add(entry);
            }
            enterdFlag = true;
        } else {
            enterdFlag = false;
            System.out.println("Cache is full!!");
        }
        return enterdFlag;
    }

    public float getAddressValue(int address) throws Exception {

        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getAddress() == address) {
                return cache.get(i).getValue();
            }
        }

        throw new Exception("Address not Found");

    }

    public String toString() {

        String str = "Cache\n";

        for (int i = 0; i < cache.size(); i++) {
            str += "-------------------------\n" +
                    "Address: " + cache.get(i).getAddress() +
                    "\nValue: " + cache.get(i).getValue() +
                    "\n-------------------------\n";
        }

        return str;
    }

}
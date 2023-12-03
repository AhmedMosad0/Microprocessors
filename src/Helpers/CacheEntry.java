package Helpers;

public class CacheEntry {
    int address;
    int value;

    
    public CacheEntry(int address, int value) {
        this.address = address;
        this.value = value;
    }


    public int getAddress() {
        return address;
    }


    public int getValue() {
        return value;
    }


    public void setAddress(int address) {
        this.address = address;
    }


    public void setValue(int value) {
        this.value = value;
    }



}

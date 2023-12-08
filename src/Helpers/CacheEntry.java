package Helpers;

public class CacheEntry {
    int address;
    float value;

    
    public CacheEntry(int address, float value) {
        this.address = address;
        this.value = value;
    }


    public int getAddress() {
        return address;
    }


    public float getValue() {
        return value;
    }


    public void setAddress(int address) {
        this.address = address;
    }


    public void setValue(float value) {
        this.value = value;
    }



}

package Helpers;

public class LSEntry {
    public String name;
    public int address;
    public boolean busy = false;
    public String V;
    public String Q;

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

}

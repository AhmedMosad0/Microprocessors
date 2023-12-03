package Helpers;

public class LSEntry {
    public String tag;
    public int address;
    public boolean busy = false;
    public double V;
    public String Q;

    public LSEntry(String tag, boolean busy, int address, double v, String q) {
        this.tag = tag;
        this.address = address;
        this.busy = busy;
        V = v;
        Q = q;
    }

    public String getTag() {
        return tag;
    }

    public int getAddress() {
        return address;
    }

    public boolean isBusy() {
        return busy;
    }

    public void settag(String tag) {
        this.tag = tag;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

}

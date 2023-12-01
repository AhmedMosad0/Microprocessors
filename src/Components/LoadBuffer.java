package Components;

import Helpers.LSEntry;

public class LoadBuffer {

    static LSEntry[] buffer;
    static boolean enterdFlag = false;

    public LoadBuffer(int size) {
        buffer = new LSEntry[size];
    }

    public static boolean add(int address) {
        boolean contains = false;

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == address) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            for (int i = 0; i < buffer.length; i++) {
                if (!buffer[i].busy) {
                    buffer[i].address = address;
                    buffer[i].name = "L" + i;
                    buffer[i].busy = true;
                    enterdFlag = true;
                    break;
                } else {
                    enterdFlag = false;
                    System.out.println("Load Buffer is full");
                }
            }
        } else {
            enterdFlag = false;
            System.out.println("This address is currently in the Load Buffer");
        }

        return enterdFlag;
    }

    public static void remove(String name) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].name.equals(name)) {
                buffer[i] = null;
                buffer[i].busy = false;
            }
        }
    }

    public String toString() {

        String str = "Load Buffer\n";

        for (int i = 0; i < buffer.length; i++) {
            str += "-------------------------\n" +
                    "Busy: " + buffer[i].busy +
                    "\nName: " + buffer[i].name +
                    "\nAddress: " + buffer[i].address +
                    "-------------------------\n";
        }

        return str;
    }
}

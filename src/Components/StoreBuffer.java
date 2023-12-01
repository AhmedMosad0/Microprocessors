package Components;

import Helpers.LSEntry;

public class StoreBuffer {
    static LSEntry[] buffer;
    static boolean enterdFlag = false;

    public StoreBuffer(int size) {
        buffer = new LSEntry[size];
    }







    public String toString() {

        String str = "Store Buffer\n";

        for (int i = 0; i < buffer.length; i++) {
            str += "-------------------------\n" +
                    "Busy: " + buffer[i].busy +
                    "\nName: " + buffer[i].name +
                    "\nAddress: " + buffer[i].address +
                    "\nV: " + buffer[i].V +
                    "\nQ: " + buffer[i].Q +
                    "-------------------------\n";
        }

        return str;
    }
}

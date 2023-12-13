package Components;

import Helpers.Instruction;
import Helpers.LSEntry;
import Helpers.RegFileEntry;

public class StoreBuffer {
    private static StoreBuffer instance;
    static LSEntry[] buffer;

    public StoreBuffer(int size) {
        buffer = new LSEntry[size];
    }

    public static StoreBuffer getInstance(int size) {
        if (instance == null) {
            instance = new StoreBuffer(size);

            for (int i = 0; i < size; i++) {
                String tag = "S" + i;
                instance.buffer[i] = new LSEntry(tag, false, 0, 0, "");
            }
        }
        return instance;
    }

    public void addNewEntry(Instruction instruction) {
        RegFileEntry[] regFile = RegFile.getRegisterFile();

        String q = "0";
        float value = 0;

        boolean canAdd = this.canAdd(instruction);

        if (canAdd) {
            for (int i = 0; i < regFile.length; i++) {
                if (instruction.r1.equals(regFile[i].getRegName())) {
                    if (!regFile[i].getQi().equals("0")) {
                        q = regFile[i].getQi();
                    } else {
                        value = regFile[i].getValue();
                    }
                }
            }

            int index = getFirstNotBusySlot();
            if (index != -1) {
                buffer[index].busy = true;
                buffer[index].address = instruction.address;
                buffer[index].Q = q;
                buffer[index].V = value;
            }

        }

    }

    public void removeEntry(int address) {

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == (address)) {
                buffer[i].address = 0;
                buffer[i].Q = "0";
                buffer[i].V = 0;
                buffer[i].busy = false;
            }
        }

    }

    public static int getFirstNotBusySlot() {
        for (int i = 0; i < buffer.length; i++) {
            if (!buffer[i].busy)
                return i;
        }
        return -1;
    }

    public static boolean hasSpace() {
        if (getFirstNotBusySlot() != -1)
            return true;
        else
            return false;
    }

    public boolean canAdd(Instruction instruction) {

        LSEntry[] loadBuffer = LoadBuffer.getBuffer();
        boolean canAdd = true;

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == instruction.address) {
                canAdd = false;
                break;
            }
        }

        for (int i = 0; i < loadBuffer.length; i++) {
            if (loadBuffer[i].address == instruction.address) {
                canAdd = false;
                break;
            }
        }

        return canAdd;

    }

    public String toString() {

        String str = "Store Buffer\n";

        for (int i = 0; i < buffer.length; i++) {
            str += //"-------------------------\n" +
                    "Busy: " + buffer[i].busy +
                    "\nTag: " + buffer[i].getTag() +
                    "\nAddress: " + buffer[i].address +
                    "\nV: " + buffer[i].V +
                    "\nQ: " + buffer[i].Q +
                    "\n-------------------------\n";
        }

        return str;
    }
}

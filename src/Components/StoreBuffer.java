package Components;

import Helpers.Instruction;
import Helpers.LSEntry;

public class StoreBuffer {
    static LSEntry[] buffer;

    public StoreBuffer(int size) {
        buffer = new LSEntry[size];
    }

    public boolean addNewEntry(Instruction instruction) {
        boolean contains = false;
        boolean enterdFlag = false;

        RegFileEntry[] regFile = RegFile.getRegisterFile();
        LSEntry[] loadBuffer = LoadBuffer.getBuffer();

        String q = "0";
        float value = 0;

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == instruction.address) {
                contains = true;
                break;
            }
        }

        for (int i = 0; i < loadBuffer.length; i++) {
            if (loadBuffer[i].address == instruction.address) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            for (int i = 0; i < regFile.length; i++) {
                if (instruction.r1.equals(regFile[i].Qi)) {
                    q = regFile[i].Qi;
                } else {
                    value = regFile[i].value;
                }
            }

            for (int i = 0; i < buffer.length; i++) {
                if (!buffer[i].busy) {
                    buffer[i].address = instruction.address;
                    buffer[i].busy = true;
                    buffer[i].Q = q;
                    buffer[i].V = value;
                    enterdFlag = true;
                    break;
                } else {
                    enterdFlag = false;
                    System.out.println("Store Buffer is full");
                }
            }
        } else {
            enterdFlag = false;
            System.out.println("This address is currently in the Store buffer or the Load buffer");
        }

        return enterdFlag;
    }

    public void remove(int address) {

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == (address)) {
                buffer[i] = null;
                buffer[i].busy = false;
            }
        }

    }

    public String toString() {

        String str = "Store Buffer\n";

        for (int i = 0; i < buffer.length; i++) {
            str += "-------------------------\n" +
                    "Busy: " + buffer[i].busy +
                    "\nAddress: " + buffer[i].address +
                    "\nV: " + buffer[i].V +
                    "\nQ: " + buffer[i].Q +
                    "\n-------------------------\n";
        }

        return str;
    }
}

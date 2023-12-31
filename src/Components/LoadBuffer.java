package Components;

import Helpers.Instruction;
import Helpers.LSEntry;
import Helpers.RegFileEntry;

public class LoadBuffer {

    private static LoadBuffer instance;
    static LSEntry[] buffer;
    int size;

    public LoadBuffer(int size) {
        buffer = new LSEntry[size];
        this.size = size;
    }

    public static LoadBuffer getInstance(int size) {
        if (instance == null) {
            instance = new LoadBuffer(size);

            for (int i = 0; i < size; i++) {
                String tag = "L" + i;
                instance.buffer[i] = new LSEntry(tag, false, 0, 0, "");
            }
        }
        return instance;
    }

    public static void addNewEntry(Instruction instruction) {
        RegFileEntry[] regFile = RegFile.getRegisterFile();
        int address = instruction.getAddress();
        boolean canAdd = canAdd(address);

        if (canAdd) {
            int index = getFirstNotBusySlot();
                if (index != -1) {
                    for(int i = 0 ; i < regFile.length ; i++ ){
                        if(instruction.getR1().equals(regFile[i].getRegName())){
                            regFile[i].setQi(buffer[index].getTag());
                            break;
                        }
                    }
                    buffer[index].address = address;
                    buffer[index].busy = true;
                }
            
        }

    }

    public static void removeEntry(String tag) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].tag.equals(tag)) {
                LSEntry temp = new LSEntry(tag, false, 0, 0, "");
                buffer[i] = temp;
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

    public static boolean canAdd(int address) {

        boolean canAdd = true;

        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].address == address) {
                canAdd = false;
                break;
            }
        }

        return canAdd;

    }

    public String toString() {

        String str = "Load Buffer\n";

        for (int i = 0; i < buffer.length; i++) {
            str += "-------------------------\n" +
                    "Busy: " + buffer[i].busy +
                    "\nTag: " + buffer[i].tag +
                    "\nAddress: " + buffer[i].address +
                    "\n-------------------------\n";
        }

        return str;
    }

    public static LSEntry[] getBuffer() {
        return buffer;
    }
}

package Components;

import Helpers.LSEntry;

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

            for(int i=0; i< size; i++) {
			String tag = "L"+i;
			instance.buffer[i] = new LSEntry(tag, false, 0 , 0 , "");
		}
        }
        return instance;
    }

    public void initializeLoadBuffer() {
		
	}

    public static boolean addNewEntry(int address) {
        boolean contains = false;
        boolean enterdFlag = false;

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

    public static void removeEntry(String tag) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i].tag.equals(tag)) {
                LSEntry temp = new LSEntry(tag, false, 0, 0, "");
                buffer[i] = temp;
            }
        }
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

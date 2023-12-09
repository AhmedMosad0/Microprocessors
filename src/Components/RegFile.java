package Components;

import Helpers.RegFileEntry;
import Helpers.ReservationStationEntry;

public class RegFile {
	static RegFileEntry[] registerFile;
	static final int size = 64;
	
	public RegFile() {
		this.registerFile = new RegFileEntry[size];
		initializeRegFile();
	}
	
	void initializeRegFile() {
		for(int i=0; i<32; i++) {
			registerFile[i] = new RegFileEntry("R"+i, "0" , 0);
		}
		int c = 1;
		for(int j=32; j<64; j++) {
			registerFile[j] = new RegFileEntry("F"+c, "0" , 0);
			c++;
		}
	}

	//call this when running main simulation to load into register file
	void loadIntoRegFile(String regName, float value) {
		for(int i=0; i< this.registerFile.length; i++) {
			if(registerFile[i].getRegName().equals(regName)) {
				registerFile[i].setValue(value);
			}
		}
	}
	
	//issue stage
	void addRegFileEntry(String regName, String insTag) {
		for(int i=0; i< this.registerFile.length; i++) {
			if(registerFile[i].getRegName().equals(regName)) {
				registerFile[i].setQi(insTag);
			}
		}
	}
	
	//write result stage
	void writeResultToRegFile(String regName, float value) {
		for(int i=0; i< this.registerFile.length; i++) {
			if(registerFile[i].getRegName().equals(regName)) {
				registerFile[i].setValue(value);
				registerFile[i].setQi("0");
			}
		}
	}

	public static RegFileEntry[] getRegisterFile() {
		return registerFile;
	}
	

	public String toString() {
		String str = "Register File\n";

		for (int i = 0 ; i < registerFile.length ; i++) {
			str += "-------------------------\n" +
					"\nRegister Name: " + registerFile[i].getRegName() +
					"\nQi: " + registerFile[i].getQi() +
					"\nValue: " + registerFile[i].getValue() +
					"\n-------------------------\n";
		}

		return str;
	}

}

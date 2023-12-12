package Components;

import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Queue;

import Helpers.Instruction;
import Helpers.RegFileEntry;
import Helpers.ReservationStationEntry;

public class MulDivRS {
	LinkedList<ReservationStationEntry> reservationStation = new LinkedList<>();
	int size;

	public MulDivRS(LinkedList<ReservationStationEntry> reservationStation, int size) {
		this.reservationStation = reservationStation;
		this.size = size;
	}

	void initializeMulDivRS() {
		// reservationStation = new LinkedList<>();
		for (int i = 0; i < this.size; i++) {
			String tag = "M" + i;
			reservationStation.add(new ReservationStationEntry(tag, false, "", 0.0, 0.0, "0", "0", 0, 0));
		}
	}

	// gets tag of 1st available RS slot
	String getFirstAvailableTag() {
		for (int i = 0; i < reservationStation.size(); i++) {
			if (!reservationStation.get(i).isBusy()) {
				return reservationStation.get(i).getTag();
			}
		}
		return "";
	}

	public void addNewEntry(Instruction instruction, int cycleCount) {
		RegFileEntry[] regFile = RegFile.getRegisterFile();

		for (int i = 0; i < reservationStation.size(); i++) {
			if (!reservationStation.get(i).isBusy()) {
				for (int j = 0; j < regFile.length; j++) {

					if (regFile[j].getRegName().equals(instruction.getR2())) {
						if (!regFile[j].getQi().equals("0")) {
							reservationStation.get(i).setQj(regFile[j].getQi());
						} else {
							reservationStation.get(i).setVj(regFile[j].getValue());
							reservationStation.get(i).setQj("0");
						}
					}

					if (regFile[j].getRegName().equals(instruction.getR3())) {
						if (!regFile[j].getQi().equals("0")) {
							reservationStation.get(i).setQk(regFile[j].getQi());
						} else {
							reservationStation.get(i).setVk(regFile[j].getValue());
							reservationStation.get(i).setQk("0");
						}
					}
					
					if (regFile[j].getRegName().equals(instruction.getR1())) {
						
						regFile[j].setQi(reservationStation.get(i).getTag());
					}
				}

				reservationStation.get(i).setBusy(true);
				reservationStation.get(i).setOperation(instruction.operation);
				reservationStation.get(i).setResult(0);
				reservationStation.get(i).setEntryCycle(cycleCount);

				// ReservationStationEntry entry = new
				// ReservationStationEntry(reservationStation.get(i).getTag(), true,
				// reservationStation.get(i).getOperation(), reservationStation.get(i).getVj(),
				// reservationStation.get(i).getVk(), reservationStation.get(i).getQj(),
				// reservationStation.get(i).getQk(), reservationStation.get(i).getResult() ,
				// 0);

				// entry.setEntryCycle(cycleCount);

				// reservationStation.add(i, entry);

				// check place in Vj/Vk or Qj/Qk?
				// check fl reg file r1 el qi bta3to 0 wala la, law msh zero get the string w
				// ahoto fe qj
				// same for r2 and put string in qk
				// otherwise, get vals of r1,r2 from reg file and place them fe Vj,Vk w zeros f
				// qj,qk

				break;
			}
		}
	}

	public boolean isStationFull() {
		if (getFirstAvailableTag().equals(""))
			return true;

		return false;
	}

	public void delMulDivEntry(String targetTag) {
		Iterator<ReservationStationEntry> iterator = reservationStation.iterator();
		while (iterator.hasNext()) {
			ReservationStationEntry entry = iterator.next();
			if (entry.getTag().equals(targetTag)) {
				entry.setBusy(false);
				entry.setOperation(" ");
				entry.setVj(0);
				entry.setVk(0);
				entry.setResult(0);
			}
		}
	}

	public String toString() {
		String str = "MulDiv Reservation Station\n";

		for (ReservationStationEntry entry : reservationStation) {
			str += "-------------------------\n" +
					"Busy: " + entry.isBusy() +
					"\nTag: " + entry.getTag() +
					"\nOp: " + entry.getOperation() +
					"\nVj: " + entry.getVj() +
					"\nVk: " + entry.getVk() +
					"\nQj: " + entry.getQj() +
					"\nQk: " + entry.getQk() +
					"\nResult: " + entry.getResult() +
					"\n-------------------------\n";
		}

		return str;
	}

}

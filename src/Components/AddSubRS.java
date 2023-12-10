package Components;

import java.util.Iterator;
import java.util.LinkedList;
import Helpers.RegFileEntry;
import Helpers.ReservationStationEntry;
import Helpers.Instruction;

public class AddSubRS {
	LinkedList<ReservationStationEntry> reservationStation = new LinkedList<>();
	int size;

	public AddSubRS(LinkedList<ReservationStationEntry> reservationStation, int size) {
		this.reservationStation = reservationStation;
		this.size = size;
	}

	void initializeAddSubRS() {
		// reservationStation = new LinkedList<>();
		for (int i = 0; i < this.size; i++) {
			String tag = "A" + i;
			reservationStation.add(new ReservationStationEntry(tag, false, "", 0.0, 0.0, "0", "0", 0, 0));
		}
	}

	public LinkedList<ReservationStationEntry> getReservationStation() {
		return reservationStation;
	}

	public int getSize() {
		return size;
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

					if (instruction.operation.equals("ADD") || instruction.operation.equals("SUB")
							|| instruction.operation.equals("SUBI") || instruction.operation.equals("ADDI")) {

						if (regFile[j].getRegName().equals(instruction.getR2())) {
							if (!regFile[j].getQi().equals("0")) {
								System.out.println(regFile[j].getQi() + " heeeeeeeeeeeeeeeeere");
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
						
					} else {

						if (regFile[j].getRegName().equals(instruction.getR1())) {
							if (!regFile[j].getQi().equals("0")) {
								reservationStation.get(i).setQj(regFile[j].getQi());
							} else {
								reservationStation.get(i).setVj(regFile[j].getValue());
								reservationStation.get(i).setQj("0");
							}
						}
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
				// reservationStation.get(i).getQk(), reservationStation.get(i).getResult(),
				// reservationStation.get(i).getBranchAddress());

				// entry.setEntryCycle(cycleCount);

				// reservationStation.add(i, entry);

				break;
				// check place in Vj/Vk or Qj/Qk?
				// check fl reg file r1 el qi bta3to 0 wala la, law msh zero get the string w
				// ahoto fe qj
				// same for r2 and put string in qk
				// otherwise, get vals of r1,r2 from reg file and place them fe Vj,Vk w zeros f
				// qj,qk
			}
		}
	}

	public boolean isStationFull() {
		return reservationStation.isEmpty();
	}

	public void delAddSubEntry(String targetTag) {
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
		String str = "AddSub Reservation Station\n";

		for (ReservationStationEntry entry : reservationStation) {
			str += "-------------------------\n" +
					"Busy: " + entry.isBusy() +
					"\nTag: " + entry.getTag() +
					"\nOp: " + entry.getOperation() +
					"\nVj: " + entry.getVj() +
					"\nVk: " + entry.getVk() +
					"\nQj: " + entry.getQj() +
					"\nQk: " + entry.getQk() +
					"\n-------------------------\n";
		}

		return str;
	}

}
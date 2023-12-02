package microprocessor;

import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Queue;

public class MulDivRS {
	LinkedList<ReservationStationEntry> reservationStation = new LinkedList<>();
	int size;

	public MulDivRS(LinkedList<ReservationStationEntry> reservationStation) {
		this.reservationStation = reservationStation;
	}
	
	void initializeMulDivRS() {
		//reservationStation = new LinkedList<>();
		for(int i=0; i< this.size; i++) {
			String tag = "M"+i;
			reservationStation.add(new ReservationStationEntry(tag, false, "", 0 , 0 , 0 , 0 , 0));
		}
	}
	
	//gets tag of 1st available RS slot 
	String getFirstAvailableTag() {
		for(int i=0; i<reservationStation.size(); i++) {
			if(!reservationStation.get(i).busy) {
				return reservationStation.get(i).tag;
			}
		}
		return "";
	}
		
	void addNewEntry(ReservationStationEntry entry) {
		for(int i=0; i<reservationStation.size(); i++) {
			if(!reservationStation.get(i).busy) {
				reservationStation.add(i, entry);
			}
		}
	}
	
	public boolean isStationFull() {
		return reservationStation.isEmpty();
	}
	
	public void delMulDivEntry(String targetTag) {
	    Iterator<ReservationStationEntry> iterator = reservationStation.iterator();
	    while (iterator.hasNext()) {
	        ReservationStationEntry entry = iterator.next();
	        if (entry.tag.equals(targetTag)) {
	            iterator.remove();
	        }
	    }
	}
	
	
	
}

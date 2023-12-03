package Components;

public class ReservationStationEntry {
	String tag;
	boolean busy;
	String operation;
	int vj, vk, qj, qk;
	int result;
	
	public ReservationStationEntry(String tag, boolean busy, String operation, int vj, int vk, int qj, int qk, int result) {
		this.tag = tag;
		this.busy = busy;
		this.operation = operation;
		this.vj = vj;
		this.vk = vk;
		this.qj = qj;
		this.qk = qk;
		this.result = result;
	}
	
}

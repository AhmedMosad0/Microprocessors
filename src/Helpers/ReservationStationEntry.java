package Helpers;

public class ReservationStationEntry {
	String tag;
	boolean busy;
	String operation;
	double vj, vk;
	String qj, qk;
	float result;
	int entryCycle;
	int branchAddress;
	
	public ReservationStationEntry(String tag, boolean busy, String operation, double vj, double vk, String qj, String qk, float result, int branchAddress) {
		this.tag = tag;
		this.busy = busy;
		this.operation = operation;
		this.vj = vj;
		this.vk = vk;
		this.qj = qj;
		this.qk = qk;
		this.result = result;
		this.branchAddress = branchAddress;
	}

	public int getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(int branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getTag() {
		return tag;
	}

	public int getEntryCycle() {
		return entryCycle;
	}

	public void setEntryCycle(int entryCycle) {
		this.entryCycle = entryCycle;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public double getVj() {
		return vj;
	}

	public void setVj(double vj) {
		this.vj = vj;
	}

	public double getVk() {
		return vk;
	}

	public void setVk(double vk) {
		this.vk = vk;
	}

	public String getQj() {
		return qj;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public String getQk() {
		return qk;
	}

	public void setQk(String qk) {
		this.qk = qk;
	}

	public float getResult() {
		return result;
	}

	public void setResult(float result) {
		this.result = result;
	}
	
}

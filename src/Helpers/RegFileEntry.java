package Helpers;

public class RegFileEntry {
	String regName;
	String Qi;
	float value;
	
	public RegFileEntry(String regName, String qi, float value) {
		this.regName = regName;
		Qi = qi;
		this.value = value;
	}

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getQi() {
		return Qi;
	}

	public void setQi(String qi) {
		Qi = qi;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	
}

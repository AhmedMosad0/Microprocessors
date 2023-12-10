package Helpers;


public class Instruction {
    public String r1;
    public String r2;
    public String r3;
    public int address;
    public float immediate;
    public String operation;

    
    public String getR1() {
        return r1;
    }
    public String getR2() {
        return r2;
    }
    public String getR3() {
        return r3;
    }
    public int getAddress() {
        return address;
    }
    public float getImmediate() {
        return immediate;
    }
    public void setR1(String r1) {
        this.r1 = r1;
    }
    public void setR2(String r2) {
        this.r2 = r2;
    }
    public void setR3(String r3) {
        this.r3 = r3;
    }
    public void setAddress(int address) {
        this.address = address;
    }
    public void setImmediate(float immediate) {
        this.immediate = immediate;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getOperation() {
        return operation;
    }

}

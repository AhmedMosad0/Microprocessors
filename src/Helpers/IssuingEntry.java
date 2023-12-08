package Helpers;

public class IssuingEntry {

    Instruction instruction;
    InstructionState state;
    int startExecution;
    int issueCycle;


    public IssuingEntry(Instruction instruction, InstructionState state) {
        this.instruction = instruction;
        this.state = state;
    }


    public int getStartExecution() {
        return startExecution;
    }


    public void setStartExecution(int startExecution) {
        this.startExecution = startExecution;
    }


    public int getIssueCycle() {
        return issueCycle;
    }


    public void setIssueCycle(int issueCycle) {
        this.issueCycle = issueCycle;
    }


    public Instruction getInstruction() {
        return instruction;
    }


    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }


    public InstructionState getState() {
        return state;
    }


    public void setState(InstructionState state) {
        this.state = state;
    }


}

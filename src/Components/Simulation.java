package Components;

import java.util.ArrayList;
import java.util.Scanner;

import Helpers.CacheEntry;
import Helpers.Instruction;
import Helpers.InstructionState;
import Helpers.IssuingEntry;

public class Simulation {
    ArrayList<Instruction> instructions;
    AddSubRS addSubRS;
    LoadBuffer loadBuffer;
    MulDivRS mulDivRS;
    StoreBuffer storeBuffer;
    RegFile regFile;
    Cache cache;
    Scanner sc = new Scanner(System.in);
    int addLatency;
    int subLatency;
    int divLatency;
    int mulLatency;
    int loadLatency;
    int storeLatency;
    int cycleCount;
    int instructionPointer = 0;
    ArrayList<IssuingEntry> queue;

    public Simulation(AddSubRS addSubRS, LoadBuffer loadBuffer, MulDivRS mulDivRS,
            StoreBuffer storeBuffer,
            RegFile regFile, Cache cache, Scanner sc, int addLatency, int subLatency, int divLatency, int mulLatency,
            int LoadLatency, int storeLatency, int cycleCount, ArrayList<IssuingEntry> queue) throws Exception {
        Parser p = new Parser();
        this.instructions = p.parse("instructions.txt");
        this.addSubRS = addSubRS;
        this.loadBuffer = loadBuffer;
        this.mulDivRS = mulDivRS;
        this.storeBuffer = storeBuffer;
        this.regFile = regFile;
        this.cache = cache;
        this.sc = sc;
        this.addLatency = Integer.parseInt(sc.nextLine());
        this.subLatency = Integer.parseInt(sc.nextLine());
        this.divLatency = Integer.parseInt(sc.nextLine());
        this.mulLatency = Integer.parseInt(sc.nextLine());
        this.loadLatency = Integer.parseInt(sc.nextLine());
        this.storeLatency = Integer.parseInt(sc.nextLine());
        this.cycleCount = cycleCount;
        this.queue = queue;
    }

    public void runSimulation() throws Exception {
        for (int i = 0; !instructions.isEmpty(); i++) {
            // executeCycle(); //logic of execution of one cycle
            // cycleCount++;

            issue();
            executeCycle();

            cycleCount++;

        }

    }

    public boolean issue() {
        Instruction currentInstruction = instructions.get(instructionPointer);
        switch (currentInstruction.operation) {
            case "SD":

                if (storeBuffer.hasSpace()) {

                    if (storeBuffer.canAdd(currentInstruction)) {
                        storeBuffer.addNewEntry(currentInstruction);
                        IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                        queue.add(entry);
                        return true;

                    } else {
                        System.out.println("Store Buffer or Load Buffer has the same effective address");
                        return false;
                    }
                } else {
                    System.out.println("Store Buffer is full");
                    return false;
                }

            case "LD":
                if (loadBuffer.hasSpace()) {

                    if (loadBuffer.canAdd(currentInstruction.address)) {
                        loadBuffer.addNewEntry(currentInstruction.address);
                        IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                        queue.add(entry);
                        return true;

                    } else {
                        System.out.println("Load Buffer has the same effective address");
                        return false;
                    }
                } else {
                    System.out.println("Store Buffer is full");
                    return false;
                }

        }
        return false;
    }

    // at every clock cycle
    public void executeCycle() throws Exception {
        // check operation
        Instruction currentInstruction;
        for (int i = 0; i < queue.size(); i++) {
            currentInstruction = queue.get(i).getInstruction();

            // check reservation station of operation
            // handle issuing
            switch (currentInstruction.operation) {
                case "ADD":
                    if (!addSubRS.isStationFull()) {
                        // addToAddSubRS()
                    }

                    break;
                case "MUL":
                    if (!mulDivRS.isStationFull()) {
                        // addToMulDivRS()
                    }

                    break;
                case "SD":
                    for (int j = 0; j < storeBuffer.buffer.length; j++) {
                        if (storeBuffer.buffer[j].address == currentInstruction.address) {

                            if (storeBuffer.buffer[j].Q.equals("0")) {
                                
                                if (queue.get(i).getState().equals(InstructionState.Writing)) {
                                    cache.entry(new CacheEntry(currentInstruction.address, storeBuffer.buffer[j].V));
                                    queue.get(i).setState(InstructionState.Finished);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + storeLatency == cycleCount)) {
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                } 

                                else if (queue.get(i).getState().equals(InstructionState.Issued)) {
                                    queue.get(i).setStartExecution(cycleCount);
                                    queue.get(i).setState(InstructionState.Executing);
                                }
                            }

                            ///// set StartExecution(clock Cycle)
                            ///// if(startExecution + latency = clock cycle) actually execute
                            ///// else
                        }
                    }

                    break;

                case "LD":
                   for (int j = 0; j < loadBuffer.buffer.length; j++) {
                        if (loadBuffer.buffer[j].address == currentInstruction.address) {
                                
                                if (queue.get(i).getState().equals(InstructionState.Writing)) {
                                    float value = cache.getAddressValue(currentInstruction.address);
                                    regFile.writeResultToRegFile(currentInstruction.r1, value);
                                    queue.get(i).setState(InstructionState.Finished);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + loadLatency == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                } 

                                else if (queue.get(i).getState().equals(InstructionState.Issued)) {
                                    queue.get(i).setStartExecution(cycleCount);
                                    queue.get(i).setState(InstructionState.Executing);
                                }
                            

                            ///// set StartExecution(clock Cycle)
                            ///// if(startExecution + latency = clock cycle) actually execute
                            ///// else
                        }
                    }

                    break;
                // case "SD": checkStoreEmptySlot();break;
                // case "LD": checkLoadSubEmptySlot();break;

            }
        }

    }

    public void printCycle(int cycle) {
        System.out.println(cycle + "\n////////////////////////");
        // addSubRS.toString();
        // mulDivRS.toString();
        loadBuffer.toString();
        storeBuffer.toString();
        // regFile.toString();
    }

    // public void checkStoreEmptySlot(){

    // }
    // public void checkLoadSubEmptySlot(){

    // }

    public static void main(String[] args) throws Exception {

    }

}

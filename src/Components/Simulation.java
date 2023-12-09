package Components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import Helpers.CacheEntry;
import Helpers.Instruction;
import Helpers.InstructionState;
import Helpers.IssuingEntry;
import Helpers.LSEntry;
import Helpers.ReservationStationEntry;

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
    int cycleCount = 1;
    int instructionPointer = 0;
    ArrayList<IssuingEntry> queue;
    boolean branchStall = false;

    public Simulation(AddSubRS addSubRS, LoadBuffer loadBuffer, MulDivRS mulDivRS,
            StoreBuffer storeBuffer,
            RegFile regFile, Cache cache, int addLatency, int subLatency, int divLatency, int mulLatency,
            int loadLatency, int storeLatency, ArrayList<IssuingEntry> queue) throws Exception {
        Parser p = new Parser();
        this.instructions = p.parse("instructions.txt");
        this.addSubRS = addSubRS;
        this.loadBuffer = loadBuffer;
        this.mulDivRS = mulDivRS;
        this.storeBuffer = storeBuffer;
        this.regFile = regFile;
        this.cache = cache;
        this.addLatency = addLatency;
        this.subLatency = subLatency;
        this.divLatency = divLatency;
        this.mulLatency = mulLatency;
        this.loadLatency = loadLatency;
        this.storeLatency = storeLatency;
        this.queue = queue;
    }

    public void runSimulation() throws Exception {
        issue();
        boolean first = true;
        instructionPointer++;

        while (!queue.isEmpty()) {
            // executeCycle(); //logic of execution of one cycle
            // cycleCount++

            boolean issued = false;

            if (!branchStall && !first && instructionPointer < instructions.size()) {
                issued = issue();
            }

            updateStations();
            executeCycle();
            updateStations();

            if (issued) {
                instructionPointer++;
            }

            printCycle(cycleCount);

            cycleCount++;
            first = false;

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
                        entry.setIssueCycle(cycleCount);
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
                        loadBuffer.addNewEntry(currentInstruction);
                        IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                        entry.setIssueCycle(cycleCount);
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

            case "ADD":
                if (!addSubRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    addSubRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    return true;
                } else {
                    System.out.println("Add/Sub RS is full");
                    return false;
                }

            case "SUB":
                if (!addSubRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    addSubRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    return true;
                } else {
                    System.out.println("Add/Sub RS is full");
                    return false;
                }

            case "SUBI":
            case "ADDI":
                if (!addSubRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    addSubRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    return true;
                } else {
                    System.out.println("Add/Sub RS is full");
                    return false;
                }

            case "MUL":
                if (!mulDivRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    mulDivRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    return true;
                } else {
                    System.out.println("Mul/Div RS is full");
                    return false;
                }

            case "DIV":
                if (!mulDivRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    mulDivRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    return true;
                } else {
                    System.out.println("Mul/Div RS is full");
                    return false;
                }

            case "BNEZ":
                if (!addSubRS.isStationFull()) {
                    IssuingEntry entry = new IssuingEntry(currentInstruction, InstructionState.Issued);
                    addSubRS.addNewEntry(currentInstruction, cycleCount);
                    entry.setIssueCycle(cycleCount);
                    queue.add(entry);
                    branchStall = true;
                    return true;
                } else {
                    System.out.println("Add/Sub RS is full");
                    return false;
                }
        }
        return false;
    }

    public void updateStations() {
        for (int i = 0; i < queue.size(); i++) {
            Instruction current = queue.get(i).getInstruction();

            for (int j = 0; j < addSubRS.getSize(); j++) {
                if (addSubRS.reservationStation.get(j).isBusy() == true) {
                    if (addSubRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                        ReservationStationEntry addS = addSubRS.reservationStation.get(j);
                        if (!addS.getQj().equals("0")) {
                            for (int k = 0; k < regFile.registerFile.length; k++) {
                                if (current.getR2().equals(regFile.registerFile[k].getRegName())) {
                                    addS.setQj(regFile.registerFile[k].getQi());
                                    if (addS.getQj().equals("0")) {
                                        addS.setVj(regFile.registerFile[k].getValue());
                                    }
                                    break;
                                }
                            }
                        }
                        if (!addS.getQk().equals("0")) {
                            for (int k = 0; k < regFile.registerFile.length; k++) {
                                if (current.getR3().equals(regFile.registerFile[k].getRegName())) {
                                    addS.setQk(regFile.registerFile[k].getQi());
                                    if (addS.getQk().equals("0")) {
                                        addS.setVk(regFile.registerFile[k].getValue());
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            for (int j = 0; j < mulDivRS.reservationStation.size(); j++) {
                if (mulDivRS.reservationStation.get(j).isBusy() == true) {
                    if (mulDivRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                        ReservationStationEntry mulS = mulDivRS.reservationStation.get(j);
                        if (!mulS.getQj().equals("0")) {
                            for (int k = 0; k < regFile.registerFile.length; k++) {
                                if (current.getR2().equals(regFile.registerFile[k].getRegName())) {
                                    mulS.setQj(regFile.registerFile[k].getQi());
                                    if (mulS.getQj().equals("0")) {
                                        mulS.setVj(regFile.registerFile[k].getValue());
                                    }
                                    break;
                                }
                            }
                        }
                        if (!mulS.getQk().equals("0")) {
                            for (int k = 0; k < regFile.registerFile.length; k++) {
                                if (current.getR3().equals(regFile.registerFile[k].getRegName())) {
                                    mulS.setQk(regFile.registerFile[k].getQi());
                                    if (mulS.getQk().equals("0")) {
                                        mulS.setVk(regFile.registerFile[k].getValue());
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            for (int j = 0; j < storeBuffer.buffer.length; j++) {
                if (storeBuffer.buffer[j].isBusy() == true) {
                    LSEntry entry = storeBuffer.buffer[j];
                    if (current.address == entry.getAddress()) {
                        if (!entry.getQ().equals("0")) {
                            for (int k = 0; k < regFile.registerFile.length; k++) {
                                if (current.getR1().equals(regFile.registerFile[k].getRegName())) {
                                    entry.setQ(regFile.registerFile[k].getQi());
                                    if (entry.getQ().equals("0")) {
                                        entry.setV(regFile.registerFile[k].getValue());
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

        }
    }

    // at every clock cycle
    public void executeCycle() throws Exception {
        // check operation
        Instruction currentInstruction;
        boolean write = false;

        for (int i = 0; i < queue.size(); i++) {
            currentInstruction = queue.get(i).getInstruction();

            // check reservation station of operation
            // handle issuing
            switch (currentInstruction.operation) {
                case "ADD":
                    for (int j = 0; j < addSubRS.getSize(); j++) {
                        if (addSubRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            ReservationStationEntry current = addSubRS.reservationStation.get(j);

                            if (current.getQj().equals("0") && current.getQk().equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    write = true;
                                    regFile.writeResultToRegFile(currentInstruction.getR1(), current.getResult());
                                    addSubRS.delAddSubEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {
                                    if ((queue.get(i).getStartExecution() + addLatency - 1 == cycleCount)) {
                                        current.setResult(alu(currentInstruction));
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);

                                    if ((queue.get(i).getStartExecution() + addLatency - 1 == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    } else
                                        queue.get(i).setState(InstructionState.Executing);

                                }

                                break;
                            }
                        }
                    }

                    break;

                case "SUB":
                    for (int j = 0; j < addSubRS.getSize(); j++) {
                        if (addSubRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            ReservationStationEntry current = addSubRS.reservationStation.get(j);

                            if (current.getQj().equals("0") && current.getQk().equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    write = true;
                                    regFile.writeResultToRegFile(currentInstruction.getR1(), current.getResult());
                                    addSubRS.delAddSubEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + subLatency - 1 == cycleCount)) {
                                        current.setResult(alu(currentInstruction));
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);

                                    if ((queue.get(i).getStartExecution() + subLatency - 1 == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    } else
                                        queue.get(i).setState(InstructionState.Executing);

                                }

                                break;
                            }
                        }
                    }

                    break;

                case "MUL":
                    for (int j = 0; j < mulDivRS.reservationStation.size(); j++) {
                        if (mulDivRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            ReservationStationEntry current = mulDivRS.reservationStation.get(j);

                            if (current.getQj().equals("0") && current.getQk().equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    write = true;
                                    regFile.writeResultToRegFile(currentInstruction.getR1(), current.getResult());
                                    mulDivRS.delMulDivEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + mulLatency - 1 == cycleCount)) {
                                        current.setResult(alu(currentInstruction));
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);

                                    if ((queue.get(i).getStartExecution() + mulLatency - 1 == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    } else
                                        queue.get(i).setState(InstructionState.Executing);

                                }

                                break;
                            }
                        }
                    }

                    break;

                case "DIV":
                    for (int j = 0; j < mulDivRS.reservationStation.size(); j++) {
                        if (mulDivRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            ReservationStationEntry current = mulDivRS.reservationStation.get(j);

                            if (current.getQj().equals("0") && current.getQk().equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    write = true;
                                    regFile.writeResultToRegFile(currentInstruction.getR1(), current.getResult());
                                    mulDivRS.delMulDivEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + divLatency - 1 == cycleCount)) {
                                        current.setResult(alu(currentInstruction));
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);

                                    if ((queue.get(i).getStartExecution() + divLatency - 1 == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    } else
                                        queue.get(i).setState(InstructionState.Executing);

                                }

                                break;
                            }
                        }
                    }

                    break;

                case "ADDI":
                case "SUBI":
                    for (int j = 0; j < addSubRS.getSize(); j++) {
                        if(cycleCount <= 10){
                        System.out.println("\n1111\n");
                        if (addSubRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            System.out.println("\n2222\n");
                            ReservationStationEntry current = addSubRS.reservationStation.get(j);

                            System.out.println("Qj: " +current.getQj() + " Qk: " + current.getQk());

                            if (current.getQj().equals("0")) {
                                System.out.println("\n3333 dakhlt wel Q = 0\n");

                                System.out.println(write);
                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    System.out.println("\n 4444 dakhalt w ana writing\n");
                                    write = true;
                                    regFile.writeResultToRegFile(currentInstruction.getR1(), current.getResult());
                                    addSubRS.delAddSubEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    System.out.println("\n 5555 dakhalt w ana ha execute " + currentInstruction.operation + "\n");

                                    current.setResult(alu(currentInstruction));
                                    queue.get(i).setState(InstructionState.Writing);
                                }

                                break;
                            }
                        }
                    }
                    }

                    break;

                case "BNEZ":
                    for (int j = 0; j < addSubRS.getSize(); j++) {
                        if (addSubRS.reservationStation.get(j).getEntryCycle() == queue.get(i).getIssueCycle()) {
                            ReservationStationEntry current = addSubRS.reservationStation.get(j);

                            if (current.getQj().equals("0") && current.getQk().equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing)) {
                                    if (current.getResult() == 1) {
                                        current.setBranchAddress(0);
                                        instructionPointer = current.getBranchAddress();
                                    }
                                    branchStall = false;
                                    addSubRS.delAddSubEntry(current.getTag());
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {
                                    current.setResult(alu(currentInstruction));
                                    queue.get(i).setState(InstructionState.Writing);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);
                                    queue.get(i).setState(InstructionState.Executing);
                                }

                                break;
                            }
                        }
                    }

                    break;

                case "SD":
                    for (int j = 0; j < storeBuffer.buffer.length; j++) {
                        if (storeBuffer.buffer[j].address == currentInstruction.address) {

                            if (storeBuffer.buffer[j].Q.equals("0")) {

                                if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                    write = true;
                                    cache.entry(new CacheEntry(currentInstruction.address, storeBuffer.buffer[j].V));
                                    storeBuffer.removeEntry(currentInstruction.address);
                                    queue.get(i).setState(InstructionState.Finished);
                                    queue.remove(i);
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                    if ((queue.get(i).getStartExecution() + storeLatency - 1 == cycleCount)) {
                                        queue.get(i).setState(InstructionState.Writing);
                                    }
                                }

                                else if (queue.get(i).getState().equals(InstructionState.Issued)
                                        && queue.get(i).getIssueCycle() < cycleCount) {
                                    queue.get(i).setStartExecution(cycleCount);

                                    if ((queue.get(i).getStartExecution() + storeLatency - 1 == cycleCount)) {

                                        queue.get(i).setState(InstructionState.Writing);
                                    } else
                                        queue.get(i).setState(InstructionState.Executing);

                                }
                                break;
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

                            if (queue.get(i).getState().equals(InstructionState.Writing) && !write) {
                                write = true;
                                float value = cache.getAddressValue(currentInstruction.address);
                                regFile.writeResultToRegFile(currentInstruction.r1, value);
                                loadBuffer.removeEntry(loadBuffer.buffer[j].getTag());
                                queue.get(i).setState(InstructionState.Finished);
                                queue.remove(i);
                            }

                            else if (queue.get(i).getState().equals(InstructionState.Executing)) {

                                if ((queue.get(i).getStartExecution() + loadLatency - 1 == cycleCount)) {

                                    queue.get(i).setState(InstructionState.Writing);
                                }
                            }

                            else if (queue.get(i).getState().equals(InstructionState.Issued)
                                    && queue.get(i).getIssueCycle() < cycleCount) {
                                queue.get(i).setStartExecution(cycleCount);

                                if ((queue.get(i).getStartExecution() + loadLatency - 1 == cycleCount)) {

                                    queue.get(i).setState(InstructionState.Writing);
                                } else
                                    queue.get(i).setState(InstructionState.Executing);

                            }

                            break;
                            ///// set StartExecution(clock Cycle)
                            ///// if(startExecution + latency = clock cycle) actually execute
                            ///// else
                        }
                    }

                    break;

            }
        }

    }

    public void flush(int c) {
        for (int i = c; i < queue.size(); i++) {
            queue.remove(i);
            //////// undo everything done
        }
    }

    public float alu(Instruction instruction) {
        String operation = instruction.getOperation();
        float r1Value = 0;
        float r2Value = 0;
        float r3Value = 0;
        float result = 0;
        float immediate = instruction.getImmediate();

        for (int i = 0; i < regFile.registerFile.length; i++) {
            if (regFile.registerFile[i].getRegName().equals(instruction.getR1())) {
                r1Value = regFile.registerFile[i].getValue();
            }

            if (regFile.registerFile[i].getRegName().equals(instruction.getR2())) {
                r2Value = regFile.registerFile[i].getValue();
            }

            if (regFile.registerFile[i].getRegName().equals(instruction.getR3())) {
                r3Value = regFile.registerFile[i].getValue();
            }
        }

        if (operation.equals("ADD")) {
            result = r3Value + r2Value;
        }

        else if (operation.equals("SUB")) {
            result = r2Value - r3Value;
        }

        else if (operation.equals("MUL")) {
            result = r3Value * r2Value;
        }

        else if (operation.equals("DIV")) {
            result = r2Value / r3Value;
        }

        else if (operation.equals("ADDI")) {
            result = r2Value + immediate;
        }

        else if (operation.equals("SUBI")) {
            System.out.println("DAKHALT EL ALUUUUUU");
            result = r2Value - immediate;
            System.out.println("\n" + result + "\n");
        }

        else if (operation.equals("BNEZ")) {
            if (r1Value == 0)
                result = 1;
            else
                result = -1;
        }

        return result;
    }

    public void printCycle(int cycle) {
        if(cycleCount <= 10){
        System.out.println(cycle + "\n////////////////////////");
        System.out.println(addSubRS.toString());
        System.out.println(mulDivRS.toString());
        System.out.println(loadBuffer.toString());
        System.out.println(storeBuffer.toString());
        System.out.println(regFile.toString());
        System.out.println(cache.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        LinkedList<ReservationStationEntry> addSubReservationStation = new LinkedList<>();
        LinkedList<ReservationStationEntry> mulDivReservationStation = new LinkedList<>();
        ArrayList<CacheEntry> cacheEntries = new ArrayList<>();
        ArrayList<IssuingEntry> queue = new ArrayList<>();

        AddSubRS addSubRS = new AddSubRS(addSubReservationStation, 3);
        MulDivRS mulDivRS = new MulDivRS(mulDivReservationStation, 3);
        StoreBuffer storeBuffer = StoreBuffer.getInstance(3);
        LoadBuffer loadBuffer = LoadBuffer.getInstance(3);
        Cache cache = new Cache(cacheEntries);
        RegFile regFile = new RegFile();

        addSubRS.initializeAddSubRS();
        mulDivRS.initializeMulDivRS();
        regFile.initializeRegFile();
        cache.initializeCache();

        regFile.loadIntoRegFile("R2", 5);
        // regFile.loadIntoRegFile("R1", 10);
        regFile.loadIntoRegFile("R3", 10);
        cache.preLoadValue(2, 10);

        Simulation simulation = new Simulation(addSubRS, loadBuffer, mulDivRS, storeBuffer, regFile, cache, 2, 2, 4, 4,
                1, 1, queue);

        simulation.runSimulation();
    }

}
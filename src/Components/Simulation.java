package Components;
import java.util.ArrayList;
import java.util.Scanner;

import Helpers.Instruction;

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
    int LoadLatency;
    int storeLatency;
    int cycleCount;
    int instructionPointer;


    public Simulation(ArrayList<Instruction> instructions,AddSubRS addSubRS, LoadBuffer loadBuffer, MulDivRS mulDivRS, StoreBuffer storeBuffer,
            RegFile regFile, Cache cache, Scanner sc,int addLatency,int subLatency,int divLatency, int mulLatency, int LoadLatency,int storeLatency, int cycleCount, int instructionPointer)throws Exception {
        Parser p = new Parser();
        this.instructions = p.parse("instructions.txt");
        this.addSubRS = addSubRS;
        this.loadBuffer = loadBuffer;
        this.mulDivRS = mulDivRS;
        this.storeBuffer = storeBuffer;
        this.regFile = regFile;
        this.cache = cache;
        this.sc = sc;
        this.addLatency =Integer.parseInt(sc.nextLine()) ;
        this.subLatency =Integer.parseInt(sc.nextLine()) ;
        this.divLatency =Integer.parseInt(sc.nextLine()) ;
        this.mulLatency =Integer.parseInt(sc.nextLine()) ;
        this.LoadLatency =Integer.parseInt(sc.nextLine()) ;
        this.storeLatency =Integer.parseInt(sc.nextLine()) ;
        this.cycleCount = cycleCount;
        this.instructionPointer = instructionPointer;
    }

    public void runSimulation(){
        while(!instructions.isEmpty()){
            // executeCycle();  //logic of execution of one cycle
            // cycleCount++;


        }

    }
    //at every clock cycle
    public void executeCycle(){
        //check operation
        Instruction currentInstruction = instructions.get(instructionPointer);
         //check reservation station of operation
         //handle issuing
        switch(currentInstruction.operation){
            case "ADD": if(!addSubRS.isStationFull()){
                //addToAddSubRS()
            };
            break;
            case "MUL": if(!mulDivRS.isStationFull()){
                //addToMulDivRS()
            };
            break;
            // case "SD": checkStoreEmptySlot();break;
            // case "LD": checkLoadSubEmptySlot();break;


        }

      
    }

  
    // public void checkStoreEmptySlot(){

    // }
    // public void checkLoadSubEmptySlot(){

    // }


    public static void main(String[]args)throws Exception{
  

    }
    
}

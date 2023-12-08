package Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import Helpers.Instruction;

public class Parser {

    ArrayList<Instruction> instructions;

    public Parser() {
        this.instructions = new ArrayList<>();
    }

    public ArrayList<Instruction> parse(String fileName) throws Exception {

        File file = new File(fileName);
        Scanner reader;
        try {
            reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split(" ");
                String r1 = "";
                String r2 = "";
                String r3 = "";
                int address = -1;
                float immediate = -1;
                String operation = "";
                int branchLocation = -1;
                switch (line[0]) {
                    case "LD":
                        operation = "LD";
                        r1 = line[1].substring(1);
                        address = Integer.parseInt(line[2].substring(1));
                        break;
                    case "SD":
                        operation = "SD";
                        r1 = line[1].substring(1);
                        address = Integer.parseInt(line[2].substring(1));
                        break;
                    case "ADD":
                        operation = "ADD";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        r3 = line[3].substring(1);
                        break;
                    case "SUB":
                        operation = "SUB";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        r3 = line[3].substring(1);
                        break;
                    case "MUL":
                        operation = "MUL";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        r3 = line[3].substring(1);
                        break;
                    case "DIV":
                        operation = "DIV";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        r3 = line[3].substring(1);
                        break;
                    case "ADDI":
                        operation = "ADDI";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        immediate = Float.parseFloat(line[3].substring(1));
                        break;

                    case "SUBI":
                        operation = "SUBI";

                        r1 = line[1].substring(1);
                        r2 = line[2].substring(1);
                        immediate = Float.parseFloat(line[3].substring(1));
                        break;

                    case "BNEZ":
                        operation = "BNEZ";

                        r1 = line[1].substring(1);
                        branchLocation = Integer.parseInt(line[2].substring(1));
                        break;

                    default:
                        throw new Exception("Incorrect command!");
                }
                Instruction instruction = new Instruction();
                instruction.r1 = r1;
                instruction.r2 = r2;
                instruction.r3 = r3;
                instruction.address = address;
                instruction.branchLocation = branchLocation;
                instruction.immediate = immediate;
                instruction.operation = operation;
                instructions.add(instruction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return instructions;
    }
}

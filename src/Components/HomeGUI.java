package Components;

import javax.swing.*;

import Helpers.CacheEntry;
import Helpers.IssuingEntry;
import Helpers.ReservationStationEntry;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class HomeGUI extends JFrame {
    static JTextArea programInstructions;
    static JTextArea registerFile;
    static JTextArea addMul;
    static JTextArea loadStore;
    static JTextArea cache;

    public HomeGUI() {

        JFrame frame = new JFrame("Window Division Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        programInstructions = new JTextArea("Program Instructions");
        JScrollPane scrollProgramInstructions = new JScrollPane(programInstructions);

        registerFile = new JTextArea("Register File");
        JScrollPane scrollRegisterFile = new JScrollPane(registerFile);

        JButton run = new JButton("Run the program");
        JTextField textBox1 = new JTextField("Enter add latency:");
        JTextField textBox2 = new JTextField("Enter sub latency:");
        JTextField textBox3 = new JTextField("Enter mul latency:");
        JTextField textBox4 = new JTextField("Enter div latency:");
        JTextField textBox5 = new JTextField("Enter load latency:");
        JTextField textBox6 = new JTextField("Enter store latency:");
        JTextField textBox7 = new JTextField("Enter add buffer size:");
        JTextField textBox8 = new JTextField("Enter mul buffer size:");
        JTextField textBox9 = new JTextField("Enter load buffer size:");
        JTextField textBox10 = new JTextField("Enter store buffer size:");

        Dimension textBoxSize = new Dimension(150, 30);
        textBox1.setPreferredSize(textBoxSize);
        textBox2.setPreferredSize(textBoxSize);
        textBox3.setPreferredSize(textBoxSize);
        textBox4.setPreferredSize(textBoxSize);
        textBox5.setPreferredSize(textBoxSize);
        textBox6.setPreferredSize(textBoxSize);
        textBox7.setPreferredSize(textBoxSize);
        textBox8.setPreferredSize(textBoxSize);
        textBox9.setPreferredSize(textBoxSize);
        textBox10.setPreferredSize(textBoxSize);

        JPanel textBoxPanel = new JPanel(new GridLayout(2, 3));
        textBoxPanel.add(textBox1);
        textBoxPanel.add(textBox2);
        textBoxPanel.add(textBox3);
        textBoxPanel.add(textBox4);
        textBoxPanel.add(textBox5);
        textBoxPanel.add(textBox6);
        textBoxPanel.add(textBox7);
        textBoxPanel.add(textBox8);
        textBoxPanel.add(textBox9);
        textBoxPanel.add(textBox10);

        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the text from the text area
                String content = programInstructions.getText();

                // Write the content to a file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("instructions.txt"))) {
                    writer.write(content);
                    writer.close();

                    int addLatency = Integer.parseInt(textBox1.getText());
                    int subLatency = Integer.parseInt(textBox2.getText());
                    int mulLatency = Integer.parseInt(textBox3.getText());
                    int divLatency = Integer.parseInt(textBox4.getText());
                    int loadLatency = Integer.parseInt(textBox5.getText());
                    int storeLatency = Integer.parseInt(textBox6.getText());
                    int addSize = Integer.parseInt(textBox7.getText());
                    
                    int mulSize = Integer.parseInt(textBox8.getText());
                    int loadSize = Integer.parseInt(textBox9.getText());
                    int storeSize = Integer.parseInt(textBox10.getText());

                    LinkedList<ReservationStationEntry> addSubReservationStation = new LinkedList<>();
                    LinkedList<ReservationStationEntry> mulDivReservationStation = new LinkedList<>();
                    ArrayList<CacheEntry> cacheEntries = new ArrayList<>();
                    ArrayList<IssuingEntry> queue = new ArrayList<>();

                    AddSubRS addSubRS = new AddSubRS(addSubReservationStation, addSize);
                    MulDivRS mulDivRS = new MulDivRS(mulDivReservationStation, mulSize);
                    StoreBuffer storeBuffer = StoreBuffer.getInstance(storeSize);
                    LoadBuffer loadBuffer = LoadBuffer.getInstance(loadSize);
                    Cache cache = new Cache(cacheEntries);
                    RegFile regFile = new RegFile();

                    addSubRS.initializeAddSubRS();
                    mulDivRS.initializeMulDivRS();
                    regFile.initializeRegFile();
                    cache.initializeCache();

                    regFile.loadIntoRegFile("R2", 5);
                    // regFile.loadIntoRegFile("R1", 10);
                    regFile.loadIntoRegFile("R10", 10);
                    regFile.loadIntoRegFile("R1", 2);

                    cache.preLoadValue(2, 10);


                    Simulation simulation = new Simulation(addSubRS, loadBuffer, mulDivRS, storeBuffer, regFile, cache,
                            addLatency, subLatency, mulLatency, divLatency,
                            loadLatency, storeLatency, queue);  //keda nkhaly sim kaman yakhod sizes el buffers

                    simulation.runSimulation();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        run.addActionListener(buttonListener);

        addMul = new JTextArea("Add/Sub & Mul/Div Buffers");
        JScrollPane scrollAddMul = new JScrollPane(addMul);

        loadStore = new JTextArea("Load & Store Buffers");
        JScrollPane scrollLoadStore = new JScrollPane(loadStore);

        cache = new JTextArea("Cache");
        JScrollPane scrollCache = new JScrollPane(cache);

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setPreferredSize(new Dimension(200, 300));
        topPanel.add(scrollProgramInstructions);
        topPanel.add(scrollRegisterFile);

        JPanel middlePanel = new JPanel(new FlowLayout());
        middlePanel.add(run);
        middlePanel.add(textBoxPanel);


        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.setPreferredSize(new Dimension(200, 300));
        bottomPanel.add(scrollAddMul);
        bottomPanel.add(scrollLoadStore);
        bottomPanel.add(scrollCache);

        container.add(topPanel, BorderLayout.NORTH);
        container.add(middlePanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);

        // Display the JFrame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HomeGUI();
    }
}

package calc2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class Calc2 extends JFrame {

    private String stringNum1 = "";
    private String stringNum2 = "";
    private String memoryStored = "";
    private String currentInput = "";
    private String sCopy;
    private String sPaste;

    private String whichOperatorPressed, whichOtherOp;
    private double num1, num2, result;
    private JPanel panel1, panel2;

    private JButton[] buttonArr;
    private JButton decimal, add, subtract, multiply,
            divide, equals, clear, back, MC,
            MR, MS, MAdd, Mminus, sqrt, percent,
            reciprocal, negate, copy, paste;

    private JMenuBar menuBar;
    private JMenu menu;
    private JTextArea displayArea;
    private boolean operatorPressed, equalsPressed, error, sqrtPressed;

    private OperationListener oListener;
    private ButtonListener bListener;
    private MemoryListener mListener;

    private Clipboard clipboard;
    private StringSelection stringSelection;

    private final ImageIcon image;

    public Calc2() throws IOException {

        image = new ImageIcon("calcicon.png");

        setIconImage(image.getImage());

        setTitle("Calculator");
        setSize(230, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setupButtons();
        setUpListeners();
        setupJMenu();

        setVisible(true);
    }

    private void setupButtons() {
        oListener = new OperationListener();
        bListener = new ButtonListener();
        mListener = new MemoryListener();

        panel1 = new JPanel();
        panel2 = new JPanel();
        buttonArr = new JButton[10];

        displayArea = new JTextArea("\t\t0", 2, 10);
        displayArea.setEditable(false);
        displayArea.setBorder(new EtchedBorder());

        panel2.add(displayArea, BorderLayout.NORTH);

        add = new JButton("+");
        subtract = new JButton("-");
        multiply = new JButton("*");
        divide = new JButton("/");
        equals = new JButton("=");
        clear = new JButton("C");
        back = new JButton("\u2190");
        MC = new JButton("MC");
        MR = new JButton("MR");
        MS = new JButton("MS");
        MAdd = new JButton("M+");
        Mminus = new JButton("M-");
        sqrt = new JButton("\u221A");
        percent = new JButton("\u0025");
        reciprocal = new JButton("1/x");
        negate = new JButton("\u00B1");
        decimal = new JButton(".");

        MC.addActionListener(mListener);
        MR.addActionListener(mListener);
        MS.addActionListener(mListener);
        MAdd.addActionListener(mListener);
        Mminus.addActionListener(mListener);

        // sqrt.addActionListener(oListener);
        percent.addActionListener(oListener);
        // reciprocal.addActionListener(oListener);
        // negate.addActionListener(oListener);
        add.addActionListener(oListener);
        subtract.addActionListener(oListener);
        multiply.addActionListener(oListener);
        divide.addActionListener(oListener);

        clear.addActionListener(bListener);
        decimal.addActionListener(bListener);

        panel1.setLayout(new GridLayout(5, 5));
        panel1.add(MC);
        panel1.add(MR);
        panel1.add(MS);
        panel1.add(MAdd);
        panel1.add(Mminus);
        panel1.add(back);
        panel1.add(clear);
        panel1.add(negate);
        panel1.add(sqrt);

        for (int i = 0; i < buttonArr.length; i++) {
            buttonArr[i] = new JButton(i + "");
            buttonArr[i].addActionListener(bListener);
        }
        panel1.add(divide);
        panel1.add(percent);
        panel1.add(multiply);
        panel1.add(reciprocal);
        panel1.add(subtract);
        panel1.add(add);
        panel1.add(decimal);
        panel1.add(equals);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(4, 4));
        panel3.add(buttonArr[7]);
        panel3.add(buttonArr[8]);
        panel3.add(buttonArr[9]);
        panel3.add(buttonArr[4]);
        panel3.add(buttonArr[5]);
        panel3.add(buttonArr[6]);
        panel3.add(buttonArr[1]);
        panel3.add(buttonArr[2]);
        panel3.add(buttonArr[3]);
        panel3.add(buttonArr[0]);
        panel2.add(panel3);
        this.add(panel2, BorderLayout.NORTH);
        this.add(panel1, BorderLayout.CENTER);
        this.pack();
    }

    private void setUpListeners() {
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });

        equals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stringNum1.equals("")) {
                    equalsPressed = true;
                    operatorPressed = false;//Ensures that new numbers are stored in stringNnum1
                    String temp = stringNum2;
                    operate();
                    stringNum2 = temp;
                }
            }
        });
        sqrt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double result = 0.0;

                if (!(stringNum1.equals(""))) {
                    if (stringNum2.equals("")) {

                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.sqrt(num1);
                        if (!operatorPressed) {
                            stringNum1 = String.valueOf(result);
                        } else {
                            stringNum2 = String.valueOf(result);
                        }
                    } else {
                        num2 = Double.parseDouble(stringNum2);
                        result = Operate.sqrt(num2);
                        stringNum2 = String.valueOf(result);
                    }
                    if (equalsPressed) {
                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.sqrt(num1);
                        stringNum1 = String.valueOf(result);
                    }
                    updateDisplayArea(String.valueOf(result));
                }

            }
        });

        reciprocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double result = 0.0;
                System.out.println("s1: " + stringNum1 + " s2: " + stringNum2);
                if (!(stringNum1.equals(""))) {
                    if (stringNum2.equals("")) {

                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.reciprocal(num1);
                        if (!operatorPressed) {
                            stringNum1 = String.valueOf(result);
                        } else {
                            stringNum2 = String.valueOf(result);
                        }
                    } else {
                        num2 = Double.parseDouble(stringNum2);
                        result = Operate.reciprocal(num2);
                        stringNum2 = String.valueOf(result);
                    }
                    if (equalsPressed) {
                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.reciprocal(num1);
                        stringNum1 = String.valueOf(result);
                    }
                    updateDisplayArea(String.valueOf(result));
                }

            }
        });
        negate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double result = 0.0;
                System.out.println("s1: " + stringNum1 + " s2: " + stringNum2);
                if (!(stringNum1.equals(""))) {
                    if (stringNum2.equals("")) {

                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.negate(num1);
                        if (!operatorPressed) {
                            stringNum1 = String.valueOf(result);
                        } else {
                            stringNum2 = String.valueOf(result);
                        }
                    } else {
                        num2 = Double.parseDouble(stringNum2);
                        result = Operate.negate(num2);
                        stringNum2 = String.valueOf(result);
                    }
                    if (equalsPressed) {
                        num1 = Double.parseDouble(stringNum1);
                        result = Operate.negate(num1);
                        stringNum1 = String.valueOf(result);
                    }
                    updateDisplayArea(String.valueOf(result));
                }

            }
        });

    }

    private void operate() {

        if (!stringNum2.equals("")) {
            num1 = Double.parseDouble(stringNum1);
            num2 = Double.parseDouble(stringNum2);

            switch (whichOperatorPressed) {
                case "+":
                    result = Operate.add(num1, num2);
                    break;
                case "-":
                    result = Operate.subtract(num1, num2);
                    break;
                case "*":
                    result = Operate.multiply(num1, num2);
                    break;
                case "/":
                    if (num2 == 0) {
                        error = true;
                    } else {
                        result = Operate.divide(num1, num2);
                    }
                    break;
            }

            stringNum1 = String.valueOf(result);
            stringNum2 = "";

            if (equalsPressed) {
                if (!error) {
                    updateDisplayArea(String.valueOf(result));
                } else {
                    updateDisplayArea("Error");//To catch divide by zero errors
                    error = false;
                }
            }
            updateDisplayArea(String.valueOf(result));
        }
    }

    private class MemoryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String memorySelected = e.getActionCommand();
            switch (memorySelected) {
                case "MC":
                    memoryStored = "0";
                    updateDisplayArea("\t0");
                    break;
                case "MR":
                    mR();
                    break;
                case "MS":
                    MS();
                    break;
                case "M+":
                    mAdd(e);
                    break;
                case "M-":
                    mMinus();
                    break;
            }
//            stringNum1 = "";
//            stringNum2 = "";

        }

        private void MS() {
            if (!(stringNum1.equals(""))) {
                if (stringNum2.equals("")) {
                    memoryStored = stringNum1;
                    
                }
            } 
            else {
               memoryStored = stringNum2;
            }
            if (equalsPressed) {
                 memoryStored = stringNum2;
            }
        }

        private void mR() {
//            if (!memoryStored.equals("")) {
//                if (stringNum1.equals("")) {
//                    stringNum1 = memoryStored;
//                    updateDisplayArea("M\t" + memoryStored);
//                } else if (stringNum2.equals("")) {
//                    stringNum2 = memoryStored;
//                    updateDisplayArea("M\t" + memoryStored);
//                }
//            }
            updateDisplayArea(memoryStored);
        }

        private void mAdd(ActionEvent e) {
//            if (!memoryStored.equals("")) {
//                if (stringNum1.equals("")) {
//                    stringNum2 = String.valueOf(Double.parseDouble(stringNum2) + Double.parseDouble(memoryStored));
//                } else if (stringNum2.equals("")) {
//                    stringNum1 = String.valueOf(Double.parseDouble(stringNum1) + Double.parseDouble(memoryStored));
//                }
//            }
            String onScreen = "";
            onScreen += displayArea.getText();
            if (onScreen.contains("M")) {
                System.out.println("contain M");
                onScreen = onScreen.substring(1, onScreen.length());
            }
            memoryStored = String.valueOf(Double.parseDouble(memoryStored) + Double.parseDouble(onScreen));
            System.out.println("memoryAdd " + memoryStored);
        }

        private void mMinus() {
//            if (!memoryStored.equals("")) {
//                if (stringNum1.equals("")) {
//                    stringNum2 = String.valueOf(Double.parseDouble(stringNum2) - Double.parseDouble(memoryStored));
//                } else if (stringNum2.equals("")) {
//                    stringNum1 = String.valueOf(Double.parseDouble(stringNum1) - Double.parseDouble(memoryStored));
//                }
//            }

            String onScreen = "";
            onScreen += displayArea.getText();
            if (onScreen.contains("M")) {
                System.out.println("contain M");
                onScreen = onScreen.substring(1, onScreen.length());
            }
            memoryStored = String.valueOf(Double.parseDouble(memoryStored) - Double.parseDouble(onScreen));
            System.out.println("memoryAdd " + memoryStored);
        }
    }

    private class OperationListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            whichOperatorPressed = e.getActionCommand();
            System.out.println(whichOperatorPressed);

            operatorPressed = true;
            if (!equalsPressed) {
                operate();
                updateDisplayArea(whichOperatorPressed);

            }
        }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(clear)) {
                updateDisplayArea("0");
                clear();
            } else {

                if (equalsPressed) {
                    equalsPressed = false;
                    stringNum2 = "";
                    if (!operatorPressed) {
                        stringNum1 = "";
                    }
                }

                if (!operatorPressed) {
                    stringNum1 += e.getActionCommand();
                    currentInput = stringNum1;
                } else if (operatorPressed) {
                    stringNum2 += e.getActionCommand();
                    currentInput = stringNum2;
                }
                updateDisplayArea(currentInput);
            }
        }
    }

    private void back() {
        if (!operatorPressed) {
            if (stringNum1.length() > 0) {
                stringNum1 = stringNum1.substring(0, stringNum1.length() - 1);
                updateDisplayArea(stringNum1);
            }
        } else {
            if (stringNum2.length() > 0) {
                stringNum2 = stringNum2.substring(0, stringNum2.length() - 1);
                updateDisplayArea(stringNum2);
            }
        }
    }

    private void clear() {
        stringNum1 = "";
        stringNum2 = "";
        num1 = 0;
        num2 = 0;
        whichOperatorPressed = "";
        equalsPressed = false;
        operatorPressed = false;
    }

    private void updateDisplayArea(String displayString) {
        displayArea.setText(displayString);
    }

    private void setupJMenu() {
        stringSelection = new StringSelection("");
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        menuBar = new JMenuBar();
        menu = new JMenu("Edit");
        copy = new JButton("Copy");
        paste = new JButton("Paste");

        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!currentInput.equals("")) {
                    sCopy = currentInput;
                    stringSelection = new StringSelection(sCopy);
                    clipboard.setContents(stringSelection, stringSelection);
                }
            }
        });
        paste.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    sPaste = (String) clipboard.getData(DataFlavor.stringFlavor);

                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(Calc2.class
                            .getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Calc2.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                if (!sPaste.equals("")) {
                    if (stringNum1.equals("")) {
                        stringNum1 = sPaste;
                    } else if (stringNum2.equals("")) {
                        stringNum2 = sPaste;
                    }
                }
                updateDisplayArea(sPaste);
            }
        });

        menu.add(copy);
        menu.add(paste);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

    }

    private class otherOpsListener implements ActionListener {
//
//        @Override

        public void actionPerformed(ActionEvent e) {
//            whichOtherOp = e.getActionCommand();
//            switch (whichOtherOp) {
//                case "\u221A":
//                    result = Operate.sqrt(Double.parseDouble(currentInput));
//                    break;
//                case "\u0025":
//                    result = Operate.percent(Double.parseDouble(currentInput));
//                    break;
//
//                case "1/x":
//                    result = Operate.reciprocal(Double.parseDouble(currentInput));
//                    break;
//                case "\u00B1":
//                    result = Operate.negate(Double.parseDouble(currentInput));
//            }
//
        }
    }

    public static void main(String[] args) throws IOException {
        new Calc2();
    }

}

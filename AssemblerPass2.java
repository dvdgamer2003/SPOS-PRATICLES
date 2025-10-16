// Import required Java I/O and utility classes

import java.io.*;
import java.util.*;

// Class to hold each symbol and its address from the symbol table
class SymTuple {
    String symbol, address;

    // Constructor for symbol table entry
    SymTuple(String s1, String s2) {
        symbol = s1;
        address = s2;
    }
}

// Class to hold each literal and its address from the literal table
class LitTuple {
    String literal, address;

    // Constructor for literal table entry
    LitTuple(String s1, String s2) {
        literal = s1;
        address = s2;
    }
}

public class AssemblerPass2 {

    // ArrayLists to store entries from the Symbol Table and Literal Table
    static ArrayList<SymTuple> symTable = new ArrayList<>();
    static ArrayList<LitTuple> litTable = new ArrayList<>();

    // ------------------------------------------------------------------
    // Method: initializeTables()
    // Purpose: Reads the Symbol Table and Literal Table created in Pass 1
    // ------------------------------------------------------------------
    static void initializeTables() throws Exception {

        // --- Load SYMBOL TABLE ---
        BufferedReader br = new BufferedReader(new FileReader("symtable.txt"));
        String s;

        // Read each line of the symbol table file
        while ((s = br.readLine()) != null) {
            // Split by tab ("\t")
            StringTokenizer st = new StringTokenizer(s, "\t");
            // Add new symbol entry into symTable
            symTable.add(new SymTuple(st.nextToken(), st.nextToken()));
        }
        br.close(); // Close file after reading

        // --- Load LITERAL TABLE ---
        br = new BufferedReader(new FileReader("littable.txt"));

        // Read each line of literal table file
        while ((s = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s, "\t");
            // Add new literal entry into litTable
            litTable.add(new LitTuple(st.nextToken(), st.nextToken()));
        }
        br.close(); // Close file after reading
    }

    // ------------------------------------------------------------------
    // Method: pass2()
    // Purpose: Reads Intermediate Code from Pass 1 and generates Machine Code
    // ------------------------------------------------------------------
    static void pass2() throws Exception {

        // Open intermediate code file (output from Pass 1)
        BufferedReader input = new BufferedReader(new FileReader("output_pass1.txt"));

        // Output file where final machine code will be written
        PrintWriter output = new PrintWriter(new FileWriter("output_pass2.txt"), true);

        String line; // To store each line of intermediate code

        // Read each line from Pass 1 output
        while ((line = input.readLine()) != null) {

            // Remove all brackets "()" from intermediate code
            // Example: (IS,04) → IS,04
            line = line.replaceAll("[()]", " ");

            // Split line into tokens (based on whitespace)
            String[] tokens = line.trim().split("\\s+");

            // Skip if line is empty
            if (tokens.length == 0) continue;

            // Result string to build one line of machine code
            String result = "";

            // First token is usually the location counter
            String loc = tokens[0];
            result += loc + " ";  // Add LC to output

            // Second token gives mnemonic info, e.g., IS,04
            String[] mnemonic = tokens[1].split(",");
            String type = mnemonic[0];   // e.g., IS / DL
            String opcode = mnemonic[1]; // e.g., 04 (ADD)

            // ------------------------------
            // Handle Imperative Statements
            // ------------------------------
            if (type.equals("IS")) {
                result += opcode + " "; // Add operation code

                // Loop through remaining tokens (like registers, symbols, literals)
                for (int i = 2; i < tokens.length; i++) {
                    String[] parts = tokens[i].split(",");

                    switch (parts[0]) {
                        // If Register (e.g., RG,1)
                        case "RG":
                            result += parts[1] + " ";
                            break;

                        // If Symbol reference (e.g., S,0 → use symbol table)
                        case "S":
                            result += symTable.get(Integer.parseInt(parts[1])).address + " ";
                            break;

                        // If Literal reference (e.g., L,1 → use literal table)
                        case "L":
                            result += litTable.get(Integer.parseInt(parts[1])).address + " ";
                            break;
                    }
                }
            }

            // ------------------------------
            // Handle Declarative Statements
            // ------------------------------
            else if (type.equals("DL")) {  // DL (Declarative)
                if (opcode.equals("02")) {  // DC (Define Constant)
                    String[] parts = tokens[2].split(",");
                    // Machine code format for DC → 00 00 constant_value
                    result += "00 00 " + parts[1] + " ";
                }
            }

            // Display on console and write to file
            System.out.println(result);
            output.println(result);
        }

        // Close files
        input.close();
        output.close();
    }

    // ------------------------------------------------------------------
    // MAIN METHOD
    // ------------------------------------------------------------------
    public static void main(String[] args) throws Exception {

        // Step 1: Load Symbol Table and Literal Table from Pass 1
        initializeTables();

        // Step 2: Process Intermediate Code and generate final Machine Code
        pass2();

        // Completion message
        System.out.println("\nPass-II Completed! Machine code generated in output_pass2.txt");
    }
}

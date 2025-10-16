// Importing Java utilities and I/O classes
import java.util.*;
import java.io.*;

// Main class for Pass 1 of the assembler
public class AssemblerPass1 {

    // Variable to hold the current address (Location Counter)
    static int address = 0;

    // Arrays to store the addresses of symbols and literals
    static int[] symbolAddr = new int[10];
    static int[] literalAddr = new int[10];

    public static void main(String[] args) {

        // --- Define instruction sets and directives ---

        // Imperative Statements: actual instructions that generate machine code
        String[] IS = {"ADD", "SUB", "MUL", "MOV"};

        // Registers supported by the assembler
        String[] REG = {"AREG", "BREG", "CREG", "DREG"};

        // Assembler Directives: control statements for the assembler
        String[] AD = {"START", "END"};

        // Declarative Statements: define constants or storage (data definition)
        String[] DL = {"DC", "DS"};

        // Arrays to store symbol and literal names
        String[] symbols = new String[10];
        String[] literals = new String[10];

        // Counters to keep track of how many symbols/literals have been found
        int symCount = 0, litCount = 0;

        // Location Counter (LC) – keeps track of current instruction address
        int lc = 0;

        try {
            // --- Step 1: Open required files ---

            // Input file containing assembly source code
            BufferedReader br = new BufferedReader(new FileReader("initial.txt"));

            // Output file for Intermediate Code (IM.txt)
            PrintWriter ic = new PrintWriter(new File("IM.txt"));

            // Output file for Symbol Table (ST.txt)
            PrintWriter stFile = new PrintWriter(new File("ST.txt"));

            // Output file for Literal Table (LT.txt)
            PrintWriter ltFile = new PrintWriter(new File("LT.txt"));

            String line;

            // --- Step 2: Read input assembly program line by line ---
            while ((line = br.readLine()) != null) {

                // Split each line into tokens (space separated)
                StringTokenizer st = new StringTokenizer(line, " ");

                // Process every token (word) in the line
                while (st.hasMoreTokens()) {

                    // Extract next token (e.g., "START", "ADD", "AREG", "A", etc.)
                    String token = st.nextToken();

                    // -------------------------------------------------------
                    // Case 1: START directive — sets the starting address
                    // -------------------------------------------------------
                    if (token.matches("\\d+") && token.length() > 2) { // If token is a number (like 200)
                        lc = Integer.parseInt(token);   // Set LC to that number
                        address = lc - 1;               // Address will start from LC - 1
                        ic.println(lc);                  // Write LC to intermediate file
                    }

                    // -------------------------------------------------------
                    // Case 2: Assembler Directives (START, END)
                    // -------------------------------------------------------
                    for (int i = 0; i < AD.length; i++)
                        if (token.equals(AD[i]))
                            ic.print("AD " + (i + 1) + " ");  // Write AD (Assembler Directive) to IM file

                    // -------------------------------------------------------
                    // Case 3: Imperative Statements (ADD, SUB, MOV, etc.)
                    // -------------------------------------------------------
                    for (int i = 0; i < IS.length; i++)
                        if (token.equals(IS[i]))
                            ic.print("IS " + (i + 1) + " ");  // Write IS (Imperative Statement) to IM file

                    // -------------------------------------------------------
                    // Case 4: Registers (AREG, BREG, etc.)
                    // -------------------------------------------------------
                    for (int i = 0; i < REG.length; i++)
                        if (token.equals(REG[i]))
                            ic.print((i + 1) + " ");          // Write register number (1–4)

                    // -------------------------------------------------------
                    // Case 5: Declarative Statements (DC, DS)
                    // -------------------------------------------------------
                    for (int i = 0; i < DL.length; i++)
                        if (token.equals(DL[i]))
                            ic.print("DL " + (i + 1) + " ");  // Write DL (Declarative) to IM file

                    // -------------------------------------------------------
                    // Case 6: Symbol Detection
                    // -------------------------------------------------------
                    // If token is a single character (like A, B) and more tokens follow
                    if (token.length() == 1 && st.hasMoreTokens()) {
                        ic.print(token + " ");                 // Write symbol to IM
                        symbolAddr[symCount] = address;        // Store address in symbol table
                        symbols[symCount++] = token;           // Store symbol name
                    }

                    // -------------------------------------------------------
                    // Case 7: Literal Detection (=5, =1, etc.)
                    // -------------------------------------------------------
                    if (token.charAt(0) == '=') {              // Check if token starts with '='
                        ic.print("L" + litCount);              // Represent literal as L0, L1, etc.
                        literals[litCount] = token;            // Store literal in literal array
                        litCount++;                            // Increment literal count
                    }

                    // -------------------------------------------------------
                    // Case 8: DS Handling (Data Space Reservation)
                    // -------------------------------------------------------
                    if (token.equals("DS")) {
                        // Next token will be number of memory locations to reserve
                        int val = Integer.parseInt(st.nextToken());
                        address += val - 1;  // Increase address by size-1 since LC auto increments later
                    }

                    // -------------------------------------------------------
                    // End of line: Move to next line in IM file
                    // -------------------------------------------------------
                    if (!st.hasMoreTokens())
                        ic.println(); // Add a newline in IM output
                }

                // Increment address counter after processing the line
                address++;
            }

            // --- Step 3: Write Symbol Table (ST.txt) ---
            for (int i = 0; i < symCount; i++)
                stFile.println(i + "\t" + symbols[i] + "\t" + symbolAddr[i]);

            // --- Step 4: Assign Addresses to Literals and Write Literal Table (LT.txt) ---
            for (int i = 0; i < litCount; i++) {
                literalAddr[i] = address++;                        // Assign new address for each literal
                ltFile.println(i + "\t" + literals[i] + "\t" + literalAddr[i]);
            }

            // --- Step 5: Close all files ---
            ic.close();
            stFile.close();
            ltFile.close();
            br.close();

            // --- Step 6: Display completion message ---
            System.out.println("Pass-I Completed! Generated IM, ST, LT files.");
        }

        // If any error occurs during file I/O
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

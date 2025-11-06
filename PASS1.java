// Importing Java utilities and I/O classes
import java.util.*;
import java.io.*;

// Main class for Pass 1 of the assembler
public class AssemblerPass1 {

    // Variable to hold the current address (Location Counter)
    static int address = 0;

    // Arrays to store the addresses of symbols and literals
    static int[] symbolAddr = new int[20];
    static int[] literalAddr = new int[20];

    public static void main(String[] args) {

        // --- Define instruction sets and directives ---

        // Imperative Statements (IS): actual instructions that generate machine code
        String[] IS = {
            "STOP", "ADD", "SUB", "MULT", "MOVER", "MOVEM",
            "COMP", "BC", "DIV", "READ", "PRINT"
        };

        // Registers supported by the assembler
        String[] REG = {"AREG", "BREG", "CREG", "DREG"};

        // Assembler Directives (AD): control statements for the assembler
        String[] AD = {
            "START", "END", "ORIGIN", "EQU", "LTORG"
        };

        // Declarative Statements (DL): define constants or storage
        String[] DL = {"DC", "DS"};

        // Arrays to store symbol and literal names
        String[] symbols = new String[20];
        String[] literals = new String[20];

        // Counters to keep track of how many symbols/literals have been found
        int symCount = 0, litCount = 0;

        // Location Counter (LC)
        int lc = 0;

        try {
            // --- Step 1: Open required files ---
            BufferedReader br = new BufferedReader(new FileReader("initial.txt"));
            PrintWriter ic = new PrintWriter(new File("IM.txt"));
            PrintWriter stFile = new PrintWriter(new File("ST.txt"));
            PrintWriter ltFile = new PrintWriter(new File("LT.txt"));

            String line;

            // --- Step 2: Read input assembly program line by line ---
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " ");

                while (st.hasMoreTokens()) {
                    String token = st.nextToken();

                    // Case 1: Numeric (START address)
                    if (token.matches("\\d+")) {
                        lc = Integer.parseInt(token);
                        address = lc - 1;
                        ic.println(lc);
                    }

                    // Case 2: Assembler Directives (AD)
                    for (int i = 0; i < AD.length; i++)
                        if (token.equals(AD[i]))
                            ic.print("AD(" + (i + 1) + ") ");

                    // Case 3: Imperative Statements (IS)
                    for (int i = 0; i < IS.length; i++)
                        if (token.equals(IS[i]))
                            ic.print("IS(" + (i + 1) + ") ");

                    // Case 4: Registers
                    for (int i = 0; i < REG.length; i++)
                        if (token.equals(REG[i]))
                            ic.print((i + 1) + " ");

                    // Case 5: Declarative Statements (DL)
                    for (int i = 0; i < DL.length; i++)
                        if (token.equals(DL[i]))
                            ic.print("DL(" + (i + 1) + ") ");

                    // Case 6: Symbol Detection
                    if (token.length() == 1 && st.hasMoreTokens()) {
                        ic.print(token + " ");
                        symbolAddr[symCount] = address;
                        symbols[symCount++] = token;
                    }

                    // Case 7: Literal Detection (=5, =1, etc.)
                    if (token.charAt(0) == '=') {
                        ic.print("L" + litCount + " ");
                        literals[litCount] = token;
                        litCount++;
                    }

                    // Case 8: DS Handling (reserve memory)
                    if (token.equals("DS")) {
                        int val = Integer.parseInt(st.nextToken());
                        address += val - 1;
                    }

                    if (!st.hasMoreTokens())
                        ic.println();
                }

                address++;
            }

            // Step 3: Write Symbol Table
            stFile.println("Index\tSymbol\tAddress");
            for (int i = 0; i < symCount; i++)
                stFile.println(i + "\t" + symbols[i] + "\t" + symbolAddr[i]);

            // Step 4: Write Literal Table
            ltFile.println("Index\tLiteral\tAddress");
            for (int i = 0; i < litCount; i++) {
                literalAddr[i] = address++;
                ltFile.println(i + "\t" + literals[i] + "\t" + literalAddr[i]);
            }

            // Step 5: Close all files
            ic.close();
            stFile.close();
            ltFile.close();
            br.close();

            System.out.println("✅ Pass-I Completed Successfully!");
            System.out.println("Generated: IM.txt, ST.txt, LT.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

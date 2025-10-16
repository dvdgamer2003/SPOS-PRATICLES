import java.util.*;
import java.io.*;

public class MacroPass1 {
    public static void main(String[] args) {
        try {
            // --- Step 1: Open all files for reading and writing ---
            BufferedReader br = new BufferedReader(new FileReader("Input.txt")); // Source program
            PrintWriter mntWriter = new PrintWriter(new File("mnt.txt")); // Macro Name Table
            PrintWriter mdtWriter = new PrintWriter(new File("mdt.txt")); // Macro Definition Table
            PrintWriter argWriter = new PrintWriter(new File("adt.txt")); // Argument List Array (ALA)

            String line;
            boolean inMacro = false; // Flag to check if we are inside a macro definition
            int mntIndex = 1;        // Used to track MDT index
            int macroCount = 0;      // Number of macros found
            int argCount = 0;        // Number of arguments in the current macro

            // Tables to store macro data temporarily
            String[] MNT = new String[10];  // Macro Name Table
            // Removed unused MDT array declaration
            String[] AR = new String[20];   // Argument List (e.g., &ARG1, &ARG2)
            int[] macroIndex = new int[10]; // Stores MDT index for each macro

            // --- Step 2: Read the source program line by line ---
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " ");
                String first = st.nextToken();

                // --- Step 3: When MACRO keyword is found ---
                if (first.equals("MACRO")) {
                    inMacro = true; // Now we are reading macro definition
                    String macroName = st.nextToken(); // e.g. INCR or SUM
                    MNT[macroCount] = macroName;       // Store name in MNT
                    macroIndex[macroCount] = mntIndex; // Store starting MDT index

                    // Write entries into tables
                    mntWriter.println(macroName + "\t" + macroIndex[macroCount]);
                    mdtWriter.println(macroName);
                    argWriter.println(macroName);
                    macroCount++;

                    // --- Step 4: Read arguments of macro if present ---
                    if (st.hasMoreTokens()) {
                        String macroArgs = st.nextToken(); // e.g. &A,&B
                        StringTokenizer argTokens = new StringTokenizer(macroArgs, ",");
                        argCount = 0;

                        while (argTokens.hasMoreTokens()) {
                            String arg = argTokens.nextToken();
                            if (arg.charAt(0) == '&') { // Arguments always start with '&'
                                AR[argCount] = arg;
                                argWriter.println(AR[argCount]); // Store argument
                                argCount++;
                            }
                        }
                    }
                }

                // --- Step 5: Process lines inside the macro body ---
                else if (inMacro) {
                    if (line.equals("MEND")) {
                        // End of macro definition
                        inMacro = false;
                        mdtWriter.println("MEND");
                    } else {
                        // Replace formal arguments (&ARG) with positional names (AR0, AR1, etc.)
                        StringTokenizer tokens = new StringTokenizer(line, " ");
                        while (tokens.hasMoreTokens()) {
                            String token = tokens.nextToken();
                            boolean isArg = false;

                            // Check if token is an argument
                            for (int i = 0; i < argCount; i++) {
                                if (token.equals(AR[i])) {
                                    mdtWriter.print("AR" + i + " "); // Replace &A → AR0
                                    isArg = true;
                                    break;
                                }
                            }

                            // Otherwise, write it as is (instruction/label)
                            if (!isArg)
                                mdtWriter.print(token + " ");

                            // Move to next line if tokens finished
                            if (!tokens.hasMoreTokens())
                                mdtWriter.println();
                        }
                    }
                }

                // Increase MDT index for each line read
                mntIndex++;
            }

            // --- Step 6: Close all files ---
            br.close();
            mntWriter.close();
            mdtWriter.close();
            argWriter.close();

            System.out.println("✅ Pass-I Completed — Tables created: MNT, MDT, ADT.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

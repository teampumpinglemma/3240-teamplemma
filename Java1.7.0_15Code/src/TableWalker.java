import java.io.*;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Parses the input file into tokens
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/11/13
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableWalker {
    DFATable dfaTable;
    // reads the input file
    BufferedReader inputReader;
    // what is left to read on the current line
    String toRead;
    // a marker to help return to what was left to read on the current line after the most recent accept state
    String markToRead;
    // used to output what characters resulted in the token
    String beenRead;
    // the length of toRead that resulted in the accept state
    int matchLength;
    // a counter to help come up with matchLength
    int i;
    // the token corresponding with the most recent accept state
    Definition mostRecentHit;
    // writes to the output file
    BufferedWriter outputWriter;
    Stack<Character> dontTrim;
    LinkedList<DefinitionWithCharacters> parsedTokens;

    /**
     * Walks through the dfaTable using the characters from inputFile, and writes the resulting tokens to outputFile
     * @param dfaTable
     * @param inputFile
     * @param outputFile
     */
    public TableWalker(DFATable dfaTable, File inputFile, File outputFile) {
        try {
            this.dfaTable = dfaTable;
            inputReader = new BufferedReader(new FileReader(inputFile));
            outputWriter = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()));
            dontTrim = new Stack<Character>();
            parsedTokens = new LinkedList<DefinitionWithCharacters>();
            toRead = inputReader.readLine();
            // while not at the end of the input file
            while (toRead != null) {
                matchLength = 0;
                i = 0;
                // get rid of all whitespace in the line
                //toRead = toRead.replaceAll("\\s", "");
                beenRead = toRead;
                char previous = ' ';
                // while not yet at the end of the line
                while (toRead.length() > 0) {
                    if (dontTrim.empty()) {
                        toRead = toRead.trim();
                        beenRead = beenRead.trim();
                    }
                    char c = toRead.charAt(0);
                    if (previous != '\\' && (c == '\'' || c == '\"')) {
                        if (!dontTrim.empty()) {
                            if (c == dontTrim.peek()) {
                                dontTrim.pop();
                            }
                            else {
                                dontTrim.push(c);
                            }
                        }
                        else {
                            dontTrim.push(c);
                        }
                    }
                    toRead = toRead.substring(1);
                    i++;
                    // take a step in the dfaTable
                    this.dfaTable.progress(c);
                    // if it's an accept state, then mark it
                    if (this.dfaTable.tryAccept() != null) {
                        markToRead = toRead;
                        mostRecentHit = this.dfaTable.tryAccept();
                        matchLength = i;
                    }
                    // if the end of the line is reached
                    if ((toRead != null) && ((toRead.length() == 0) || (dontTrim.empty() && toRead.charAt(0) == ' '))) {
                        DefinitionWithCharacters dwc = new DefinitionWithCharacters(mostRecentHit, beenRead.substring(0, matchLength));
                        // write the most recently accepted token to the output file
                        outputWriter.write(dwc.definition.name + " " + dwc.characters);
                        outputWriter.newLine();
			System.out.println(dwc.characters);
                        parsedTokens.add(dwc);
                        // reset toRead to the character right after the most recently accepted
                        toRead = markToRead;
                        beenRead = toRead;
                        markToRead = null;
                        // reset the current state in the dfaTable to the start state
                        dfaTable.currentDFAState = dfaTable.tableRows.get(0);
                        i = 0;
                    }
                    // if no token was matched by the end of the line
                    if (toRead == null) {
                        System.out.println("Ignore previous line... Could not match any token");
                        System.exit(0);
                    }
                    previous = c;
                }
                toRead = inputReader.readLine();
            }
            outputWriter.close();
        }
        catch (Exception e) {
            System.out.println("Could not read input file");
            System.exit(0);
        }
    }

    public DefinitionWithCharacters peekToken() {
        return parsedTokens.getFirst();
    }

    public void matchToken(String expected) {
        DefinitionWithCharacters dwc = parsedTokens.remove();
        String received = dwc.definition.name;
        if (!received.equals(expected)) {
            throwError(received, expected);
        }
    }

    public void throwError(String received, String... expected) {
        System.out.println("Generated scanner token-matching error:");
        if (received != null) {
            System.out.print("Got " + received + " ...expecting ");
        }
        else {
            System.out.print("Got null ...expecting ");
        }
        for (int i = 0; i < expected.length - 1; i++) {
            System.out.print(expected[i].toString() + " or ");
        }
        System.out.println(expected[expected.length - 1]);
        System.exit(0);
    }
}

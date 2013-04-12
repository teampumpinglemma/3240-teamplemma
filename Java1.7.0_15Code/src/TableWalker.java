import java.io.*;

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
            toRead = inputReader.readLine();
            // while not at the end of the input file
            while (toRead != null) {
                matchLength = 0;
                i = 0;
                // get rid of all whitespace in the line
                toRead = toRead.replaceAll("\\s", "");
                beenRead = toRead;
                // while not yet at the end of the line
                while (toRead.length() > 0) {
                    char c = toRead.charAt(0);
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
                    if (toRead != null && toRead.length() == 0) {
                        // write the most recently accepted token to the output file
                        outputWriter.write(mostRecentHit.name + " " + beenRead.substring(0, matchLength));
                        outputWriter.newLine();
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
}

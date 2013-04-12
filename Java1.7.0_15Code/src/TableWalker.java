import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/11/13
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableWalker {
    DFATable dfaTable;
    BufferedReader inputReader;
    String toRead;
    String markToRead;
    String beenRead;
    int matchLength;
    int i;
    Definition mostRecentHit;
    BufferedWriter outputWriter;
    public TableWalker(DFATable dfaTable, File inputFile, File outputFile) {
        try {
            this.dfaTable = dfaTable;
            inputReader = new BufferedReader(new FileReader(inputFile));
            outputWriter = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()));
            toRead = inputReader.readLine();
            while (toRead != null) {
                matchLength = 0;
                i = 0;
                toRead = toRead.replaceAll("\\s", "");
                beenRead = toRead;
                while (toRead.length() > 0) {
                    char c = toRead.charAt(0);
                    toRead = toRead.substring(1);
                    i++;
                    this.dfaTable.progress(c);
                    this.dfaTable.tryAccept();
                    if (this.dfaTable.tryAccept() != null) {
                        markToRead = toRead;
                        mostRecentHit = this.dfaTable.tryAccept();
                        matchLength = i;
                    }
                    if (toRead != null && toRead.length() == 0) {
                        outputWriter.write(mostRecentHit.name + " " + beenRead.substring(0, matchLength));
                        outputWriter.newLine();
                        toRead = markToRead;
                        beenRead = toRead;
                        markToRead = null;
                        dfaTable.currentDFAState = dfaTable.tableRows.get(0);
                        i = 0;
                    }
                    if (toRead == null) {
                        System.out.println("Could not match token :'(");
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

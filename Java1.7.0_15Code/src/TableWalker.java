import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
    Definition mostRecentHit;
    public TableWalker(DFATable dfaTable, File inputFile) {
        try {
            this.dfaTable = dfaTable;
            inputReader = new BufferedReader(new FileReader(inputFile));
            toRead = inputReader.readLine();
            while (toRead != null) {
                while (toRead.length() > 0) {
                    toRead = toRead.trim();
                    char c = toRead.charAt(0);
                    toRead = toRead.substring(1);
                    this.dfaTable.progress(c);
                    this.dfaTable.tryAccept();
                    if (this.dfaTable.tryAccept() != null) {
                        markToRead = toRead;
                        mostRecentHit = this.dfaTable.tryAccept();
                    }
                    if (toRead != null && toRead.length() == 0) {
                        System.out.println(mostRecentHit.name);
                        toRead = markToRead;
                        markToRead = null;
                        dfaTable.currentDFAState = dfaTable.tableRows.get(0);
                    }
                    if (toRead == null) {
                        System.out.println("Could not match token :'(");
                        System.exit(0);
                    }
                }
                toRead = inputReader.readLine();
            }
        }
        catch (Exception e) {
            System.out.println("Could not read input file");
            System.exit(0);
        }
    }
}

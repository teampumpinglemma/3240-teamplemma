import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * The Main class runs everything
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 2/24/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    /**
     * The main method... creates a RegexParser, then parses and builds the NFAs for the Character Classes, then parses and builds the NFAs for the Tokens, then combines the Token NFAs and builds the DFA Table, and then finally scans the input file for tokens and writes them to the output file
     *
     * @param args :args[0] pass in the absolute path to the Spec file, args[1] pass in the absolute path to the Input file, args[2] pass in the absolute path to the TableOutput file, args[3] pass in the absolute path to the TokenOutput file (file does not have to exist yet)
     */
    public static void main(String[] args) {
        RegexParser regexParser = new RegexParser(new File(args[0]));
        regexParser.parseCharClasses();
        regexParser.parseTokens();
        regexParser.buildDFATable();
        // write DFA Table to file
        try {
            BufferedWriter tableWriter = new BufferedWriter(new FileWriter(new File(args[2])));
            for (int i = 0; i < regexParser.dfaTable.tableRows.size(); i++) {
                tableWriter.write("State" + i);
                tableWriter.newLine();
                for (int j = 0; j < 95; j++) {
                    //if (regexParser.dfaTable.tableRows.get(i).nextStates[j] != 1) {
                        tableWriter.write("   " + (char)(j + 32) + " " + regexParser.dfaTable.tableRows.get(i).nextStates[j]);
                    //}
                }
                tableWriter.newLine();
            }
            tableWriter.newLine();
            tableWriter.write("Accept States");
            tableWriter.newLine();
            for (int j = 0; j < regexParser.dfaTable.tableRows.size(); j++) {
                for (int i = 0; i < regexParser.dfaTable.nfa.acceptStates.size(); i++) {
                    if (regexParser.dfaTable.tableRows.get(j).nfaStates.contains(regexParser.dfaTable.nfa.acceptStates.get(i))) {
                        tableWriter.write(" " + j);
                    }
                }
            }
            tableWriter.close();
        }
        catch (Exception e) {
            System.out.println("Could not write to table file");
            System.exit(0);
        }
        TableWalker tw = new TableWalker(regexParser.dfaTable, new File(args[1]), new File(args[3]));
        System.out.println("Success! Check your output files!");

        LL1GrammarParser parser = new LL1GrammarParser(new File(args[4]), regexParser.specReader.tokens);
        parser.parseGrammar();


        System.out.println("Creating First and Follow Sets: ");

        LL1FFSets sets = new LL1FFSets(parser.rules);

        System.out.println("Working on the Parsing Table");

        LL1ParsingTable table = new LL1ParsingTable(sets, regexParser.specReader.tokens,tw.parsedTokens, parser.rules);
        //System.out.println(table.run());
        System.out.println("BAM!!");


    }
}

import java.io.File;

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
     * @param args :args[0] pass in the absolute path to the Spec file, args[1] pass in the absolute path to the Input file, args[2] pass in the absolute path to the Output file (file does not have to exist yet)
     */
    public static void main(String[] args) {
        RegexParser regexParser = new RegexParser(new File(args[0]));
        regexParser.parseCharClasses();
        regexParser.parseTokens();
        regexParser.buildDFATable();
        new TableWalker(regexParser.dfaTable, new File(args[1]), new File(args[2]));
        System.out.println("Success! Check your output file!");
        /*
        System.out.println("Char Classes");
        for (int i = 0; i < regexParser.specReader.defined.size(); i++) {
            System.out.println(regexParser.specReader.defined.get(i).name);
            System.out.print("\t");
            for (int j = 0; j < regexParser.specReader.defined.get(i).tokens.size(); j++) {
                System.out.print(regexParser.specReader.defined.get(i).tokens.get(j).token.toString() + " " + regexParser.specReader.defined.get(i).tokens.get(j).characters + "   ");
            }
            System.out.println();
        }
        System.out.println("\nTokens");
        for (int i = 0; i < regexParser.specReader.tokens.size(); i++) {
            System.out.println(regexParser.specReader.tokens.get(i).name);
            System.out.print("\t");
            for (int j = 0; j < regexParser.specReader.tokens.get(i).tokens.size(); j++) {
                System.out.print(regexParser.specReader.tokens.get(i).tokens.get(j).token.toString() + " " + regexParser.specReader.tokens.get(i).tokens.get(j).characters + "   ");
            }
            System.out.println();
        }

        System.out.println("\nBuilt Character Classes");
        for (int i = 0; i < regexParser.characterClasses.size(); i++) {
            System.out.println(regexParser.characterClasses.get(i).name);
            boolean[] accepted = regexParser.characterClasses.get(i).accepted;
            for (int j = 0; j < accepted.length; j++) {
                if (accepted[j]) {
                    System.out.print(" " + (char)(j + 32));
                }
            }
            System.out.println();
        }
        System.out.println("\nDFA Table");
        for (int i = 0; i < regexParser.dfaTable.tableRows.size(); i++) {
            System.out.println(i);
            for (int j = 0; j < regexParser.dfaTable.tableRows.get(i).nfaStates.size(); j++) {
                System.out.print(regexParser.dfaTable.tableRows.get(i).nfaStates.get(j).toString() + " ");
            }
            System.out.println();
            for (int j = 0; j < 95; j++) {
                if (regexParser.dfaTable.tableRows.get(i).nextStates[j] != 1) {
                    System.out.print("   " + (char)(j + 32) + " " + regexParser.dfaTable.tableRows.get(i).nextStates[j]);
                }
            }
            System.out.println();
        }
        System.out.println("Accept");
        for (int j = 0; j < regexParser.dfaTable.tableRows.size(); j++) {
            for (int i = 0; i < regexParser.dfaTable.nfa.acceptStates.size(); i++) {
                if (regexParser.dfaTable.tableRows.get(j).nfaStates.contains(regexParser.dfaTable.nfa.acceptStates.get(i))) {
                    System.out.print(" " + j);
                }
            }
        }
        */
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * The RegexParser class parses the regex on each line of the Spec file by
 * 1. feeding that line to a SpecReader and then
 * 2. parsing the regex using a recursive descent parser which
 * 3. requests tokens from the SpecReader to decide how to parse
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 2/24/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegexParser {

    SpecReader specReader;
    BufferedReader br;
    // just for checking if there are still lines in the Spec file
    BufferedReader checker;
    ArrayList<CharacterClass> characterClasses;
    ArrayList<NFA> charClassNFAs;
    ArrayList<NFA> tokenNFAs;
    Stack<NFA> inProgressNFAs;
    ArrayList<DFATable> dfaTables;

    /**
     * Constructor creates two BufferedReaders for the same Spec file,
     * one for the SpecReader and one just to check for the end of the file,
     * and creates a SpecReader
     * @param spec :the Spec file
     */
    public RegexParser(File spec) {
        try {
            br = new BufferedReader(new FileReader(spec));
            checker = new BufferedReader(new FileReader(spec));
            specReader = new SpecReader(br);
        }
        catch (Exception e) {
            System.out.println("Regex parser construction error");
            System.exit(0);
        }
    }

    /**
     * Parses the entire Spec file
     */
    public void parseCharClasses() {
        try {
            // while still on char classes
            while (specReader.set_up_new_line()) {
                checker.readLine();
                // parse through the regex using a recursive descent parser
                reg_ex(false);
                // if the recursive descent parser finishes, then make sure the entire regex has been parsed, otherwise throw an error
                specReader.matchToken(SpecReader.terminal.END);
                // if the recursive descent parser finishes and the entire regex has been parsed, then the parse for that line was successful
                // System.out.println("Parse successful!");
            }
            checker.readLine();
        }
        catch (Exception E) {
            System.out.println("readLine error");
            System.exit(0);
        }
        buildCharClassNFAs();
    }

    public void parseTokens() {
        try {
            tokenNFAs = new ArrayList<NFA>();
            String l = checker.readLine();
            while (l != null) {
                specReader.set_up_new_line();
                // parse through the regex using a recursive descent parser
                reg_ex(true);
                // if the recursive descent parser finishes, then make sure the entire regex has been parsed, otherwise throw an error
                specReader.matchToken(SpecReader.terminal.END);
                // if the recursive descent parser finishes and the entire regex has been parsed, then the parse for that line was successful
                // System.out.println("Parse successful!");
                l = checker.readLine();
            }
        }
        catch (Exception E) {
            System.out.println("readLine error");
            System.exit(0);
        }
    }

    public void buildDFATables() {
        dfaTables = new ArrayList<DFATable>();
        for (int i = 0; i < tokenNFAs.size(); i++) {
            dfaTables.add(new DFATable(tokenNFAs.get(i)));
        }
        System.out.println();
    }

    /**
     * The root of the recursive descent parser
     */
    private void reg_ex(boolean tokenTime) {
        inProgressNFAs = new Stack<NFA>();
        inProgressNFAs.push(new NFA());
        rexp();
        if (tokenTime) {
            tokenNFAs.add(inProgressNFAs.pop());
        }
    }

    private void rexp() {
        rexp1();
        rexp_prime();
    }

    private void rexp_prime() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.UNION) {
            specReader.matchToken(token);
            inProgressNFAs.push(new NFA());
            rexp1();
            rexp_prime();
            NFA after = inProgressNFAs.pop();
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(unionNFAs(before, after));
            System.out.println();
        }
        else {
            return;
        }
    }

    private void rexp1() {
        rexp2();
        rexp1_prime();
    }

    private void rexp1_prime() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PAR_OPEN || token == SpecReader.terminal.RE_CHAR || token == SpecReader.terminal.PERIOD || token == SpecReader.terminal.SQUARE_OPEN || token == SpecReader.terminal.DEFINED) {
            rexp2();
            rexp1_prime();
        }
        else {
            return;
        }
    }

    private void rexp2() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PAR_OPEN) {
            specReader.matchToken(token);
            inProgressNFAs.push(new NFA());
            rexp();
            specReader.matchToken(SpecReader.terminal.PAR_CLOSE);
            rexp2_tail();
            NFA after = inProgressNFAs.pop();
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(concatNFAs(before, after));
            System.out.println();
        }
        else if (token == SpecReader.terminal.RE_CHAR) {
            specReader.matchToken(token);
            CharacterClass cc = new CharacterClass("");
            cc.setToTrue(specReader.tokens.get(specReader.tokens.size() - 1).tokens.get(specReader.tokens.get(specReader.tokens.size() - 1).tokens.size() - 1).characters.charAt(specReader.tokens.get(specReader.tokens.size() - 1).tokens.get(specReader.tokens.get(specReader.tokens.size() - 1).tokens.size() - 1).characters.length() - 1));
            inProgressNFAs.push(cc.createNFA());
            rexp2_tail();
            NFA after = inProgressNFAs.pop();
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(concatNFAs(before, after));
            System.out.println();
        }
        else {
            rexp3();
        }
    }

    private void rexp2_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.STAR) {
            specReader.matchToken(token);
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(starNFA(before));
            System.out.println();
        }
        else if (token == SpecReader.terminal.PLUS) {
            specReader.matchToken(token);
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(plusNFA(before));
            System.out.println();
        }
        else {
            return;
        }
    }

    private void rexp3() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PERIOD || token == SpecReader.terminal.SQUARE_OPEN || token == SpecReader.terminal.DEFINED) {
            char_class();
        }
        else {
            return;
        }
    }

    private void char_class() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PERIOD) {
            specReader.matchToken(token);
            CharacterClass cc = new CharacterClass("");
            cc.setToTrue(' ', '~');
            inProgressNFAs.push(cc.createNFA());
            NFA after = inProgressNFAs.pop();
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(concatNFAs(before, after));
            System.out.println();
        }
        else if (token == SpecReader.terminal.SQUARE_OPEN) {
            specReader.matchToken(token);
            char_class1();
        }
        else if (token == SpecReader.terminal.DEFINED) {
            specReader.matchToken(token);
            int i = 0;
            for (i = 0; i < specReader.defined.size(); i++) {
                if (specReader.tokens.get(specReader.tokens.size() - 1).tokens.get(specReader.tokens.get(specReader.tokens.size() - 1).tokens.size() - 1).characters.equals(specReader.defined.get(i).name)) {
                    break;
                }
            }
            NFA after = characterClasses.get(i).createNFA();
            NFA before = inProgressNFAs.pop();
            inProgressNFAs.push(concatNFAs(before, after));
            System.out.println();
        }
        else {
            specReader.throwError(token, SpecReader.terminal.PERIOD, SpecReader.terminal.SQUARE_OPEN, SpecReader.terminal.DEFINED);
        }
    }

    private void char_class1() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.CLS_CHAR || token == SpecReader.terminal.SQUARE_CLOSE) {
            char_set_list();
        }
        else if (token == SpecReader.terminal.CARROT) {
            exclude_set();
        }
        else {
            specReader.throwError(token, SpecReader.terminal.CLS_CHAR, SpecReader.terminal.SQUARE_CLOSE, SpecReader.terminal.CARROT);
        }
    }

    private void char_set_list() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.CLS_CHAR) {
            char_set();
            char_set_list();
        }
        else if (token == SpecReader.terminal.SQUARE_CLOSE) {
            specReader.matchToken(token);
        }
        else {
            specReader.throwError(token, SpecReader.terminal.CLS_CHAR, SpecReader.terminal.SQUARE_CLOSE);
        }
    }

    private void char_set() {
        specReader.matchToken(SpecReader.terminal.CLS_CHAR);
        char_set_tail();
    }

    private void char_set_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.DASH) {
            specReader.matchToken(token);
            specReader.matchToken(SpecReader.terminal.CLS_CHAR);
        }
        else {
            return;
        }
    }

    private void exclude_set() {
        specReader.matchToken(SpecReader.terminal.CARROT);
        char_set();
        specReader.matchToken(SpecReader.terminal.SQUARE_CLOSE);
        specReader.matchToken(SpecReader.terminal.IN);
        exclude_set_tail();
    }

    private void exclude_set_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.SQUARE_OPEN) {
            specReader.matchToken(token);
            char_set();
            specReader.matchToken(SpecReader.terminal.SQUARE_CLOSE);
        }
        else if (token == SpecReader.terminal.DEFINED) {
            specReader.matchToken(token);
        }
        else {
            specReader.throwError(token, SpecReader.terminal.SQUARE_OPEN, SpecReader.terminal.DEFINED);
        }
    }

    public void buildCharClassNFAs() {
        // Stores the built character classes
        characterClasses = new ArrayList<CharacterClass>();
        // For every character class that was parsed earlier
        for (int i = 0; i < specReader.defined.size(); i++) {
            Definition definition = specReader.defined.get(i);
            // Set up a new character class
            characterClasses.add(new CharacterClass(definition.name));
            // All character classes should start with a SQUARE_OPEN
            if (definition.tokens.get(0).token == SpecReader.terminal.SQUARE_OPEN) {
                // If it's an exclude set
                if (definition.tokens.get(1).token == SpecReader.terminal.CARROT) {
                    CharacterClass not = new CharacterClass("");
                    // The current token (starting with the one after the carrot)
                    int j = 2;
                    // While still inside square brackets
                    while (definition.tokens.get(j).token != SpecReader.terminal.SQUARE_CLOSE) {
                        // If the next token is a dash, then a range of characters is being defined
                        if (definition.tokens.get(j + 1).token == SpecReader.terminal.DASH) {
                            not.setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length()- 1), definition.tokens.get(j + 2).characters.charAt(definition.tokens.get(j + 2).characters.length() - 1));
                            j += 3;
                        }
                        // Else a single character is being defined
                        else {
                            not.setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length() - 1));
                            j += 1;
                        }
                    }
                    j += 2;
                    CharacterClass in = null;
                    // If the character class following the IN token is a previously defined character class
                    if (definition.tokens.get(j).token == SpecReader.terminal.DEFINED) {
                        // Find which one it is
                        for (int k = 0; k < characterClasses.size() - 1; k++) {
                            if (characterClasses.get(k).name.equals(definition.tokens.get(j).characters)) {
                                in = characterClasses.get(k);
                                break;
                            }
                        }
                    }
                    // Else it's a another set of square brackets
                    else {
                        in = new CharacterClass("");
                        j += 1;
                        // While still inside square brackets
                        while (definition.tokens.get(j).token != SpecReader.terminal.SQUARE_CLOSE) {
                            // If the next token is a dash, then a range of characters is being defined
                            if (definition.tokens.get(j + 1).token == SpecReader.terminal.DASH) {
                                in.setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length()- 1), definition.tokens.get(j + 2).characters.charAt(definition.tokens.get(j + 2).characters.length() - 1));
                                j += 3;
                            }
                            // Else a single character is being defined
                            else {
                                in.setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length() - 1));
                                j += 1;
                            }
                        }
                    }
                    characterClasses.get(i).notBlankInBlank(not, in);
                }
                // Else it's a normal set of square brackets with characters to include
                else {
                    // The current token (starting with the one after the SQUARE_OPEN)
                    int j = 1;
                    // While still inside square brackets
                    while (definition.tokens.get(j).token != SpecReader.terminal.SQUARE_CLOSE) {
                        // If the next token is a dash, then a range of characters is being defined
                        if (definition.tokens.get(j + 1).token == SpecReader.terminal.DASH) {
                            characterClasses.get(i).setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length()- 1), definition.tokens.get(j + 2).characters.charAt(definition.tokens.get(j + 2).characters.length() - 1));
                            j += 3;
                        }
                        // Else a single character is being defined
                        else {
                            characterClasses.get(i).setToTrue(definition.tokens.get(j).characters.charAt(definition.tokens.get(j).characters.length() - 1));
                            j += 1;
                        }
                    }
                }
            }
            // Else throw an error
            else {
                System.out.println("Not a defined class");
                System.exit(0);
            }
        }
        charClassNFAs = new ArrayList<NFA>();
        for (int i = 0; i < characterClasses.size(); i++) {
            charClassNFAs.add(characterClasses.get(i).createNFA());
        }
    }

    public NFA concatNFAs(NFA n1, NFA n2) {
        NFA nfa = new NFA();
        nfa.states.remove(0);
        nfa.start = n1.start;
        for (int i = 0; i < n1.transitions.size(); i++) {
            nfa.transitions.add(n1.transitions.get(i));
        }
        for (int i = 0; i < n1.states.size(); i++) {
            nfa.states.add(n1.states.get(i));
        }
        nfa.transitions.add(new Transition(n1.accept, null, true, n2.start));
        for (int i = 0; i < n2.transitions.size(); i++) {
            nfa.transitions.add(n2.transitions.get(i));
        }
        for (int i = 0; i < n2.states.size(); i++) {
            nfa.states.add(n2.states.get(i));
        }
        nfa.accept = n2.accept;
        return nfa;
    }

    public NFA unionNFAs(NFA n1, NFA n2) {
        NFA nfa = new NFA();
        nfa.transitions.add(new Transition(nfa.start, null, true, n1.start));
        nfa.transitions.add(new Transition(nfa.start, null, true, n2.start));
        for (int i = 0; i < n1.transitions.size(); i++) {
            nfa.transitions.add(n1.transitions.get(i));
        }
        for (int i = 0; i < n1.states.size(); i++) {
            nfa.states.add(n1.states.get(i));
        }
        for (int i = 0; i < n2.transitions.size(); i++) {
            nfa.transitions.add(n2.transitions.get(i));
        }
        for (int i = 0; i < n2.states.size(); i++) {
            nfa.states.add(n2.states.get(i));
        }
        nfa.accept = new State();
        nfa.states.add(nfa.accept);
        nfa.transitions.add(new Transition(n1.accept, null, true, nfa.accept));
        nfa.transitions.add(new Transition(n2.accept, null, true, nfa.accept));
        return nfa;
    }

    public NFA starNFA(NFA n1) {
        NFA nfa = new NFA();
        nfa.states.remove(0);
        nfa.start = n1.start;
        for (int i = 0; i < n1.transitions.size(); i++) {
            nfa.transitions.add(n1.transitions.get(i));
        }
        for (int i = 0; i < n1.states.size(); i++) {
            nfa.states.add(n1.states.get(i));
        }
        nfa.transitions.add(new Transition(n1.accept, null, true, nfa.start));
        nfa.accept = nfa.start;
        return nfa;
    }

    public NFA plusNFA(NFA n1) {
        NFA nfa = new NFA();
        nfa.states.remove(0);
        nfa.start = n1.start;
        for (int i = 0; i < n1.transitions.size(); i++) {
            nfa.transitions.add(n1.transitions.get(i));
        }
        for (int i = 0; i < n1.states.size(); i++) {
            nfa.states.add(n1.states.get(i));
        }
        nfa.transitions.add(new Transition(n1.accept, null, true, nfa.start));
        nfa.accept = n1.accept;
        return nfa;
    }
}

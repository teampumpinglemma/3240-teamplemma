import java.io.File;
import java.util.ArrayList;

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
     * The main method... creates a RegexParser, and then parses the Spec file, and then builds the character classes
     *
     * @param args :pass in the Spec file... to do this in IntelliJ, go to Run -> Edit Configurations... and then put the path to the file in "Program arguments"
     */
    public static void main(String[] args) {
        RegexParser regexParser = new RegexParser(new File(args[0]));
        regexParser.parse();

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

        // Stores the built character classes
        ArrayList<CharacterClass> characterClasses = new ArrayList<CharacterClass>();
        // For every character class that was parsed earlier
        for (int i = 0; i < regexParser.specReader.defined.size(); i++) {
            Definition definition = regexParser.specReader.defined.get(i);
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
        System.out.println("\nBuilt Character Classes");
        for (int i = 0; i < characterClasses.size(); i++) {
            System.out.println(characterClasses.get(i).name);
            boolean[] accepted = characterClasses.get(i).accepted;
            for (int j = 0; j < accepted.length; j++) {
                if (accepted[j]) {
                    System.out.print(" " + (char)(j + 32));
                }
            }
            System.out.println();
        }
    }
}

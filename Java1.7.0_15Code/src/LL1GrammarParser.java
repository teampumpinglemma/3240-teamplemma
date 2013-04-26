import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/25/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class LL1GrammarParser {

    BufferedReader buffReader, checker;
    SpecReader specReader;
    ArrayList<String> rules, nonTerminals;
    String currentLine, currentRule, identifier;
    boolean startedLine = false;
    LinkedList<DefinitionWithCharacters> parsedTokens;
    boolean stopParsingLine = false;

    /**
     * This file will be used to read the grammar from the specified input file.
     */
    public LL1GrammarParser(File grammarFile, LinkedList<DefinitionWithCharacters> parsedTokens)
    {
        try {
            buffReader = new BufferedReader(new FileReader(grammarFile));
            checker = new BufferedReader(new FileReader(grammarFile));
            rules = new ArrayList<String>();
            nonTerminals = new ArrayList<String>();
            this.parsedTokens = parsedTokens;

            for (int i = 0; i < parsedTokens.size(); i++)
            {
                for (int j = 0; j < parsedTokens.get(i).definition.tokens.size(); j++)
                {
                    System.out.println(this.parsedTokens.get(i).definition.name + "   " + parsedTokens.get(i).definition.name);
                    System.out.println(this.parsedTokens.get(i).definition.tokens.get(j).token + "   " + parsedTokens.get(i).definition.tokens.get(j).token);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Grammar parser construction error");
            System.exit(0);
        }
    }

    public void parseGrammar()
    {
        // Need list of terminals (tokens) and non-terminals
        try
        {
            currentLine = buffReader.readLine();
        }
        catch(Exception ex)
        {
            System.out.println("Error reading file");
            System.exit(0);
        }

        // Get rid of leading and trailing white space on line
        currentLine = currentLine.trim();

        // If line is not empty
        if ((currentLine.length() > 0) && (stopParsingLine == false))
        {
            // Read in next "word"
            String word = currentLine.substring(0, currentLine.indexOf(' '));

            // Check if word is non-terminal
            if (word.substring(0, 1).equals("<"))
            {
                // New rule for the new line
                currentRule = word;

                if ((!nonTerminals.isEmpty()) && (!nonTerminals.contains(word)))
                {
                    nonTerminals.add(word);
                }
            }
            else
            {
                // Throw error
                System.out.println("Must start rule with <non-terminal>");
                System.exit(0);
            }

            // Get next "word"
            currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
            currentLine.trim();
            word = currentLine.substring(0, currentLine.indexOf(" "));
            word.trim();

            if (word.equals("::="))
            {
                // Current rule is new identifier
                currentRule = currentRule + " " + word;
                identifier = currentRule;
            }
            else
            {
                // Throw error
                System.out.println("Rule must contain ::= in proper location");
                System.exit(0);
            }

            // Get next "word"
            currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
            currentLine.trim();
            word = currentLine.substring(0, currentLine.indexOf(" "));
            word.trim();

            while ((word.length() > 0) && (stopParsingLine == false))
            {
                // Check if current word is terminal or non-terminal
                if (word.substring(0, 1).equals("<"))
                {
                    // Non-terminal
                    currentRule = currentRule + " " + word;

                    if (!nonTerminals.contains(word))
                    {
                        nonTerminals.add(word);
                    }
                }
                else if (word.equals("|"))
                {
                    // Add next terminals/non-terminals to rules ArrayList (unless there are more |'s or end of line)
                    if (!currentRule.equals(""))
                    {
                        rules.add(currentRule);
                    }

                    currentRule = identifier;
                }
                else // Terminal
                {
                    boolean matched = false;
                    for (int i = 0; i < parsedTokens.size(); i++) {
                        if (parsedTokens.get(i).definition.name.toUpperCase().equals("$" + word.toUpperCase())) {
                            matched = true;
                            break;
                        }
                    }
                    if (matched)
                    {
                        currentRule = currentRule + " " + word;
                    }
                    else
                    {
                        // Throw error
                        System.out.println("Terminal not recognized: " + word);
                        System.exit(0);
                    }
                }

                // Get next "word"
                try
                {
                    currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
                    System.out.println(currentLine);
                    currentLine.trim();
                    word = currentLine.substring(0, currentLine.indexOf(" "));
                    System.out.println(word);
                    word.trim();

                    if (word.equals("end"))
                    {
                        stopParsingLine = true;
                        word = "";
                        currentLine = "";
                    }
                }
                catch (Exception ex)
                {
                    stopParsingLine = true;
                    word = "";
                    currentLine = "";
                }
            }

            // Add final rule on the line
            rules.add(currentRule);

            // Go to next line because there are no more "words" left on line
            parseGrammar();
        }

        for (int i = 0; i < rules.size(); i++)
        {
            System.out.println(rules.get(i));
        }
    }
}

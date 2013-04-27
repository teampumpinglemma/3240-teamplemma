import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/25/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LL1ParsingTable {

    ArrayList<grammarRules> rules;
    ArrayList<parserSet> FIRST, FOLLOW;
    boolean addNewRule, addFirstWord;

    /**
     * This will be the file that creates the LL(1) Parsing Table, the FIRST sets, and the FOLLOW sets.
     */
    public LL1ParsingTable(ArrayList<String> notFormatted)
    {
        addNewRule = true;
        addFirstWord = true;

        // Change rules into "proper" grammarRules class format
        reformatRules(notFormatted);

        // Create FIRST and FOLLOW sets
        //createFollowSet();
        // Create FIRST set
        createFIRSTset();
    }

    public void createFIRSTset()
    {
        FIRST = new ArrayList<parserSet>();

        for (int i = 0; i < rules.size(); i++)
        {
            parserSet firstSet = new parserSet(rules.get(i).identifier);
            FIRST.add(firstSet);
        }
        for (int i = 0; i < rules.size(); i++)
        {
            createFirstSet(rules.get(i));
        }

        System.out.println("wwooooowww " + FIRST.size());

        // DEBUG PRINT STATEMENT
        /*for (int i = 0; i < FIRST.size(); i++)
        {
            //System.out.println(FIRST.get(i).nonTerminal);
            for (int j = 0; j < FIRST.get(i).set.size(); j++)
            {
                System.out.println(FIRST.get(i).nonTerminal + " " + FIRST.get(i).set.get(j));
            }
        } */
    }

    public void reformatRules(ArrayList<String> notFormatted)
    {
        String identifier = "";
        rules = new ArrayList<grammarRules>();

        for (int i = 0; i < notFormatted.size(); i ++)
        {
            int index = notFormatted.get(i).indexOf("::=");
            identifier = notFormatted.get(i).substring(0, index - 1);

            // Check if identifier is already in ArrayList of grammarRules
            for (int z = 0; z < rules.size(); z++)
            {
                if (rules.get(z).identifier.equals(identifier))
                {
                    // Add rule to that location
                    rules.get(z).rulesList.add(notFormatted.get(i).substring(index + 4));
                    addNewRule = false;
                }
                else
                {
                    addNewRule = true;
                }
            }

            // If not in ArrayList of grammarRule
            if (addNewRule)
            {
                // Add identifier to rulesList
                grammarRules rule = new grammarRules(identifier);

                // Add rule to that location
                rules.add(rule);
                rules.get(rules.size() - 1).rulesList.add(notFormatted.get(i).substring(index + 4));
            }
        }

        /*for (int i = 0; i < rules.size(); i++)
        {
            for (int j = 0; j < rules.get(i).rulesList.size(); j++)
            {
                System.out.println(rules.get(i).identifier + " " + rules.get(i).rulesList.get(j));
            }
        } */
    }

    /**
     * This method creates the FIRST sets for the grammar file.
     */
    public void createFirstSet(grammarRules rule)
    {
        // Get first word from current rule
        for (int i = 0; i < rule.rulesList.size(); i ++)
        {
            int space = rule.rulesList.indexOf(" ");
            String firstWord = rule.rulesList.get(i).substring(space + 1);
            space = firstWord.indexOf(" ");
            if (space >= 0)
            {
                firstWord = firstWord.substring(0, space + 1);
                firstWord.trim();
            }
            else
            {
                firstWord.trim();
            }

            for (int k = 0; k < rules.size(); k ++)
            {
                System.out.println(firstWord + " " + rules.get(k).identifier);
                if (firstWord.equals(rules.get(k).identifier))
                {
                    System.out.println(firstWord + " " + rules.get(k).identifier);
                    addFirstWord = false;
                    createFirstSet(rules.get(k));
                }
            }

            if (addFirstWord)
            {
                // Add firstWord to first set
                for (int k = 0; k < FIRST.size(); k ++)
                {
                    if (rule.identifier.equals(FIRST.get(k).nonTerminal))
                    {
                        FIRST.get(k).set.add(firstWord);
                    }
                }
            }
        }
    }

    /**
     * This method creates the FOLLOW sets for the grammar file.
     */
    public void createFollowSet()
    {
        createFIRSTset();
        // Taken from lecture slides
        for(int i = 0; i < rules.size()){
            FOLLOW.add(rules.get(i));
        }
    }
}

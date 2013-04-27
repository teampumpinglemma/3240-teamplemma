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
    boolean addNewRule;

    /**
     * This will be the file that creates the LL(1) Parsing Table, the FIRST sets, and the FOLLOW sets.
     */
    public LL1ParsingTable(ArrayList<String> notFormatted)
    {
        // Change rules into "proper" grammarRules class format
        reformatRules(notFormatted);

        // Create FIRST and FOLLOW sets
    }

    public void reformatRules(ArrayList<String> notFormatted)
    {
        String identifier = "";
        rules = new ArrayList<grammarRules>();

        for (int i = 0; i < notFormatted.size(); i ++)
        {
            int index = notFormatted.get(i).indexOf("::=");
            identifier = notFormatted.get(i).substring(0, index);

            // Check if identifier is already in ArrayList of grammarRules
            for (int z = 0; z < rules.size(); z++)
            {
                if (rules.get(z).identifier.equals(identifier))
                {
                    // Add rule to that location
                    rules.get(z).rulesList.add(notFormatted.get(i).substring(index + 1));
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
                rules.get(rules.size() - 1).rulesList.add(notFormatted.get(i).substring(index + 1));
            }
        }
    }

    /**
     * This method creates the FIRST sets for the grammar file.
     */
    public void createFirstSet()
    {
        FIRST = new ArrayList<parserSet>();

        // Taken and modified from lecture slides
        for (int i = 0; i < rules.size(); i++)
        {
            parserSet firstSet = new parserSet(rules.get(i).identifier);
            FIRST.add(firstSet);
        }

        for (int i = 0; i < rules.size(); i ++)
        {
            for (int j = 0; j < rules.get(i).rulesList.size(); j++)
            {
                // Get first word

            }
        }
    }

    /**
     * This method creates the FOLLOW sets for the grammar file.
     */
    public void createFollowSet()
    {
        // Taken from lecture slides
    }
}

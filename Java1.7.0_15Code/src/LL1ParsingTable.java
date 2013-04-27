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
        // Change rules into "proper" grammarRules class format
        reformatRules(notFormatted);

<<<<<<< HEAD
<<<<<<< HEAD
        // Create FIRST and FOLLOW sets
        createFollowSet();
=======
=======
>>>>>>> a17df68c480501098cc4e51313098b183404f12e
        // Create FIRST set
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
        for (int i = 0; i < FIRST.size(); i++)
        {
            for (int j = 0; j < FIRST.get(i).set.size(); j++)
            {
                System.out.println(FIRST.get(i).nonTerminal + " " + FIRST.get(i).set.get(j));
            }
        }
<<<<<<< HEAD
>>>>>>> a17df68c480501098cc4e51313098b183404f12e
=======
>>>>>>> a17df68c480501098cc4e51313098b183404f12e
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

            for (int k = 0; k < rules.size(); k ++)
            {
                if (firstWord.equals(rules.get(k).identifier))
                {
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
        //createFirstSet();
        FOLLOW = new ArrayList<parserSet>();
        // Taken from lecture slides
        parserSet start = new parserSet(rules.get(0).identifier);
        start.set.add("$");
        FOLLOW.add(start);
        boolean changes = true;
        for(int i = 1; i < rules.size(); i++){
            FOLLOW.add(new parserSet(rules.get(i).identifier));
        }
        while(changes){
             for(int i = 0; i < rules.size(); i++){
                 
                 for(int j = 0; j < rules.get(i).rulesList.size(); j++){
                     String rl = rules.get(i).rulesList.get(j);
                     String curNT = "";
                     System.out.println(rl);
                     for(int k = 0; k < rl.length();k++){
                         if(rl.charAt(k) == '<'){
                             curNT += rl.charAt(k);
                         }else if(curNT.length() > 0){
                             curNT += rl.charAt(k);
                             if(rl.charAt(k) == '>'){
                                 FOLLOW.get(rules.get(i).identifier)
                         }
                     }
                 }
             }
        }
    }
}

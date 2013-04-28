import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/25/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
/*public class LL1ParsingTable {

    ArrayList<grammarRules> rules;
    ArrayList<parserSet> FIRST, FOLLOW;
    ArrayList<grammarRules> markedForLater;
    ArrayList<String> laterWord;
    boolean addNewRule;

    /**
     * This will be the file that creates the LL(1) Parsing Table, the FIRST sets, and the FOLLOW sets.
     */
/*    public LL1ParsingTable(ArrayList<String> notFormatted)
    {
        addNewRule = true;
        markedForLater = new ArrayList<grammarRules>();
        laterWord = new ArrayList<String>();

        // Change rules into "proper" grammarRules class format
        reformatRulesEmily(notFormatted);

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

        // For generating first set for rules that are recursive
       /* for (int i = 0; i < markedForLater.size(); i++)
        {
            int officialIndex = 0;

            for (int j = 0; j < FIRST.size(); j++)
            {
                // Need to know where current rule is in FIRST
                if (FIRST.get(j).nonTerminal.equals(markedForLater.get(i).identifier))
                {
                    officialIndex = j;
                }
            }

            for (int j = 0; j < FIRST.size(); j++)
            {
                System.out.println(FIRST.get(j).nonTerminal + " " + laterWord.get(i));
                for (int k = 0; k < FIRST.get(j).set.size(); k++)
                {
                    for (int l = 0; l < FIRST.get(officialIndex).set.size(); l++)
                    {
                        if (!(FIRST.get(officialIndex).set.get(l).equals(FIRST.get(j).set.get(k))))
                        {
                            addNewRule = true;
                        }
                        //System.out.print(FIRST.get(j).set.get(k) + " ");
                    }

                    if (addNewRule)
                    {
                        FIRST.get(officialIndex).set.add(FIRST.get(j).set.get(k));
                    }
                }
            }
        }*/

        System.out.println("wwooooowww " + FIRST.size());

        // DEBUG PRINT STATEMENT
        for (int i = 0; i < FIRST.size(); i++)
        {
            //System.out.println(FIRST.get(i).nonTerminal);
            for (int j = 0; j < FIRST.get(i).set.size(); j++)
            {
                System.out.println(FIRST.get(i).nonTerminal + " " + FIRST.get(i).set.get(j));
            }
<<<<<<< HEAD
        } */
/*    }
=======
        }
    }
>>>>>>> 1d95d884eb77fa224418626da148a04906aac35f

    public void reformatRulesEmily(ArrayList<String> notFormatted)
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
  /*  }

    /**
     * This method creates the FIRST sets for the grammar file.
     */
/*    public void createFirstSet(grammarRules rule)
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
                firstWord = firstWord.trim();
            }
            else
            {
                firstWord = firstWord.trim();
            }

            boolean notFound = true;
            for (int k = 0; k < rules.size(); k ++)
            {
                if (firstWord.equals(rules.get(k).identifier))
                {
                    markedForLater.add(rules.get(k));
                    laterWord.add(firstWord);
                    notFound = false;
                }
            }

            if (notFound)
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
/*    public void createFollowSet()
    {
        FOLLOW = new ArrayList<parserSet>();
        createFIRSTset();
        // Taken from lecture slides
<<<<<<< HEAD
        for(int i = 0; i < rules.size()){
            FOLLOW.add(new parserSet(rules.get(i).identifier));
        }
        FOLLOW.get(0).set.add("$");
        boolean changes = true;
        while(changes){
           for(int i = 0; i < FOLLOW.size(); i++){
               String A = FOLLOW.get(i).nonterminal;
               for(int j = 0; j < FOLLOW.get(i).set.size(); j++){
                   String[] X = spacePart(FOLLOW.get(i).set.get(j));
                   boolean nt = nt(X);
        }
=======
       /* for(int i = 0; i < rules.size()){
            FOLLOW.add(rules.get(i));
        } */
>>>>>>> 1d95d884eb77fa224418626da148a04906aac35f
    }
}*/

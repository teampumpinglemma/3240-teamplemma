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
   // ArrayList<grammarRules> rules;
    ArrayList<String> rules;
    ArrayList<String> nonTerminals;
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
            //rules = new ArrayList<grammarRules>();
            rules = new ArrayList<String>();
            nonTerminals = new ArrayList<String>();
            this.parsedTokens = parsedTokens;
        }
        catch (Exception e) {
            System.out.println("Grammar parser construction error");
            System.exit(0);
        }
    }

    public void parseGrammar()
    {
        ArrayList<String> lines = new ArrayList();
        // Need list of terminals (tokens) and non-terminals
        try
        {
            String s_temp = buffReader.readLine();
            while(s_temp != null){
                lines.add(s_temp);
                s_temp = buffReader.readLine();
            }
        }
        catch(Exception ex)
        {
            if(lines.size() != 0){
                System.out.println("Error reading file");
                System.exit(0);
            }
        }
        for(int u = 0; u < lines.size(); u++){
            currentLine = lines.get(u);
            // Get rid of leading and trailing white space on line
            currentLine = currentLine.trim();
            //stopParsingLine = false;
            // If line is not empty
            int index = 0;
            String state = "Start";
            String word = "";
            boolean matched = false;
            while((currentLine.length() > index) && (!state.equals("error")))
            {
/*                System.out.println(state);*/
                if(state.equals("Start")){
                    switch (currentLine.charAt(index)){
                        case '<':
                            state = "Record_st";
                            word = "<";
                            break;
                        case ' ':
                        case '\t':
                            break;
                        default:
                            System.out.println("Must start rule with <non-terminal>");
                            System.exit(0);
                     }
                }else if(state.equals("Record_st")){
                     switch (currentLine.charAt(index)){
                         case ' ':
                         case '\t':
                             System.out.println("Error: Space in word");
                             System.exit(0);
                             break;
                         case '>':
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             currentRule = word;
                             state = "Eq";
                             if ((!nonTerminals.isEmpty()) && (!nonTerminals.contains(word))){
                                 nonTerminals.add(word);
                             }
                             word = "";
                             break;
                         default:
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             break;
                     }
                }else if(state.equals("Eq")){
                     switch (currentLine.charAt(index)){
                         case ' ':
                         case '\t':
                             break;
                         case '=':
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             if(!word.equals("::=")){
                                 /*System.out.println(word);*/
                                 System.out.println("Rule must contain ::= in proper location");
                                 System.exit(0);
                             }else{
                                 currentRule = currentRule.concat(" " + word);
                                 identifier = currentRule.toString();
			/*	System.out.println("id "+identifier);*/
                                 word = "";
                                 state = "Pred";
                             }
                             break;
                         case ':':
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             break;
                         default:
                             System.out.println("Rule must contain ::= in proper location");
                             System.exit(0);
                     }
                }else if(state.equals("Pred")){
                     switch (currentLine.charAt(index)){
                         case ' ':
                         case '\t':
                             break;
                         case '<':
                             word = "<";
                             state = "NTPred";
                             break;
                         case '>':
                             System.out.println("Invalid character");
                             System.exit(0);
                             break;
                         case '|':
                             if (!currentRule.equals("")){
                                 rules.add(currentRule);
                             }
                             currentRule = identifier.toString();
                             break;
                         default:
                             word = Character.toString(currentLine.charAt(index));
                             state = "TPred";
                             break;
                     }
                }else if(state.equals("NTPred")){
                     switch (currentLine.charAt(index)){
                         case ' ':
                         case '\t':
                         case '|':
                             System.out.println("Error: Space in word");
                             System.exit(0);
                             break;
                         case '>':
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             currentRule += " " + word;
                             state = "Pred";
                             if ((!nonTerminals.isEmpty()) && (!nonTerminals.contains(word))){
                                 nonTerminals.add(word);
                             }
                             word = "";
                             break;
                         default:
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             break;
                     }
                }else if(state.equals("TPred")){
                     switch (currentLine.charAt(index)){
                         case ' ':
                         case '\t':
                             matched = false;
                             for (int i = 0; i < parsedTokens.size(); i++) {
                                 if (parsedTokens.get(i).definition.name.toUpperCase().equals("$" + word.toUpperCase())) {
                                     matched = true;
                                     break;
                                 }
                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }else{
                                 System.out.println("Terminal not recognized: " + word);
                                 System.exit(0);
                             }
                             word = "";
                             state = "Pred";
                             break;
                         case '|':
                             matched = false;
                             for (int i = 0; i < parsedTokens.size(); i++) {
                                 if (parsedTokens.get(i).definition.name.toUpperCase().equals("$" + word.toUpperCase())) {
                                     matched = true;
                                     break;
                                 }
                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }else{
                                 System.out.println("Terminal not recognized: " + word);
                                 System.exit(0);
                             }
                             if (!currentRule.equals("")){
                                 rules.add(currentRule);
                             }
                             currentRule = identifier.toString();
                             word = "";
                             break;
                         case '<':
                             currentRule = currentRule.concat(" " + word);
                             state = "NTPred";
                             word = "<";
                             break;
                         default:
                             word = word.concat(Character.toString(currentLine.charAt(index)));
                             break;
                     }
                }
                // Read in next "word"
 //               String word = currentLine.substring(0, currentLine.indexOf(' '));

 //               // Check if word is non-terminal
 //               if (word.substring(0, 1).equals("<"))
 //               {
 //                   // New rule for the new line
 //                   currentRule = word;

 //                   if ((!nonTerminals.isEmpty()) && (!nonTerminals.contains(word)))
 //                   {
 //                       nonTerminals.add(word);
 //                   }
 //               }
 //               else
 //               {
 //                   // Throw error
 //                   System.out.println("Must start rule with <non-terminal>");
 //                   System.exit(0);
 //               }

 //               // Get next "word"
 //               currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
 //               currentLine.trim();
 //               word = currentLine.substring(0, currentLine.indexOf(" "));
 //               word.trim();

 //               if (word.equals("::="))
 //               {
 //                   // Current rule is new identifier
 //                   currentRule = currentRule + " " + word;
 //                   identifier = currentRule;
 //               }
 //               else
 //               {
 //                   // Throw error
 //                   System.out.println("Rule must contain ::= in proper location");
 //                   System.exit(0);
 //               }

 //               // Get next "word"
 //               try{
 //                   currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
 //               }catch(StringIndexOutOfBoundsException e){
 //                   
 //               currentLine.trim();
 //               word = currentLine.substring(0, currentLine.indexOf(" "));
 //               word.trim();

 //               while ((word.length() > 0) && (stopParsingLine == false))
 //               {
 //                   // Check if current word is terminal or non-terminal
 //                   if (word.substring(0, 1).equals("<"))
 //                   {
 //                       // Non-terminal
 //                       currentRule = currentRule + " " + word;

 //                       if (!nonTerminals.contains(word))
 //                       {
 //                           nonTerminals.add(word);
 //                       }
 //                   }
 //                   else if (word.equals("|"))
 //                   {
 //                       // Add next terminals/non-terminals to rules ArrayList (unless there are more |'s or end of line)
 //                       if (!currentRule.equals(""))
 //                       {
 //                           rules.add(currentRule);
 //                       }

 //                       currentRule = identifier;
 //                   }
 //                   else // Terminal
 //                   {
 //                       boolean matched = false;
 //                       for (int i = 0; i < parsedTokens.size(); i++) {
 //                           if (parsedTokens.get(i).definition.name.toUpperCase().equals("$" + word.toUpperCase())) {
 //                               matched = true;
 //                               break;
 //                           }
 //                       }
 //                       if (matched)
 //                       {
 //                           currentRule = currentRule + " " + word;
 //                       }
 //                       else
 //                       {
 //                           // Throw error
 //                           System.out.println("Terminal not recognized: " + word);
 //                           System.exit(0);
 //                       }
 //                   }

 //                   // Get next "word"
 //                   try
 //                   {
 //                       currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
 //                       System.out.println(currentLine);
 //                       currentLine.trim();
 //                       word = currentLine.substring(0, currentLine.indexOf(" "));
 //                       System.out.println(word);
 //                       word.trim();

 //                       if (word.equals("end"))
 //                       {
 //                           stopParsingLine = true;
 //                           word = "";
 //                           currentLine = "";
 //                       }
 //                   }
 //                   catch (Exception ex)
 //                   {
 //                       stopParsingLine = true;
 //                       word = "";
 //                       currentLine = "";
 //                   }
 //               }


 //               // Add final rule on the line
 //               rules.add(currentRule);

                // Go to next line because there are no more "words" left on line
                //parseGrammar();
		/*System.out.println("Rule "+u+": "+currentRule);
		System.out.println("LINE: "+currentLine);*/
                index++;
            }
        }

        for (int i = 0; i < rules.size(); i++)
        {
            System.out.println(rules.get(i));
        }
    }
}

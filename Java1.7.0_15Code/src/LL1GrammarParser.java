import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class LL1GrammarParser {

    BufferedReader buffReader, checker;
    SpecReader specReader;
   // ArrayList<grammarRules> rules;
    ArrayList<String> rules;
    ArrayList<String> nonTerminals;
    String currentLine, currentRule, identifier;
    boolean startedLine = false;
    ArrayList<Definition> parsedTokens;
    boolean stopParsingLine = false;

    /**
     * This file will be used to read the grammar from the specified input file.
     */
    public LL1GrammarParser(File grammarFile, ArrayList<Definition> parsedTokens)
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
            currentRule = "";
            //stopParsingLine = false;
            // If line is not empty
            int index = 0;
            String state = "Start";
            String word = "";
            boolean matched = false;
            while((currentLine.length() > index) && (!state.equals("error")))
            {
//                System.out.println(state + " " + currentLine.charAt(index));
//		System.out.println("id "+identifier);
//		if(currentLine.charAt(index) == '|'){
//		System.out.println("Rule "+u+": "+currentRule);
//		System.out.println("LINE: "+currentLine);}
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
                                 System.out.println("Rule must contain ::= in proper location");
                                 System.exit(0);
                             }else{
                                 currentRule = currentRule.concat(" " + word);
                                 identifier = currentRule.toString();
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
                         case '|':
                             if (!currentRule.equals("")){
                                 rules.add(currentRule);
                             }
                             currentRule = identifier.toString();
                             break;
                         case '(':
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             currentRule += " \\(";
                             break;
                         case ')':
                             matched = false;
                             for (int i = 0; i < parsedTokens.size(); i++) {
                                 String s = "";
                                 for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                     s += parsedTokens.get(i).tokens.get(j).characters;
                                 }
                                 if (s.equals(word)) {
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }
                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             word = "";
                             currentRule += " \\)";
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
                         case '(':
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             currentRule += " \\(";
                             break;
                         case ')':
                             matched = false;
                             for (int i = 0; i < parsedTokens.size(); i++) {
                                 String s = "";
                                 for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                     s += parsedTokens.get(i).tokens.get(j).characters;
                                 }
                                 if (s.equals(word)) {
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }
                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             word = "";
                             currentRule += " \\)";
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
                                 String s = "";
                                 for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                     s += parsedTokens.get(i).tokens.get(j).characters;
                                 }
                             
                                 if (s.equals(word)) {
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                     matched = true;
                                     word = parsedTokens.get(i).name;
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
                                 String s = "";
                                 for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                     s += parsedTokens.get(i).tokens.get(j).characters;
                                 }
                             
                                 if (s.equals(word)) {
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }

                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }
                             if (!currentRule.equals("")){
                                 rules.add(currentRule);
                             }
                             currentRule = identifier.toString();
                             word = "";
                             break;
                         case '(':
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             currentRule += " \\(";
                             break;
                         case ')':
                             matched = false;
                             for (int i = 0; i < parsedTokens.size(); i++) {
                                 String s = "";
                                 for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                     s += parsedTokens.get(i).tokens.get(j).characters;
                                 }
                                 if (s.equals(word)) {
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                     matched = true;
                                     word = parsedTokens.get(i).name;
                                     break;
                                 }
                             }
                             if (matched){
                                 currentRule = currentRule + " " + word;
                             }
                             if(word != ""){
                                 currentRule += " " + word;
                             }
                             word = "";
                             currentRule += " \\)";
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
                if(++index == currentLine.length()){
                     if(word.length() != 0){
                         matched = false;
                         for (int i = 0; i < parsedTokens.size(); i++) {
                             String s = "";
                             for(int j = 0; j < parsedTokens.get(i).tokens.size();j++){
                                 s += parsedTokens.get(i).tokens.get(j).characters;
                             }
                             if (s.equals(word)) {
                                 matched = true;
                                 word = parsedTokens.get(i).name;
                                 break;
                             }else if(parsedTokens.get(i).name.substring(1,parsedTokens.get(i).name.length()).equals(word)){
                                 matched = true;
                                 word = parsedTokens.get(i).name;
                                 break;
                             }
                         }
                         if (matched){
                             currentRule = currentRule + " " + word;
                         }else{
                             System.out.println("Terminal not recognized: " + word);
                             System.exit(0);
                         }
                     }
                     if(!currentRule.equals("")){
                             rules.add(currentRule);
                     }
                }
                 
           }
        }

        /*for (int i = 0; i < rules.size(); i++)
        {
            System.out.println(rules.get(i));
        }*/
    }
}

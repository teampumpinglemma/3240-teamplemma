import java.util.*;
import java.io.*;

/**
 * Created with Vim.
 * User: csmythe
 */
public class LL1ParsingTable {
    
    ArrayList<Definition> ALtokens;
    Hashtable<String, String[]> first, follow;
    Hashtable<String, String> tokens;
    LinkedList<DefinitionWithCharacters> parsed;
    ArrayList<String> rules;
    String[][] table;
    ArrayList indNT;
    /**
     * This will be the file that creates the LL(1) Parsing Table, the FIRST sets, and the FOLLOW sets.
     */
    public LL1ParsingTable(LL1FFSets ffsets, ArrayList<Definition> tokens, LinkedList<DefinitionWithCharacters> fl,ArrayList<String> rules)
    {
        first = ffsets.FIRST;
        follow = ffsets.FOLLOW;
        this.rules = rules;
        this.ALtokens = tokens;
        this.tokens = new Hashtable<String,String>();
        this.parsed = fl;
        this.table = new String[first.keySet().size()][tokens.size()+1];
        convString();
        makeTable();
    }

    public void convString(){
       for(Definition d : ((Definition[])ALtokens.toArray(new Definition[0]))){
           String s = "";
           for(int i = 0; i < d.tokens.size(); i++){
               s += d.tokens.get(i).characters;
           }
           tokens.put(s, d.name);
       }
       ArrayList<String> rl = new ArrayList<String>();
       for(int i = 0; i < rules.size(); i++){
           rl.add("");
           for(String r : spSplit(rules.get(i))){
               boolean add = false;
               for(String s : tokens.keySet())
                   if(r.equals(s)){
                       String h = rl.remove(i);
                       h += " " + tokens.get(s);
                       rl.add(h);
                       add = true;
                   }
               if(!add){
                   String h = rl.remove(i);
                   h += " "+r;
                   rl.add(h);
               }
           }
           String h = rl.remove(i);
           h = h.substring(1,h.length());
           rl.add(h);
       }
       rules = rl;
    }

    public String[] spSplit(String s){
        ArrayList<String> a = new ArrayList<String>();
        while(s != ""){
            try{
                a.add(s.substring(0,s.indexOf(" ")));
                s = s.substring(s.indexOf(" ")+1, s.length());
            }catch(Exception e){
                a.add(s);
                s = "";
            }
        }
        return ((String[])a.toArray(new String[0]));
    }

    public int find (ArrayList l, Object o){
        for(int i = 0; i < l.size(); i++){
            if(o.equals(l.get(i)))
                return i;}
        return -1;
    }

    public int Dfind (ArrayList<Definition> l, Object o){
        for(int i = 0; i < l.size(); i++){
            if(o.equals(l.get(i).name))
                return i;}
        return -1;
    }

    public void makeTable(){
       indNT = new ArrayList<String>(first.keySet());
       for(int i = 0; i < rules.size(); i++){
           String[] r = spSplit(rules.get(i));
           boolean nu = true;
           for(int j = 2; j < r.length; j++){
               if(r[j].equals("<epsilon>"))
                   break;
               if(r[j].charAt(0) != '<'){
                   if(table[find(indNT,r[0])][Dfind(ALtokens,r[j])] == null){
                       table[find(indNT,r[0])][Dfind(ALtokens,r[j])] = rules.get(i);
                   }else{
                       System.out.println("Not LL(1) grammar.");
                       System.exit(0);
                   }
                   nu = false;
                   break;
               }else{
                   for(int k = 0; k < ALtokens.size(); k++){
                       for(String s : first.get(r[j])){
                           if(s.equals(ALtokens.get(k).name)){
                               if(table[find(indNT,r[0])][Dfind(ALtokens,s)] == null){
                                   table[find(indNT,r[0])][Dfind(ALtokens,s)] = rules.get(i);
                               }else{
                                   System.out.println("Not LL(1) grammar"+ i);
                                   System.exit(0);
                               }
                           }
                       }
                   }
                   nu = false;
                   for(String s: first.get(r[j])){
                       if(s == "<epsilon>"){
                           nu = true;
                       }
                   }
                   if(!nu){
                       break;
                   }
               }
           }
           if(nu){
               for(int j = 0; j < ALtokens.size(); j++){
                   if(follow.get(r[0]) != null){
                       for(String s: follow.get(r[0])){
                           if(s.equals(ALtokens.get(j))){
                               if(table[find(indNT,r[0])][Dfind(ALtokens,r[j])] == null){
                                  table[find(indNT,r[0])][Dfind(ALtokens,r[j])] = rules.get(i);
                               }else{
                                  System.out.println("Not LL(1) grammar");
                                  System.exit(0);
                               }
                           }
                       }
                   }
               }
           }
       }
       /*for(int b = 0; b < table.length;b++){
          System.out.println();
          for(int c= 0;c<table[b].length;c++)
              System.out.print(table[b][c] + " ");}*/
       
    }

    public String[] turnParsedToStrings(){
       ArrayList<String> s = new ArrayList<String>();
       for(int i = 0; i < parsed.size(); i++){
           s.add(parsed.get(i).definition.name);
       }
       return ((String[])s.toArray(new String[0]));
    }

    public String run(){
       String[] psd = turnParsedToStrings();
       Stack<String> ps = new Stack<String>();
       for(int j = spSplit((String)rules.get(0)).length-1; j > 1; j--){
           ps.push(spSplit(((String)rules.get(0)))[j]);}
       int i = 0;
       while(!ps.empty() && i < psd.length){
           if(ps.peek().equals(psd[i])){
               ps.pop();
               i++;
           }else if(ps.peek().charAt(0) == '<' && !ps.peek().equals("<epsilon>") && table[find(indNT,ps.peek())][Dfind(ALtokens, psd[i])] != null){
               String test = ps.pop();
               String ru = table[find(indNT,test)][Dfind(ALtokens, psd[i])];
               for(int j = spSplit(ru).length-1; j > 1; j--){
                   ps.push(spSplit(ru)[j]);
               }
           }else{
               boolean noerr = false;
               for(String r : rules){
                  if(r.equals(ps.peek() + " ::= <epsilon>")){
                      noerr = true;
                      ps.pop();
                      break;
                  }
               }
               if(!noerr)
                   return "Error";
           }
       }
       if(ps.empty() && i >= psd.length)
           return "Accept";
       return "Reject";
    }
}

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
                       h +=  tokens.get(s) + " ";
                       rl.add(h);
                       add = true;
                   }
               if(!add){
                   String h = rl.remove(i);
                   h += r + " ";
                   rl.add(h);
               }
           }
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
       indNT = new ArrayList<String>(first.keySet());//values());
//       for(int i = 0; i< indNT.size();i++)
  //         for(int j = 0; j < ((String[])(indNT.get(i))).length;j++)
    //           System.out.println(((String[])indNT.get(i))[j]);
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
                       System.out.println("79 Not LL(1) grammar.");
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
                                   System.out.println("91 Not LL(1) grammar"+ i);
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
                                  System.out.println("115 Not LL(1) grammar");
                                  System.exit(0);
                               }
                           }
                       }
                   }
               }
           }
       }
       
    }

}

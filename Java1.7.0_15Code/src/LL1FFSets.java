import java.util.*;
import java.io.*;

/**
 * Created with Unix cp utility.
 * User: csmythe
 */
public class LL1FFSets {

    Hashtable<String, String[]> rules;
    Hashtable<String, String[]> FIRST, FOLLOW;
    String base = "";

    /**
     * This will be the file that creates the the FIRST sets and the FOLLOW sets.
     */
    public LL1FFSets(ArrayList<String> notFormatted)
    {
            
        // Change rules into "proper" grammarRules class format
        reformatRules(notFormatted);

        // Create FIRST and FOLLOW sets
        createFirstSet();
        createFollowSet();
    }

/**
 * Formats the rules differently for parsing below.
 * */

    public void reformatRules(ArrayList<String> nf){
//Hashtables handle things easier.
//Basically, make an array of right sides and match them to the left sides
        Hashtable<String, ArrayList<String>> m = new Hashtable<String, ArrayList<String>>();
        for(int i = 0; i < nf.size(); i++){
            String id = nf.get(i).substring(0, nf.get(i).indexOf(" "));
            if(base == ""){
                base = id;
            }
            String predicate = nf.get(i).substring(nf.get(i).indexOf("::= ") + 4,nf.get(i).length());
            if(!m.containsKey(id)){
                m.put(id, new ArrayList<String>());
            }
            m.get(id).add(predicate);
        }
        rules = new Hashtable<String, String[]>();
//Move from temporary hashtable
        for(String k : m.keySet()){
            rules.put(k, ((String[])m.get(k).toArray(new String[0])));
            //for(int l = 0; l < rules.get(k).length;l++){
              //      System.out.println(k + ": "+l);
          //  }
        }
    }
/**
 * Splits an input according to the spaces
 * Outputs an array of split Strings
 * */

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
/**
 * Follows the first set pseudocode in Louden
 * */
    public void createFirstSet(){
        Hashtable <String, ArrayList<String>> f = new Hashtable<String, ArrayList<String>>();
        for(String A: rules.keySet()){
             f.put(A, new ArrayList<String>());
        }
        boolean change = true;
        while(change){
            change = false;
            for(String A: rules.keySet()){
                for(String p: rules.get(A)){
                     int k = 0;
                     boolean cont = true;
                     String[] pc = spSplit(p);
                     while(cont & k < pc.length){
                         if(pc[k].charAt(0) != '<'){
//Add terminals directly if not in
                             //System.out.println(pc[k]);
                             if((!f.get(A).contains(pc[k]))){
                                 f.get(A).add(pc[k]);
                                 change = true;
                             }
                         }else if(pc[k].charAt(0) == '<' && !pc[k].equals("<epsilon>")){
                             for(String temp : f.get(pc[k])){
                                 if(!f.get(A).contains(temp) && !temp.equals("<epsilon>")){
//Add the nonterminals if they are not in
                                     f.get(A).add(temp);
                                     change = true;
                                 }
                             }
                         }else if(pc[k].equals("<epsilon>")){
//First sets have to handle epsilon too. This is one effective adding method.
                             if(!f.get(A).contains("<epsilon>")){
                                 f.get(A).add("<epsilon>");
                                 change = true;
                             }
                         }
                         cont = false;
                         if(f.get(pc[k]) != null){
                             for(String temp : f.get(pc[k])){
                                 if(temp.equals("<epsilon>")){
                                     cont = true;
                                 }
                             }
                         }
                         k++;
                     }
                     if(cont){
                        f.get(A).add("<epsilon>"); 
                     }
                 }
            }
        }
        FIRST = new Hashtable<String,String[]>();
        for(String k: f.keySet()){
            FIRST.put(k, ((String[])f.get(k).toArray(new String[0])));
           /* System.out.print(k+":");
            for(String l:f.get(k))
                System.out.print(" "+l);
            System.out.println();*/
        }
    }
/**
 * Does array contain entry? Answers true or false!
 * */
    public boolean aContains(String[] a, String in){
        if(a == null)
            return false;
        for(String s:a){
            if(s.equals(in)){
                return true;
            }
        }
        return false;
    }
/**
 * Follows the algorithm in Louden
 * */
    
    public void createFollowSet(){
        Hashtable<String, ArrayList<String>> f = new Hashtable<String, ArrayList<String>>();
        for(String k: FIRST.keySet()){
            f.put(k, new ArrayList<String>());
        }
        boolean change = true;
        while(change){
            change = false;
            for(String A: rules.keySet()){
                for(String p: rules.get(A)){
//Required to loop like this for hashing
                    String[] pc = spSplit(p);
                    for(int i = 0; i < pc.length; i++){
                        if(pc[i].charAt(0) == '<' && pc[i] != "<epsilon>"){
                            int j = i;
                            while(++j < pc.length){
                                if(pc[j].charAt(0) == '<' && pc[j] != "<epsilon>"){
                                    for(String chr: FIRST.get(pc[j])){
                                        if(!f.get(pc[i]).contains(chr) && !chr.equals("<epsilon>")){
//Nonterminals
                                            f.get(pc[i]).add(chr);
                                            change = true;
                                        }
                                    }
//Terminals
                                }else if(pc[j].charAt(0) != '<'){
                                    if(!f.get(pc[i]).contains(pc[j])){
                                        f.get(pc[i]).add(pc[j]);
                                        change = true;
                                    }
                                }
                                if(!aContains(FIRST.get(pc[j]),"<epsilon>")){
                                    j = pc.length+1;
                                }
                            }
//If a nonterminal
                            if(j == pc.length && pc[i].charAt(0) == '<' && !pc[i].equals("<epsilon>")){
                                for(String chr:f.get(A)){
                                    if(!f.get(pc[i]).contains(chr) && !chr.equals("<epsilon>")){
                                        f.get(pc[i]).add(chr);
                                        change = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FOLLOW = new Hashtable<String, String[]>();
        /*for(String k : f.keySet()){
            FOLLOW.put(k, ((String[])f.get(k).toArray(new String[0])));
            System.out.print(k+":");
            for(String l:f.get(k))
                System.out.print(" "+l);
            System.out.println();
        }*/
    }
}

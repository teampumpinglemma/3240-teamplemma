import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/25/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LL1ParsingTable2 {

    Hashtable<String, String[]> rules;
    Hashtable<String, String[]> FIRST, FOLLOW;
    String base = "";
    boolean addNewRule, addFirstWord;

    /**
     * This will be the file that creates the LL(1) Parsing Table, the FIRST sets, and the FOLLOW sets.
     */
    public LL1ParsingTable2(ArrayList<String> notFormatted)
    {
        addNewRule = true;
        addFirstWord = true;

        // Change rules into "proper" grammarRules class format
        reformatRules(notFormatted);

        // Create FIRST and FOLLOW sets
        //createFollowSet();
        // Create FIRST set
        createFirstSet();
    }

    public void reformatRules(ArrayList<String> nf){
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

        for(String k : m.keySet()){
            rules.put(k, ((String[])m.get(k).toArray(new String[0])));
            //for(int l = 0; l < rules.get(k).length;l++){
              //      System.out.println(k + ": "+l);
          //  }
        }
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
                              if((!f.get(A).contains(Character.toString(pc[k].charAt(0))) && pc[k].charAt(0) != '\\')){ 
                                 f.get(A).add(Character.toString(pc[k].charAt(0)));
                                 change = true;
                             }else if(pc[k].length() < 2){
                             }else if((!f.get(A).contains(Character.toString(pc[k].charAt(1))) && pc[k].charAt(0) == '\\')){
                                 f.get(A).add(Character.toString(pc[k].charAt(1)));
                                 change = true;
                             }
                         }else if(pc[k].charAt(0) == '<' && !pc[k].equals("<epsilon>")){
                             for(String temp : f.get(pc[k])){
                                 if(!f.get(A).contains(temp)){
                                     f.get(A).add(temp);
                                     change = true;
                                 }
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
        }
    }
       
}

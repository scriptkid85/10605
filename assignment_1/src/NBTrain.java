import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class NBTrain {

  private static Hashtable<Vector<String>, Integer> counters = new Hashtable<Vector<String>, Integer>();

  private static int totalcount = 0;
  
  private static int numberofinstance = 0;
  
  public NBTrain() {
    counters = new Hashtable<Vector<String>, Integer>();
  }

  private static Vector<String> tokenizeDoc(String cur_doc) {
    String[] words = cur_doc.split("\\s+");
    Vector<String> tokens = new Vector<String>();
    for (int i = 0; i < words.length; i++) {
      words[i] = words[i].replaceAll("\\W", "");
      if (words[i].length() > 0) {
        tokens.add(words[i]);
      }
    }
    return tokens;
  }

  private static void outputCounter() throws IOException {
    int value;
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));

    Vector<String> totalinstance = new Vector<String>();
    totalinstance.add("*");
    counters.put(totalinstance, numberofinstance);
    
    Vector<String> totalcounts = new Vector<String>();
    totalcounts.add("*");
    totalcounts.add("*");
    counters.put(totalcounts, totalcount);

    for (Iterator<Vector<String>> it = counters.keySet().iterator(); it.hasNext();) {
      Vector<String> key = (Vector<String>) it.next();
      value = counters.get(key);
      if(key.size() == 1){
        outputcounter.write(key.get(0) + "\t" + value + "\n");
        outputcounter.flush();
      }
      else {
        outputcounter.write(key.get(0) + "\t" + key.get(1) + "\t" + value + "\n");
        outputcounter.flush();
      }
    }

  }
/*
  private static void updateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String s;
    Vector<String> tokens;

    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = tokenizeDoc(s);
      for (String token : tokens) {
        if (counters.containsKey(token))
          counters.put(token, counters.get(token) + 1);
        else
          counters.put(token, 1);
      }
    }
  }
*/
  
  private static void updateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String s, labels, content;
    String[] labeltokens;
    Vector<String> contenttokens, tempkey;
    int splitposition;

    while ((s = in.readLine()) != null && s.length() != 0) {
      splitposition = s.indexOf("\t");
      labels = s.substring(0, splitposition);
      content = s.substring(splitposition + 2,s.length());
      labeltokens = labels.split(",");
      contenttokens = tokenizeDoc(content);
      
      for(String label : labeltokens){
        if(label.equals("CCAT") || label.equals("ECAT") 
                || label.equals("GCAT") || label.equals("MCAT")){
          numberofinstance ++;
          tempkey = new Vector<String>();
          tempkey.add(label);
          if(counters.containsKey(tempkey))
            counters.put(tempkey, counters.get(tempkey) + 1);
          else counters.put(tempkey, 1);
          
          for(String token: contenttokens){
            
            tempkey = new Vector<String>();
            tempkey.add(label);
            tempkey.add(token);
            if(counters.containsKey(tempkey))
              counters.put(tempkey, counters.get(tempkey) + 1);
            else counters.put(tempkey, 1);
            
            tempkey = new Vector<String>();
            tempkey.add(label);
            tempkey.add("*");
            if(counters.containsKey(tempkey))
              counters.put(tempkey, counters.get(tempkey) + 1);
            else counters.put(tempkey, 1);
            
            
          }
         totalcount += contenttokens.size();
        }
      }
    }
  }



  public static void main(String args[]) throws IOException {
    totalcount = 0;
    numberofinstance = 0;
    updateCounter();
    outputCounter();
  }

}

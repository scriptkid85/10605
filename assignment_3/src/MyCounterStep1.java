import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class MyCounterStep1 {

  private static Hashtable<Vector<String>, Integer> counters = new Hashtable<Vector<String>, Integer>();

  private static int hashtablemaxsize = 100000;

  private static long phrasesize = 0;
  
  private static long vocabularysize = 0;
  
  public MyCounterStep1() {
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
    if(counters.size() > 0){
      int value;
      BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    
      for (Iterator<Vector<String>> it = counters.keySet().iterator(); it.hasNext();) {
        Vector<String> key = (Vector<String>) it.next();
        value = counters.get(key);
        if(key.size() == 1){
          outputcounter.write(key.get(0) + "\t" + value + "\n");
          outputcounter.flush();
        }
        else {
          outputcounter.write(key.get(1) + " " + key.get(0) + "\t" + value + "\n");
          outputcounter.flush();
        }
      }
    }
  }
  
  private static void updateCounterfromFile(String inputfile) throws IOException {

    FileInputStream fstream = new FileInputStream(inputfile);
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));

    String s;
    String[] tokens, biwords;

    int sumForPreviousKey = 0;
    s = br.readLine();
    String prekey = s.split("\t")[0]; //key is word \t decade \t number
    String predecade = s.split("\t")[1];
    sumForPreviousKey = Integer.parseInt(s.split("\t")[2]);
    vocabularysize ++;
    while ((s = br.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens[0].split(" ").length > 1)break;
      if(tokens[0].equals(prekey)){
        if(!tokens[1].equals("1990")){
          sumForPreviousKey += Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
        else {
          outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
          outputcounter.flush();
          sumForPreviousKey = Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
      }
      else{
        vocabularysize ++;
        if(tokens[1].equals("1990")){
          if(predecade.equals("1990")){
            outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            outputcounter.write("B " + prekey + "\t" + "0" + "\n");
            outputcounter.flush();
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write("C " + prekey + "\t" + "0" + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
        else{
          if(predecade.equals("1990")){
            outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
      }
    }
    if(predecade.equals("1990")){
      outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    else{
      outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    
    tokens = s.split("\t");
    prekey = tokens[0];
    predecade = tokens[1];
    sumForPreviousKey = Integer.parseInt(tokens[2]);
    phrasesize ++;
    while ((s = br.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens[0].equals(prekey)){
        if(!tokens[1].equals("1990")){
          sumForPreviousKey += Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
        else {
          outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
          outputcounter.flush();
          sumForPreviousKey = Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
      }
      else{
        phrasesize ++;
        if(tokens[1].equals("1990")){
          if(predecade.equals("1990")){
            outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            outputcounter.write("B " + prekey + "\t" + "0" + "\n");
            outputcounter.flush();
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write("C " + prekey + "\t" + "0" + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
        else{
          if(predecade.equals("1990")){
            outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
      }
    }
    if(predecade.equals("1990")){
      outputcounter.write("C " + prekey + "\t" + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    else{
      outputcounter.write("B " + prekey + "\t" + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    
    outputcounter.write("^vocabularysize" + "\t" + vocabularysize + "\n");
    outputcounter.flush();
    outputcounter.write("^phrasesize" + "\t" + phrasesize + "\n");
    outputcounter.flush();
    
  }
  
  
  private static void updateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s;
    String[] tokens, words;

    int sumForPreviousKey = 0;
    s = in.readLine();
    String prekey = s.split("\t")[0]; //key is word \t decade \t number
    String predecade = s.split("\t")[1];
    sumForPreviousKey = Integer.parseInt(s.split("\t")[2]);

    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens[0].split(" ").length > 1)break;
      if(tokens[0].equals(prekey)){
        if(!tokens[1].equals("1990")){
          sumForPreviousKey += Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
        else {
          outputcounter.write( prekey + "\t" + "B " + sumForPreviousKey + "\n");
          outputcounter.flush();
          vocabularysize += sumForPreviousKey;
          sumForPreviousKey = Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
      }
      else{
        vocabularysize ++;
        if(tokens[1].equals("1990")){
          if(predecade.equals("1990")){
            outputcounter.write(prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            outputcounter.write(prekey + "\t" + "B " + "0" + "\n");
            outputcounter.flush();
            vocabularysize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write(prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write(prekey + "\t" + "C " + "0" + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            vocabularysize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
        else{
          if(predecade.equals("1990")){
            outputcounter.write( prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            vocabularysize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            outputcounter.write(prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            vocabularysize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
      }
    }
    if(predecade.equals("1990")){
      outputcounter.write( prekey + "\t" + "C " + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    else{
      outputcounter.write(prekey + "\t" + "B " + sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    
    tokens = s.split("\t");
    prekey = tokens[0];
    words = prekey.split(" ");
    predecade = tokens[1];
    vocabularysize += sumForPreviousKey;
    
    sumForPreviousKey = Integer.parseInt(tokens[2]);

    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens[0].equals(prekey)){
        if(!tokens[1].equals("1990")){
          sumForPreviousKey += Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
        else {
          words = prekey.split(" ");
          outputcounter.write(words[0] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
          outputcounter.flush();
          outputcounter.write(words[1] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
          outputcounter.flush();
          phrasesize += sumForPreviousKey;
          sumForPreviousKey = Integer.parseInt(tokens[2]);
          predecade = tokens[1];
        }
      }
      else{
        phrasesize ++;
        if(tokens[1].equals("1990")){
          if(predecade.equals("1990")){
            words = prekey.split(" ");
            outputcounter.write( words[0] + " " + prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write( words[1] + " " + prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            
            words = prekey.split(" ");
            outputcounter.write(words[0] + " " + prekey + "\t" + "B " +"0" + "\n");
            outputcounter.flush();
            outputcounter.write(words[1] + " " + prekey + "\t" + "B " +"0" + "\n");
            outputcounter.flush();
            phrasesize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            words = prekey.split(" ");
            outputcounter.write(words[0] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write(words[1] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            
            
            outputcounter.write(words[0] + " " + prekey + "\t" + "C " + "0" + "\n");
            outputcounter.flush();
            outputcounter.write(words[1] + " " + prekey + "\t" + "C " + "0" + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            phrasesize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
        else{
          if(predecade.equals("1990")){
            words = prekey.split(" ");
            outputcounter.write(words[0] + " " + prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write(words[1] + " " + prekey + "\t" + "C " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            phrasesize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
          else{
            words = prekey.split(" ");
            outputcounter.write(words[0] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            outputcounter.write(words[1] + " " + prekey + "\t" + "B " + sumForPreviousKey + "\n");
            outputcounter.flush();
            prekey = tokens[0];
            phrasesize += sumForPreviousKey;
            sumForPreviousKey = Integer.parseInt(tokens[2]);
            predecade = tokens[1];
          }
        }
      }
    }
    if(predecade.equals("1990")){
      words = prekey.split(" ");
      outputcounter.write(words[0] + " " + prekey + "\t" + "C " +  sumForPreviousKey + "\n");
      outputcounter.flush();
      outputcounter.write(words[1] + " " + prekey + "\t" + "C " +  sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    else{
      words = prekey.split(" ");
      outputcounter.write(words[0] + " " + prekey + "\t" + "B " +  sumForPreviousKey + "\n");
      outputcounter.flush();
      outputcounter.write(words[1] + " " + prekey + "\t" + "B " +  sumForPreviousKey + "\n");
      outputcounter.flush();
    }
    phrasesize += sumForPreviousKey;
    
    outputcounter.write("^vocabularysize" + "\t" + vocabularysize + "\n");
    outputcounter.flush();
    outputcounter.write("^phrasesize" + "\t" + phrasesize + "\n");
    outputcounter.flush();
    
  }



  public static void main(String args[]) throws IOException {
    //updateCounterfromFile("inputfile.txt");
    updateCounter();
//    outputCounter();
  }

}

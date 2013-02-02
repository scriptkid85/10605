import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class NBTest {

  private static Hashtable<Vector<String>, Integer> traincounters = new Hashtable<Vector<String>, Integer>();
  
  private static HashSet<String> vocabulary = new HashSet<String>();

  private static int traintotalcount = 0;
  
  private static int traintotalinstance = 0;
  
  private static long testtotalinstance = 0;

  private static String[] labelspace = {"CCAT", "ECAT", "GCAT", "MCAT"};
  
  private static double alpha = 1;
  
  private static int correctness = 0;

  
  
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
  

  private static double computeLogprob(Vector<String> feathers, String label){
    double logprob = 0;
    Vector<String> tempkey;
    tempkey = new Vector<String>();
    int labelnum = labelspace.length;
    int vocabularynum = vocabulary.size();
    int countYstar, countY, countXY;
    
    tempkey.clear();
    tempkey.add(label);
    tempkey.add("*");
    if(traincounters.containsKey(tempkey)){
      countYstar = traincounters.get(tempkey);
    }
    else countYstar = 0;
    
    tempkey.clear();
    tempkey.add(label);
    if(traincounters.containsKey(tempkey)){
      countY = traincounters.get(tempkey);
    }
    else countY = 0;
    
    for(String feather: feathers){
      tempkey.clear();
      tempkey.add(label);
      tempkey.add(feather);
      if(traincounters.containsKey(tempkey)){
        countXY = traincounters.get(tempkey);
      }
      else countXY = 0;
              
      logprob += Math.log((countXY + alpha) / (double)(countYstar + alpha * vocabularynum));
      
    }
    logprob += Math.log((countY + alpha) / (double)(traintotalinstance + (alpha * labelnum)));

    return logprob;    
  }
  
  //for test readhashtable
  private static void outputCounter() throws IOException {
    int value;
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));

    for (Iterator<Vector<String>> it = traincounters.keySet().iterator(); it.hasNext();) {
      Vector<String> key = (Vector<String>) it.next();
      value = traincounters.get(key);
      if (key.size() == 1) {
        outputcounter.write(key.get(0) + "\t" + value + "\n");
        outputcounter.flush();
      } else {
        outputcounter.write(key.get(0) + "\t" + key.get(1) + "\t" + value + "\n");
        outputcounter.flush();
      }
    }

  }

  
  private static void readHashtable() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String s;
    String[] tokens;
    Vector<String> tempkey = new Vector<String>();
   
    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if (tokens.length == 2) {
        tempkey = new Vector<String>();
        tempkey.add(tokens[0]);
        traincounters.put(tempkey, Integer.parseInt(tokens[1]));
      } else if (tokens.length == 3) {
        tempkey = new Vector<String>();
        tempkey.add(tokens[0]);
        tempkey.add(tokens[1]);
        traincounters.put(tempkey, Integer.parseInt(tokens[2]));
        if(!vocabulary.contains(tokens[1]))
          vocabulary.add(tokens[1]);
      }
    }
    
    tempkey.clear();
    tempkey.add("*");
    traintotalinstance = traincounters.get(tempkey);
    
    tempkey.clear();
    tempkey.add("*");
    tempkey.add("*");
    traintotalcount = traincounters.get(tempkey);
  }
  
  private static void readHashtable(String inputhashtable) throws IOException {
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputhashtable);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String s;
      String[] tokens;
      Vector<String> tempkey = new Vector<String>();
      while ((s = br.readLine()) != null) {
      tokens = s.split("\t");
      if (tokens.length == 2) {
        tempkey = new Vector<String>();
        tempkey.add(tokens[0]);
        traincounters.put(tempkey, Integer.parseInt(tokens[1]));
      } else if (tokens.length == 3) {
        tempkey = new Vector<String>();
        tempkey.add(tokens[0]);
        tempkey.add(tokens[1]);
        traincounters.put(tempkey, Integer.parseInt(tokens[2]));
        if(!vocabulary.contains(tokens[1]))
          vocabulary.add(tokens[1]);
      }
      }
      tempkey.clear();
      tempkey.add("*");
      traintotalinstance = traincounters.get(tempkey);
      
      tempkey.clear();
      tempkey.add("*");
      tempkey.add("*");
      traintotalcount = traincounters.get(tempkey);
    }
    catch(Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }

  

  private static void updateCounter(String inputfile) throws IOException {
    
    double logprob = -Double.MAX_VALUE, maxlogprob = -Double.MAX_VALUE;
    
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputfile);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String s, labels, content;
      int splitposition;
      // Read File Line By Line
      while ((s = br.readLine()) != null) {
        testtotalinstance ++;
        logprob = -Double.MAX_VALUE;
        maxlogprob = -Double.MAX_VALUE;
        splitposition = s.indexOf("\t");
        labels = s.substring(0, splitposition);
        content = s.substring(splitposition + 2,s.length());
        Vector<String> feathers = tokenizeDoc(content);
        String resultlabel = new String();
        for(String label: labelspace){
          logprob = computeLogprob(feathers, label);
          if(maxlogprob < logprob){
            maxlogprob = logprob;
            resultlabel = label;
          }
        } 
        
        for(String label: labels.split(",")){
          if(label.equals(resultlabel)){
            correctness ++;
            break;
          }
        }
        System.out.println("[" + labels + "]" + "\t" + resultlabel + "\t" + maxlogprob);
      }
      // Close the input stream
      in.close();
    } catch (Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void main(String args[]) throws IOException {
    
    if (args.length != 2 || !args[0].equals("-t")) {
      System.out.println("usage: NBTest -t <inputTestFile>");
      System.exit(0);
    }
    String inputfile = args[1];

    //readHashtable("output.txt");
    readHashtable();
    //outputCounter();
    //System.out.println("Finish reading hashtable.");
    
    updateCounter(inputfile);
    
    System.out.println("Percent correct: " + correctness + "/" + testtotalinstance + "=" + (correctness / (double)testtotalinstance) + "%");
  }

}

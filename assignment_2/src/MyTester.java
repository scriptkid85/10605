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

public class MyTester {

  private static Hashtable<Vector<String>, Integer> TesttableC = new Hashtable<Vector<String>, Integer>();
  
  private static Hashtable<Vector<String>, Integer> fixedTable = new Hashtable<Vector<String>, Integer>();

  private static HashSet<String> NEEDED = new HashSet<String>();
  
  private static HashSet<String> vocabulary = new HashSet<String>();

  private static long vocabularynum;
  
  private static int readin_num_threshold = 500;
  
  private static int traintotalcount = 0;
  
  private static String inputmodel;
  
  private static int traintotalinstance;
  
  private static long testtotalinstance = 0;

  private static Vector<String> labelspace = new Vector<String>();
  
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
    int labelnum = labelspace.size();
    int countYstar, countY, countXY;
    
    tempkey.clear();
    tempkey.add("*");
    tempkey.add(label);
    if(fixedTable.containsKey(tempkey)){
      countYstar = fixedTable.get(tempkey);
    }
    else countYstar = 0;
    
    tempkey.clear();
    tempkey.add(label);
    if(fixedTable.containsKey(tempkey)){
      countY = fixedTable.get(tempkey);
    }
    else countY = 0;
    
    for(String feather: feathers){
      tempkey.clear();
      tempkey.add(feather);
      tempkey.add(label);
      if(TesttableC.containsKey(tempkey)){
        countXY = TesttableC.get(tempkey);
      }
      else countXY = 0;
              
      logprob += Math.log((countXY + alpha) / (double)(countYstar + alpha * vocabularynum));
      
    }
 //   logprob += Math.log((countY + alpha) / (double)(traintotalinstance + (alpha * labelnum)));
    logprob += Math.log((countY) / (double)(traintotalinstance ));

    return logprob;    
  }
  
  //for test readhashtable
  private static void outputCounter() throws IOException {
    int value;
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));

    for (Iterator<Vector<String>> it = TesttableC.keySet().iterator(); it.hasNext();) {
      Vector<String> key = (Vector<String>) it.next();
      value = TesttableC.get(key);
      if (key.size() == 1) {
        outputcounter.write(key.get(0) + "\t" + value + "\n");
        outputcounter.flush();
      } else {
        outputcounter.write(key.get(0) + "\t" + key.get(1) + "\t" + value + "\n");
        outputcounter.flush();
      }
    }

  }

  
//  private static void readHashtable() throws IOException {
//    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//    String s;
//    String[] tokens;
//    Vector<String> tempkey = new Vector<String>();
//   
//    while ((s = in.readLine()) != null && s.length() != 0) {
//      tokens = s.split("\t");
//      if (tokens.length == 2) {
//        tempkey = new Vector<String>();
//        tempkey.add(tokens[0]);
//        TesttableC.put(tempkey, Integer.parseInt(tokens[1]));
//      } else if (tokens.length == 3) {
//        tempkey = new Vector<String>();
//        tempkey.add(tokens[0]);
//        tempkey.add(tokens[1]);
//        TesttableC.put(tempkey, Integer.parseInt(tokens[2]));
//        if(!vocabulary.contains(tokens[1]))
//          vocabulary.add(tokens[1]);
//      }
//    }
//    
//    tempkey.clear();
//    tempkey.add("*");
//    traintotalinstance = TesttableC.get(tempkey);
//    
//    tempkey.clear();
//    tempkey.add("*");
//    tempkey.add("*");
//    traintotalcount = TesttableC.get(tempkey);
//  }
  
  private static void initwithModel(String inputModel){
    Vector<String> tempkey = new Vector<String>();
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputModel);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String s;
      String[] tokens, keys;
      
      while ((s = br.readLine()) != null) {
        tokens = s.split("\t");
        keys = tokens[0].split(" ");
        if(keys[0].equals("*")){
          tempkey = new Vector<String>();
          tempkey.add("*");
          if(keys.length == 2){
            tempkey.add(keys[1]);
            labelspace.add(keys[1]);
          }
          fixedTable.put(tempkey, Integer.parseInt(tokens[1]));
        }
        else if(keys.length == 1 && !keys[0].equals("*")){
          if(keys[0].equals("vocabsize"))
            vocabularynum = Integer.parseInt(tokens[1]);
          else{
            tempkey = new Vector<String>();
            tempkey.add(keys[0]);
            fixedTable.put(tempkey, Integer.parseInt(tokens[1]));
          }
        }
        else if(keys.length == 2){
          if(keys[0].equals("gable")){
            int a = 1;
          }
          if(NEEDED.contains(keys[0])){
            tempkey = new Vector<String>();
            tempkey.add(keys[0]);
            tempkey.add(keys[1]);
            TesttableC.put(tempkey, Integer.parseInt(tokens[1]));
          }
        }
      }
      tempkey.clear();
      tempkey.add("*");
      traintotalinstance = fixedTable.get(tempkey);
    }
    catch(Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  private static void readNEEDED(String inputfile){
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputfile);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String s, content;
      int splitposition;
      // Read File Line By Line
      while ((s = br.readLine()) != null) {
        splitposition = s.indexOf("\t");
        content = s.substring(splitposition + 1,s.length());
        Vector<String> neededfeathers = tokenizeDoc(content);

        //read in the counters according to the example  
        
        for(String str: neededfeathers){
          NEEDED.add(str);
        }
      }
      br.close();
      in.close();
    }catch(Exception e){
      System.err.println("Error in readNEEDED: " + e.getMessage());
    }
  }


  private static void updateCounter(String inputfile) throws IOException {
    
    double logprob = -Double.MAX_VALUE, maxlogprob = -Double.MAX_VALUE;
    int cnt = 0;
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputfile);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      //the second series of inputstreams which used to compute logprob
      FileInputStream fstream2 = new FileInputStream(inputfile);
      DataInputStream in2 = new DataInputStream(fstream2);
      BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
      
      
      String s, labels, content;
      int splitposition;
      // Read File Line By Line
      int readcounter = 0;
      while ((s = br.readLine()) != null) {
        readcounter ++;     
        testtotalinstance ++;
        splitposition = s.indexOf("\t");
        labels = s.substring(0, splitposition);
        content = s.substring(splitposition + 1,s.length());
        logprob = -Double.MAX_VALUE;
        maxlogprob = -Double.MAX_VALUE;
        String resultlabel = new String();
        splitposition = s.indexOf("\t");
        Vector<String> feathers = tokenizeDoc(content);
        
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
      br.close();
      in.close();
    } catch (Exception e) {// Catch exception if any
      System.err.println("Error in updateCounter: " + e.getMessage());
    }
  }

  public static void main(String args[]) throws IOException {
    
    if (args.length != 3 || !args[0].equals("-t")) {
      System.out.println("usage: NBTest -t <inputTestFile> <inputModel>");
      System.exit(0);
    }
    String inputfile = args[1];
    inputmodel = args[2];
    
    readNEEDED(inputfile);
    //initwithModel("model");
    initwithModel(inputmodel);
    
    updateCounter(inputfile);
    
    System.out.println("Percent correct: " + correctness + "/" + testtotalinstance + "=" + (correctness / (double)testtotalinstance)*100 + "%");
  }

}

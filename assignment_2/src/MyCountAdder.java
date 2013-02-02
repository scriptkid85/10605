import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;



public class MyCountAdder {
  
  private static Hashtable<Vector<String>, Integer> traincounters = new Hashtable<Vector<String>, Integer>();

  private static HashSet<String> vocabulary = new HashSet<String>();
  
  
  private static void aggregateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s;
    String[] tokens;
    Vector<String> tempkey = new Vector<String>();
    s = in.readLine();
    int sumForPreviousKey = 0;
    String prekey = s.split("\t")[0];
    sumForPreviousKey += Integer.parseInt(s.split("\t")[1]); 
    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens.length < 2){
          System.out.println(s);
      }
      if(tokens[0].equals(prekey)){
        sumForPreviousKey += Integer.parseInt(tokens[1]);
      }
      else{  
        outputcounter.write(prekey + "\t" + sumForPreviousKey + "\n");
        outputcounter.flush();
        prekey = tokens[0];
        sumForPreviousKey = Integer.parseInt(tokens[1]);
      }
    }
    
    outputcounter.write(prekey + "\t" + sumForPreviousKey + "\n");
    outputcounter.flush();
  }
  
  
  private static void aggregateFromFile(String inputhashtable){
    try {
      // Open the file that is the first
      // command line parameter
      FileInputStream fstream = new FileInputStream(inputhashtable);
      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));

      String s;
      String[] tokens;
      s = br.readLine();
      int sumForPreviousKey = 0;
      String prekey = s.split("\t")[0];
      sumForPreviousKey += Integer.parseInt(s.split("\t")[1]); 
      while ((s = br.readLine()) != null && s.length() != 0) {
        tokens = s.split("\t");
        if(tokens[0].equals("tr^swadesh")){
            int a = 1;
            a ++;
        }
        if(tokens[0].equals(prekey)){
          sumForPreviousKey += Integer.parseInt(tokens[1]);
        }
        else{  
          outputcounter.write(prekey + "\t" + sumForPreviousKey + "\n");
          outputcounter.flush();
          prekey = tokens[0];
          sumForPreviousKey = Integer.parseInt(tokens[1]);
        }
      }
      
      outputcounter.write(prekey + "\t" + sumForPreviousKey + "\n");
      outputcounter.flush();
      
      
    }
    catch(Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  
  public static void main(String args[]){
    //aggregateCounter("raw");
    aggregateFromFile("raw.txt");
  }
}

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



public class MyCounterStep3 {
  
  private static Hashtable<Vector<String>, Integer> traincounters = new Hashtable<Vector<String>, Integer>();

  private static long vocabularysize = 0;
  
  private static long phrasesize = 0;
  
  
  private static void aggregateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s;
    String[] tokens;
    
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    
    
    s = in.readLine();
    phrasesize = 1;
    String presum = s.split("\t")[2];
    
    while ((s = in.readLine()) != null && s.length() != 0) {
      phrasesize ++;
      if(phrasesize % 2 == 0){
        tokens = s.split("\t");
        outputcounter.write(tokens[0] + "\t" + tokens[1] + " " + tokens[2] + " " + presum + "\n");
        outputcounter.flush();
      }
      else{
        presum = s.split("\t")[2];
      }
    }
    phrasesize /= 2;
  }
  
  
  private static void aggregateFromFile(String inputfile) throws IOException{
    FileInputStream fstream = new FileInputStream(inputfile);
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s;
    String[] tokens;
    
    s = br.readLine();
    phrasesize = 1;
    String prekey = s;
    
    while ((s = br.readLine()) != null && s.length() != 0) {
      phrasesize ++;
      if(phrasesize % 2 == 0){
        tokens = s.split("\t");
        outputcounter.write(prekey + "\t" + tokens[1] + "\n");
        outputcounter.flush();
      }
      else{
        prekey = s;
      }
    }
    
    phrasesize /= 2;
    outputcounter.write(phrasesize + "\n");
  }
  
  
  public static void main(String args[]){
    vocabularysize = 0;
    try {
      aggregateCounter();
      //aggregateFromFile("output");
    } catch (IOException e) {
      e.printStackTrace();
    }
//    aggregateFromFile("raw");
  }
}

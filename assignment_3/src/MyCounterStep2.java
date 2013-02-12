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



public class MyCounterStep2 {
  
  private static Hashtable<Vector<String>, Integer> traincounters = new Hashtable<Vector<String>, Integer>();

  private static int vocabularysize;
  
  private static int labelspace;
  
  
  private static void aggregateCounter() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s, prekey, BsumForBigram ;
    String[] tokens, words;

    String BsumForUnigram, CsumForUnigram;
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    s = in.readLine();
    outputcounter.write(s + "\n");
    outputcounter.flush();
    
    
    s = in.readLine();
    String preunigram = s.split("\t")[0]; 
    BsumForUnigram = s.split("\t")[1];
    s = in.readLine();
    CsumForUnigram = s.split("\t")[1];
    prekey = "";
    BsumForBigram = "";

    while ((s = in.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      words = tokens[0].split(" ");
      if(words.length > 2){
        if(words[1].equals(words[2])){
          BsumForBigram = tokens[1];
          s = in.readLine();
          s = in.readLine();
          s = in.readLine();
          tokens = s.split("\t");
          words = tokens[0].split(" ");
          outputcounter.write(words[1] + " " + words[2] + "\t" + BsumForBigram + " " + tokens[1] + "\t" + BsumForUnigram + " " + CsumForUnigram + "\n");
          outputcounter.flush();
          outputcounter.write(words[1] + " " + words[2] + "\t" + BsumForBigram + " " + tokens[1] + "\t" + BsumForUnigram + " " + CsumForUnigram + "\n");
          outputcounter.flush();
        }
        else{
          if(!tokens[0].equals(prekey)){
            prekey = tokens[0];
            BsumForBigram = tokens[1];
  
          }
          else{
            outputcounter.write(words[1] + " " + words[2] + "\t" + BsumForBigram + " " + tokens[1] + "\t" + BsumForUnigram + " " + CsumForUnigram + "\n");
            outputcounter.flush();
  
          }
        }
      }
      else{
        preunigram = tokens[0]; //key is word \t decade \t number
        BsumForUnigram = tokens[1];
        s = in.readLine();
        CsumForUnigram = s.split("\t")[1];
      }
    }
  }
  
  
  private static void aggregateFromFile(String inputfile) throws IOException{
    FileInputStream fstream = new FileInputStream(inputfile);
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    BufferedWriter outputcounter = new BufferedWriter(new OutputStreamWriter(System.out));
    String s;
    String[] tokens, words;

    int sumForUnigram = 0;
    s = br.readLine();
    outputcounter.write(s);
    outputcounter.flush();
    String preunigram = s.split("\t")[0]; //key is word \t decade \t number
    sumForUnigram = Integer.parseInt(s.split("\t")[1]);
    
    while ((s = br.readLine()) != null && s.length() != 0) {
      tokens = s.split("\t");
      if(tokens[0].split(" ").length > 1){
        words = tokens[0].split(" ");
        outputcounter.write(words[1] + " " + words[2] + " " + tokens[1] + "\t" + preunigram + " " + sumForUnigram + "\n");
        outputcounter.flush();
      }
      else{
        preunigram = tokens[0]; //key is word \t decade \t number
        sumForUnigram = Integer.parseInt(tokens[1]);
      }
    }
  }
  
  
  public static void main(String args[]){
    vocabularysize = 0;
    labelspace = 0;
    try {
      aggregateCounter();
      //aggregateFromFile("output");
    } catch (IOException e) {
      e.printStackTrace();
    }
//    aggregateFromFile("raw");
  }
}

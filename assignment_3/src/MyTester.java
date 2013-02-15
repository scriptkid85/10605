import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;

public class MyTester {

  private static long fvocabularynum = 0;
  
  private static long fphrasenum = 0;
  
  private static long bvocabularynum = 0;
  
  private static long bphrasenum = 0;
  
  private static long phrasesize = 0;
  
  private static long vocabularysize = 0;
  
  private static Comparator<Myentry> comparator = new MyentryComparator();
  
  private static PriorityQueue<Myentry> PQ = new PriorityQueue<Myentry>(10, comparator); 
  
  
  private static double computeKL(double p, double q){
    return p * Math.log(p / q);
  }
  
  private static void computeScore(String s){
    String[] splits = s.split("\t");
    String phrase = splits[0];
    String counters = splits[1];
    String[] values = counters.split(" ");
    long BXYcount = Long.parseLong(values[1]);
    long CXYcount = Long.parseLong(values[3]);

    long CXcount = Long.parseLong(values[7]);

    long CYcount = Long.parseLong(values[11]);
    
    double PfgXY = (CXYcount + 1) / (double)(fphrasenum + phrasesize);
    double PbgXY = (BXYcount + 1) / (double)(bphrasenum + phrasesize);
    double PfgX = (CXcount + 1) / (double)(fvocabularynum + vocabularysize);
    double PfgY = (CYcount + 1) / (double)(fvocabularynum + vocabularysize);
    
    double Phraseness = computeKL(PfgXY, PfgX * PfgY);
    double Informativeness = computeKL(PfgXY, PbgXY);
    String output = "<" + phrase + ">\t<" + String.format("%.3g", (Phraseness + Informativeness)) + ">\t<" + String.format("%.3g", Phraseness) + ">\t<" + String.format("%.3g", Informativeness) + ">";
    Myentry entry = new Myentry(output, Phraseness, Informativeness);
    
    
    PQ.add(entry);
    if(PQ.size() > 20){
      PQ.remove();
    }
    

 }

  private static void readsource(){
    Vector<String> tempkey = new Vector<String>();
    try {
      // Open the file that is the first
      // command line parameter
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String s;
      String[] tokens, keys;
      s = in.readLine();
      bphrasenum = Long.parseLong(s.split("\t")[1]);
      s = in.readLine();
      bvocabularynum = Long.parseLong(s.split("\t")[1]);
      s = in.readLine();
      fphrasenum = Long.parseLong(s.split("\t")[1]);
      s = in.readLine();
      fvocabularynum = Long.parseLong(s.split("\t")[1]);
      
      s = in.readLine();
      phrasesize = Long.parseLong(s.split("\t")[1]);
      
      s = in.readLine();
      vocabularysize = Long.parseLong(s.split("\t")[1]);
      
      while ((s = in.readLine()) != null) {
        computeScore(s);
      }
    }
    catch(Exception e) {// Catch exception if any
      System.err.println("Error in readsource: " + e.getMessage());
      e.printStackTrace();
    }
  }


  public static void main(String args[]) throws IOException {
    readsource();
    Stack<Myentry> stack = new Stack<Myentry>();
    while(!PQ.isEmpty()){
      Myentry e = PQ.remove();
      stack.push(e);
    }
    while(!stack.isEmpty()){
      stack.peek().printSelf();
      stack.pop();
    }
  }

}

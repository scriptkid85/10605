
public class Myentry{
  private String output;
  private double Phraseness;
  private double Informativeness;
  private double totalscore;
  
  public Myentry(String s, double p, double i){
    output = s;
    Phraseness = p;
    Informativeness = i;
    totalscore = Phraseness + Informativeness;
  }
  
  public double getTotal(){
    return totalscore;
  }
  
  public void printSelf(){
    System.out.println(output);
  }
}

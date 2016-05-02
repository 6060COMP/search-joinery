package joins;

import java.util.Random;

/**
 * A fictional parts catalogue used to illustrate joins in SearchJoinery
 * 
 * @author david
 *
 */
public class Warehouse {
  static Random rnd = new Random();

  public Warehouse()
  {
  }
  
  public float searchPrice(String term, float rrp)
  {
    int secs = rnd.nextInt(5);
    
    float multiplier = rnd.nextFloat();
    
    try
    {
      Thread.sleep(secs*1000);
    }
    catch (InterruptedException e)
    {}
    System.out.println("---Search took "+secs+" seconds");
    return rrp*(multiplier+.5f);
  }
  
  
}

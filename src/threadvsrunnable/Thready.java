package threadvsrunnable;

public class Thready extends Thread {

  public void run() /*thread "work" code goes here*/ {
    int i = 0;
    while (i < 10) {
      for (int y = 1; y <= 10; y++)
        System.out.println(y + " * " + i + " = " + (y * i));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      i++;
    }
  }
  
  public static void main(String[] args) {
    Thready thready = new Thready();
    thready.start();
  }
}

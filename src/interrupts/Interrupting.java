package interrupts;

import java.util.Random;

/**
 * Very simple - albeit a little macabre - demo class to show interrupt-based
 * communications between threads Here, the interrupt is used as a safe
 * alternative to Thread.stop
 * 
 * Note how the resulting termination is not instant. friendlyThread gets to do
 * what it wants before terminating; interrupt is just that - an interrupt, not
 * a kill signal.
 * 
 * In most executions, the two checks for alive: <BR>
 * -immediately after the interrupt, and <BR>
 * -0.5s after the interrupt <BR>
 * will give different results (true, false)
 * 
 * @author david
 *
 */
public class Interrupting {

  public static void main(String[] args) {
    Thread friendlyThread = new Thread() {
      public void run() {
        while (!isInterrupted()) {
          System.out.println("Hello friends!");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            interrupt(); // restate interrupt
            System.out.println("Woken rudely from my sleep!");
          }
        }
        System.out.println("Surely these can't be my last words");
        System.out.println("...");
      }
    };

    Thread theReaperThread = new Thread() {
      public void run() {
        try {
          Random rnd = new Random(System.currentTimeMillis());
          int secs = rnd.nextInt(10);
          System.err.println("Reaper will collect in " + secs + "s");
          Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
        }
        System.err.println("The reaper collects...");
        friendlyThread.interrupt();

        System.err.println("Is Friendly alive : " + friendlyThread.isAlive());

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        System.err.println("Is Friendly alive now : "
            + friendlyThread.isAlive());

      }
    };

    friendlyThread.start();
    theReaperThread.start();
  }

}

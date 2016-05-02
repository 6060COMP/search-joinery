package waitandnotify;

import java.util.Vector;

/**
 * This class is a simple demonstration class used to show how a time-delayed
 * polling approach can be used to control access to a buffer. The buffer should
 * only be read when it has data inside it.
 * 
 * @see NotifyBuffer for a non-polling alternative
 * 
 * @author david
 *
 */
public class PollingBuffer {

  Vector<String> aStringBuffer = new Vector<String>();

  public void add(String string) {
    aStringBuffer.add(string);
  }

  public String get() {
    return aStringBuffer.remove(0);
  }

  public boolean isEmpty() {
    return aStringBuffer.isEmpty();
  }

  public static void main(String[] args) {
    final PollingBuffer buffer = new PollingBuffer();

    Thread putter = new Thread() {
      public void run() {
        while (true) {
          buffer.add("It was a bright cold day in April");
          buffer.add("and the clocks were striking thirteen");

          buffer.add("Sanity was statistical.");
          buffer
              .add("It was merely a question of learning to think as they thought");

          buffer.add("Freedom is slavery.");
          buffer.add("Two and two make five");

          System.out.println("Buffer filled!");
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            //do something
          }
        }
      }
    };

    putter.start();

    Thread getter = new Thread() {
      private int latency = 800;

      public void run() {
        while (true) {
          while (buffer.isEmpty()) {
            try {
              Thread.sleep(latency);
            } catch (InterruptedException e) {
            }
          }

          System.out.println("Got:  " + buffer.get());

        }
      }
    };
    getter.start();
  }

}

package waitandnotify;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a simple demonstration class used to show how a wait/notify
 * message scheme can be used to control access to a buffer. The buffer should
 * only be read when it has data inside it.
 * 
 * Study the anonymous inner thread irresponsibleGetter for an example of how
 * *not* to respond to a notify message. What issues does this cause?
 * 
 * @see PollingBuffer for a non-polling alternative
 * 
 * @author david
 *
 */

public class NotifyBuffer {

  List<String> aStringBuffer = new ArrayList<String>();

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
    final NotifyBuffer buffer = new NotifyBuffer();

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
          synchronized (buffer) {
            buffer.notifyAll();
          }
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {

          }
        }
      }
    };

    putter.start();

    Thread getter = new Thread() {
      public void run() {
        while (true) {
          synchronized (buffer) {
            while (buffer.isEmpty()) {
              try {
                buffer.wait();
              } catch (InterruptedException e) {
              }
            }
          }

          System.out.println("Got:  " + buffer.get());

        }
      }
    };
    getter.start();

    Thread irresponsibleGetter = new Thread() {
      public void run() {
        while (true) {
          synchronized (buffer) {
            // this logic is flawed - this if statement should be a loop
            if (buffer.isEmpty()) { // this is race condition-prone
              try {
                buffer.wait(); // when this releases,
                // it should go to check that buffer.isEmpty() is still false
              } catch (InterruptedException e) {
              }
            }
          }

          // this may get here at the same time as another thread; both calling
          // get() together
          System.out.println("IRR:  " + buffer.get());

        }
      }
    };
    irresponsibleGetter.start();
  }

}

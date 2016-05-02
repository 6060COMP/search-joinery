package flagcontrol;

import java.util.LinkedList;
import java.util.Random;

/**
 * Simple simulated buffer class demonstrates (lack of) flag-based thread control without
 * using Thread.suspend() and Thread.resume()
 * 
 * @see ControlledBuffer
 * 
 * @author david
 *
 */
public class SimulatedBuffer {

  private LinkedList<Integer> buffer = new LinkedList<Integer>();

  public boolean shouldRead() {
    return (buffer.size() > 0);
  }

  public int read() {
    return buffer.poll();
  }

  public synchronized void write(int toWrite) {
    buffer.offer(toWrite);
  }

  public static void main(String[] args) {

    final SimulatedBuffer buff = new SimulatedBuffer();

    Thread reader = new Thread() {
      public void run() {

        try {
          Thread.sleep(1000);
          while (true) { // assume buffer always full
            int readBuffer = 0;
            while (readBuffer < 1000) {
              buff.read();
              readBuffer++;
            }
            System.out.println("Read " + readBuffer + " chars");
            Thread.sleep(1000); // simulate 1s processing
          }
        } catch (InterruptedException e) {

        }
      }
    };

    Thread writer = new Thread() {
      public void run() {
        Random rnd = new Random();
        try {

          while (reader.isAlive()) {
            int writeBuffer = 0;
            while (writeBuffer < 1000) {
              buff.write(writeBuffer);
              writeBuffer++;
            }
            System.out.println("**Written " + writeBuffer + " chars");

            int sleepy = rnd.nextInt(2000);
            if (sleepy < 970)
              sleepy = 970;
            Thread.sleep(sleepy);
          }
        } catch (InterruptedException e) {

        }

      }
    };

    writer.start();
    reader.start();

  }
}

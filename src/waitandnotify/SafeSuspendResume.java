package waitandnotify;

/**
 * This class shows how the deprecated java methods {@link Thread#suspend()} and
 * {@link Thread#resume()} can have their behaviour safely re-implemented using
 * a wait / notify messaging scheme.
 * 
 * It also uses an interrupt to create a near-instant stop method. Note that
 * restarting a thread is permitted, but this is not entirely threadsafe.
 * 
 * Note that this "thread's" run method doesn't do an awful lot other than log
 * the state of the system.
 * 
 * @author david
 *
 */
public class SafeSuspendResume implements Runnable {

  private boolean suspended;
  private boolean stopped;
  private Thread running;

  @Override
  public void run() {
    while (!stopped) {
      try {
        Thread.sleep(1000);

        synchronized (this) {
          while (suspended && !stopped)
            wait();
          // will wait once the thread is marked suspended.
          // execution will only proceed when a) the object is notified
          // (#notify), or b) the thread is interrupted (Thread#interrupt).
        }
        System.out.println("Alt Thread " + toString() + " is RUNNING!");
      } catch (InterruptedException e) {
        System.err.println("interrupt!");
      }

    }
    System.out.println("Alt thread " + toString() + " STOPPED!");
    System.out.println("\n\n\n");
  }

  public boolean start() {
    // check to see if a currently running thread exists
    if (running == null || !running.isAlive()) {
      // reset flags
      suspended = stopped = false;

      // create a new thread and start it
      running = new Thread(this);
      running.start();
      return true;
    }

    return false;
  }

  public void stop() {
    stopped = true; // sets the stopped flag
    running.interrupt(); // then interrupts the running thread
  }

  public void suspend() {
    suspended = true; // sets the suspended flag
    running.interrupt();
  }

  public synchronized void resume() {
    suspended = false;
    notifyAll();
  }

  public static void main(String[] args) {
    SafeSuspendResume thread = new SafeSuspendResume();

    try {
      System.out.println("start");
      thread.start();
      Thread.sleep(2000);
      System.out.println("suspend");
      thread.suspend();
      Thread.sleep(4000);
      System.out.println("resume");
      thread.resume();
      Thread.sleep(2000);
      System.out.println("stop");
      thread.stop();
      Thread.sleep(4000);

      // restart it - note java.lang.Thread cannot restart once finished
      System.out.println("restart");
      thread.start();
      Thread.sleep(2000);
      System.out.println("suspend");
      thread.suspend();
      Thread.sleep(4000);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("stop");
    thread.stop();

  }
}

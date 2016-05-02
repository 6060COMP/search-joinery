package joins;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * Simple class to demonstrate the use of Thread.join() as a barrier
 * synchronisation mechanism.
 * 
 * Several mock warehouses are associated with a searching algorithm, each of
 * which can return a price for a part. All searches can run in parallel, but
 * the aggregating thread that compares them must wait for them all to finish
 * before deciding which is best.
 * 
 * Look at the {@link #search()} method, and the associated class {@link Warehouse}
 * 
 */
public class SearchJoinery {

  private final int WAREHOUSE_COUNT = 5;
  private String searchTerm;
  private Vector<Float> results;
  private Vector<Warehouse> warehouses;

  public SearchJoinery(String searchTerm) {
    this.results = new Vector<Float>();
    this.searchTerm = searchTerm;
    this.warehouses = new Vector<Warehouse>();

    for (int cnt = 0; cnt < WAREHOUSE_COUNT; cnt++)
      warehouses.add(new Warehouse());
  }

  /**
   * Begins and coordinates the "worker" threads, each of which searches a different warehouse
   * @return
   * @throws InterruptedException
   */
  public Vector<String> search() throws InterruptedException {
    ArrayList<Thread> threads = new ArrayList<Thread>();
    // create a new CountDownLatch
    CountDownLatch latch = new CountDownLatch(WAREHOUSE_COUNT);
    // create search threads and start them
    for (Warehouse warehouse : warehouses) {
      Thread thread = new Thread() {
        public void run() {
          results.add(warehouse.searchPrice(searchTerm, 30));
          // after operation has completed, count down the latch
          latch.countDown();
        }
      };
      threads.add(thread);
      thread.start();
    }

    // await the latch
    latch.await();

    // work through returned results
    Vector<String> prices = sortPrices();
    return prices;
  }

  /*
   * Compares the final results and selects the best one; outputting its data
   * This method must only start when ALL results are in!
   */
  public Vector<String> sortPrices() {
    Vector<String> prices = new Vector<String>();
    float lowestPrice = Float.MAX_VALUE;
    int bestWarehouse = -1;
    for (int cnt = 0; cnt < results.size(); cnt++) {
      if (results.get(cnt) < lowestPrice) {
        lowestPrice = results.get(cnt);
        bestWarehouse = cnt;
      }
      prices.add("£" + String.format("%.2f", results.get(cnt))
          + " from warehouse " + (cnt + 1));
    }

    System.out.println("Best warehouse " + (bestWarehouse + 1) + ", at "
        + lowestPrice);
    return prices;
  }

  public static void main(String[] args) throws InterruptedException {

    Scanner scan = new Scanner(System.in);

    System.out.println("Please enter your part number: ");
    String searchTerm = scan.nextLine();

    SearchJoinery search = new SearchJoinery(searchTerm);
    Vector<String> results = search.search();

    System.out.println("Found the following prices");
    for (String result : results)
      System.out.println("-" + result);

    scan.close();
  }

}
